package com.ruoyi.platform.chat.handler.impl;

import com.ruoyi.platform.chat.handler.MessageHandler;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import com.ruoyi.platform.chat.utils.ChatCacheUtils;
import com.ruoyi.platform.domain.ChatMessage;
import com.ruoyi.platform.service.IChatMessageService;
import com.ruoyi.platform.service.IChatSessionService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class OfflineMessagePullHandler implements MessageHandler {

    // 缓存相关常量
    private static final String CACHE_KEY_PREFIX = "chat:sessionId:";
    private static final String CACHE_KEY_SUFFIX = ":messages";
    private static final int MAX_CACHE_MESSAGE_COUNT = 100;
    private static final long CACHE_EXPIRE_DAYS = 7;

    @Autowired
    private IChatMessageService chatMessageService;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private IChatSessionService chatSessionService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChatCacheUtils chatCacheUtils;

    @Override
    public long supportType() {
        return 3L;
    }

    @Override
    public void handler(ChannelSessionManager channelSessionManager, ChannelHandlerContext ctx, ChatMessageProto.ChatMessage chatMessage) {
        executorService.execute(() -> {
            try {
                // 1. 获取上线用户的ID和类型（拉取者即接收方）
                Long receiverId = chatMessage.getFromId();
                Long receiverType = (long) chatMessage.getFromType();
                String receiverKey = receiverType + ":" + receiverId;
                Channel receiverChannel = channelSessionManager.getChannel(receiverKey);

                // 校验通道是否活跃
                if (Objects.isNull(receiverChannel) || !receiverChannel.isActive()) {
                    log.warn("用户{}通道已关闭，无法推送离线消息", receiverId);
                    return;
                }

                // 2. 查询该用户的所有离线消息（msg_status=3）
                ChatMessage queryMsg = new ChatMessage();
                queryMsg.setToId(receiverId);
                queryMsg.setToType(receiverType);
                queryMsg.setMsgStatus(3L);
                List<ChatMessage> offlineMsgList = chatMessageService.selectChatMessageList(queryMsg);

                if (offlineMsgList.isEmpty()) {
                    log.debug("用户{}无离线消息", receiverId);
                    // 发送无离线消息的系统通知
                    sendNoOfflineMessageResponse(receiverChannel);
                    return;
                }

                log.info("为用户{}拉取到{}条离线消息", receiverId, offlineMsgList.size());

                // 3. 推送离线消息给用户，并更新状态和缓存
                for (ChatMessage offlineMsg : offlineMsgList) {
                    pushOfflineMessage(channelSessionManager, receiverChannel, offlineMsg);
                }

            } catch (Exception e) {
                log.error("处理离线消息拉取异常", e);
            }
        });
    }

    /**
     * 推送单条离线消息
     */
    private void pushOfflineMessage(ChannelSessionManager channelSessionManager, Channel receiverChannel, ChatMessage offlineMsg) {
        try {
            // 转换为Protobuf消息
            ChatMessageProto.ChatMessage protoMsg = convertToProtoMessage(offlineMsg);

            // 生成二进制帧
            ByteBuf buf = generateByteBuf(protoMsg);

            // 推送消息
            receiverChannel.writeAndFlush(new BinaryWebSocketFrame(buf))
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            // 推送成功：更新消息状态为"已送达"
                            offlineMsg.setMsgStatus(1L);
                            offlineMsg.setDeliverTime(new Date());
                            transactionTemplate.execute(status -> {
                                try {
                                    // 步骤1：更新数据库状态为“已送达”
                                    offlineMsg.setMsgStatus(1L);
                                    offlineMsg.setDeliverTime(new Date());
                                    int updateRows = chatMessageService.updateChatMessage(offlineMsg); // 注意：此方法需移除@Transactional注解
                                    if (updateRows == 0) {
                                        throw new RuntimeException("数据库状态更新失败，消息ID: " + offlineMsg.getMessageId());
                                    }

                                    // 步骤2：写入缓存（若缓存失败，事务会回滚）
                                    chatCacheUtils.cacheChatMessage(offlineMsg);

                                    log.debug("事务提交成功，消息ID: {}", offlineMsg.getMessageId());
                                    return true; // 事务正常提交
                                } catch (Exception e) {
                                    // 发生异常，手动回滚事务
                                    status.setRollbackOnly();
                                    log.error("事务执行失败，已回滚，消息ID: {}", offlineMsg.getMessageId(), e);

                                    return false; // 事务回滚
                                }
                            });

                        } else {
                            log.error("离线消息推送失败，消息ID: {}", offlineMsg.getMessageId(), future.cause());
                        }
                    });
        } catch (Exception e) {
            log.error("推送离线消息异常，消息ID: {}", offlineMsg.getMessageId(), e);
        }
    }

    /**
     * 将消息存入Redis缓存
     */
    private void cacheMessage(ChatMessage message) {
        try {
            if (message.getSessionId() == null) {
                log.warn("消息会话ID为空，无法缓存，消息ID: {}", message.getMessageId());
                return;
            }


            String cacheKey = buildCacheKey(message.getSessionId());


            // 使用右推（最新消息在列表尾部）
            redisTemplate.opsForList().rightPush(cacheKey, message);

            // 修剪列表，保持最大消息数量
            redisTemplate.opsForList().trim(cacheKey, -MAX_CACHE_MESSAGE_COUNT ,-1 );

            // 设置过期时间
            redisTemplate.expire(cacheKey, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);

            log.debug("消息缓存成功，缓存Key: {}, 消息ID: {}", cacheKey, message.getMessageId());
        } catch (Exception e) {
            log.error("消息缓存失败，消息ID: {}", message.getMessageId(), e);
        }
    }

    /**
     * 构建缓存Key
     */
    private String buildCacheKey(Long sessionId) {
        return CACHE_KEY_PREFIX + sessionId + CACHE_KEY_SUFFIX;
    }

    /**
     * 发送无离线消息响应
     */
    private void sendNoOfflineMessageResponse(Channel receiverChannel) {
        try {
            ChatMessageProto.ChatMessage noMessageResponse = ChatMessageProto.ChatMessage.newBuilder()
                    .setMsgType(4)
                    .setMsgContent("暂无离线消息")
                    .setSendTime(new Date().toString())
                    .build();

            ByteBuf buf = generateByteBuf(noMessageResponse);
            receiverChannel.writeAndFlush(new BinaryWebSocketFrame(buf));
        } catch (Exception e) {
            log.error("发送无离线消息响应失败", e);
        }
    }

    /**
     * 数据库ChatMessage转换为Protobuf消息
     */
    private ChatMessageProto.ChatMessage convertToProtoMessage(ChatMessage dbMsg) {
        return ChatMessageProto.ChatMessage.newBuilder()
                .setMessageId(dbMsg.getMessageId())
                .setFromId(dbMsg.getFromId())
                .setFromType(dbMsg.getFromType().intValue())
                .setToId(dbMsg.getToId())
                .setToType(dbMsg.getToType().intValue())
                .setMsgType(dbMsg.getMsgType().intValue())
                .setMsgContent(dbMsg.getMsgContent() != null ? dbMsg.getMsgContent() : "")
                .setMsgStatus(dbMsg.getMsgStatus().intValue())
                .setSendTime(dbMsg.getSendTime() != null ? dbMsg.getSendTime().toString() : "")
                .setSessionId(dbMsg.getSessionId() != null ? dbMsg.getSessionId() : 0)
                .build();
    }

    /**
     * 生成二进制缓冲区
     */
    private ByteBuf generateByteBuf(ChatMessageProto.ChatMessage msg) {
        byte[] protoBytes = msg.toByteArray();
        int dataLength = protoBytes.length;
        ByteBuf buf = Unpooled.buffer(8 + 1 + 4 + dataLength);
        buf.writeLong(0xCAFEBABEL);
        buf.writeByte((byte) msg.getMsgType());
        buf.writeInt(dataLength);
        buf.writeBytes(protoBytes);
        return buf;
    }
}