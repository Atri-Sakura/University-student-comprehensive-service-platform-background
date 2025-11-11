package com.ruoyi.platform.chat.handler.impl;

import com.ruoyi.platform.chat.handler.MessageHandler;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import com.ruoyi.platform.chat.method.ChatOperateMethod;
import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import com.ruoyi.platform.chat.utils.ChatCacheUtils;
import com.ruoyi.platform.domain.ChatMessage;
import com.ruoyi.platform.domain.ChatSession;
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

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

@Component
@Slf4j
public class RetriveMessageHandler implements MessageHandler {

    // 协议常量（与TextMessageHandler保持一致，确保协议统一）
    private static final long PROTOCOL_MAGIC_NUMBER = 0xCAFEBABEL;

    @Autowired
    private ExecutorService messageExecutor;

    @Autowired
    private IChatSessionService chatSessionService;

    @Autowired
    private ChatOperateMethod chatOperateMethod;

    @Autowired
    private IChatMessageService chatMessageService;

    @Autowired
    private ChatCacheUtils chatCacheUtils;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private ChannelSessionManager channelSessionManager;

    @Override
    public long supportType() {
        return 5; // 撤回消息类型为5（与TextMessageHandler的1区分）
    }

    @Override
    public void handler(ChannelSessionManager channelSessionManager, ChannelHandlerContext ctx, ChatMessageProto.ChatMessage chatMessage) {
        messageExecutor.execute(() -> {
            try {
                // 1. 参数校验
                if (chatMessage.getMessageId() == 0) {
                    sendErrorResponse(ctx, "被撤回的消息Id不能为空");
                    return;
                }
                if (chatMessage.getFromType() == 0 && chatMessage.getToType() == 0) {
                    sendErrorResponse(ctx, "未确认角色类型");
                    return;
                }
                if (chatMessage.getFromId() == 0 && chatMessage.getToId() == 0) {
                    sendErrorResponse(ctx, "角色为空");
                    return;
                }

                // 2. 会话与消息查询
                Long sessionId = chatSessionService.selectChatSessionIdByFromTo(
                        (long) chatMessage.getFromType(),
                        chatMessage.getFromId(),
                        (long) chatMessage.getToType(),
                        chatMessage.getToId()
                );
                if (sessionId == null) {
                    sendErrorResponse(ctx, "会话不存在");
                    return;
                }

                ChatMessage dbMsg = chatMessageService.selectChatMessageByMessageId(chatMessage.getMessageId());
                if (dbMsg == null) {
                    sendErrorResponse(ctx, "消息不存在");
                    return;
                }

                // 3. 权限与时效校验
                if (chatMessage.getFromId() != dbMsg.getFromId()) {
                    sendErrorResponse(ctx, "不能撤销别人的消息");
                    return;
                }

                long msgCreateTime = dbMsg.getCreateTime().getTime();
                long now = System.currentTimeMillis();
                if (now - msgCreateTime > 60 * 2 * 1000) { // 2分钟时效
                    sendErrorResponse(ctx, "时间超过2分钟，无法撤回");
                    return;
                }

                // 4. 更新消息状态（撤回）
                dbMsg.setIsDeleted(1L);
                dbMsg.setUpdateTime(new Date());
                dbMsg.setMsgStatus(3L);
                chatMessageService.updateChatMessage(dbMsg);

                // 5. 清理缓存
                chatCacheUtils.deleteMessageFromCache(chatMessage.getMessageId());

                // 6. 推送撤回通知（使用ByteBuf缓冲区）
                pushToReceiver(ctx, chatMessage, sessionId);

                // 7. 向发起者发送成功响应
                sendSuccessResponse(ctx);

            } catch (Exception e) {
                log.error("消息撤回失败", e);
                sendErrorResponse(ctx, e.getMessage());
            }
        });
    }

    /**
     * 发送成功响应（使用ByteBuf缓冲区）
     */
    private void sendSuccessResponse(ChannelHandlerContext ctx) {
        ChatMessageProto.ChatMessage successMsg = ChatMessageProto.ChatMessage.newBuilder()
                .setMsgType(5) // 撤回消息类型
                .setMsgContent("消息撤回成功")
                .build();
        // 生成ByteBuf并封装为二进制帧发送
        ByteBuf buf = generateByteBuf(successMsg);
        ctx.writeAndFlush(new BinaryWebSocketFrame(buf));
    }

    /**
     * 推送撤回通知给接收者（核心：使用ByteBuf构建协议格式）
     */
    public void pushToReceiver(ChannelHandlerContext ctx, ChatMessageProto.ChatMessage chatMessage, Long sessionId) {
        // 1. 构建撤回通知消息体
        ChatMessageProto.ChatMessage notifyMsg = ChatMessageProto.ChatMessage.newBuilder()
                .setFromId(chatMessage.getFromId())
                .setToId(chatMessage.getToId())
                .setFromType(chatMessage.getFromType())
                .setMessageId(chatMessage.getMessageId())
                .setSessionId(sessionId)
                .setToType(chatMessage.getToType())
                .setMsgType(5) // 撤回消息类型
                .setMsgContent("[撤回一条消息]")
                .build();

        // 2. 生成符合协议的ByteBuf缓冲区
        ByteBuf buf = generateByteBuf(notifyMsg);

        // 3. 获取接收者通道并发送（二进制帧）
        String receiverKey = chatMessage.getToType() + ":" + chatMessage.getToId();
        Channel receiverChannel = channelSessionManager.getChannel(receiverKey);
        if (receiverChannel != null && receiverChannel.isActive() && receiverChannel.isWritable()) {
            receiverChannel.writeAndFlush(new BinaryWebSocketFrame(buf))
                    .addListener(future -> {
                        if (!future.isSuccess()) {
                            log.error("撤回通知推送失败，消息ID: {}", chatMessage.getMessageId(), future.cause());
                        }
                    });
        } else {
            // 通道无效时释放缓冲区（避免内存泄漏）
            if (buf.refCnt() > 0) {
                buf.release();
            }
            log.warn("接收者通道未激活，撤回通知未发送，接收者Key: {}", receiverKey);
        }
    }

    /**
     * 发送错误响应（使用ByteBuf缓冲区）
     */
    public void sendErrorResponse(ChannelHandlerContext ctx, String error) {
        ChatMessageProto.ChatMessage errorResponse = ChatMessageProto.ChatMessage.newBuilder()
                .setMsgType(5) // 撤回消息类型
                .setMsgContent("撤回失败:" + error)
                .build();
        ByteBuf buf = generateByteBuf(errorResponse);
        ctx.writeAndFlush(new BinaryWebSocketFrame(buf));
    }

    /**
     * 生成符合协议的ByteBuf缓冲区
     * 格式：魔数(8字节) + 消息类型(1字节) + 数据长度(4字节) + Protobuf数据
     */
    private ByteBuf generateByteBuf(ChatMessageProto.ChatMessage msg) {
        byte[] protoBytes = msg.toByteArray(); // 将Protobuf消息序列化为字节数组
        int dataLength = protoBytes.length;

        // 初始化缓冲区：容量 = 魔数(8) + 类型(1) + 长度(4) + 数据长度
        ByteBuf buf = Unpooled.buffer(8 + 1 + 4 + dataLength);

        // 按协议写入数据
        buf.writeLong(PROTOCOL_MAGIC_NUMBER); // 魔数（与TextMessageHandler保持一致）
        buf.writeByte((byte) msg.getMsgType()); // 消息类型（此处为5）
        buf.writeInt(dataLength); // 数据长度
        buf.writeBytes(protoBytes); // Protobuf数据

        return buf;
    }
}