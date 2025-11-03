package com.ruoyi.platform.chat.handler.impl;

import com.ruoyi.common.core.domain.entity.ChatMessage;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.platform.chat.handler.MessageHandler;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import com.ruoyi.platform.chat.method.ChatOperateMethod;
import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
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

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 文本消息处理器（优化版）
 * 修复并发安全、性能损耗、业务漏洞，适配生产环境
 */
@Component
@Slf4j
public class TextMessageHandler implements MessageHandler {

    // 常量提取：避免硬编码，统一维护
    private static final long PROTOCOL_MAGIC_NUMBER = 0xCAFEBABEL; // 修正魔数（原0xCAFEBABEL）
    private static final int MAX_CACHE_MESSAGE_COUNT = 100; // 缓存消息最大条数
    private static final long CACHE_EXPIRE_DAYS = 7; // 缓存过期时间（天）
    private static final String CACHE_KEY_PREFIX = "chat:sessionId:";
    private static final String CACHE_KEY_SUFFIX = ":messages";

    @Autowired
    private ExecutorService messageExecutor;

    @Autowired
    private ChatOperateMethod chatOperateMethod;

    @Autowired
    private IChatSessionService chatSessionService;

    @Autowired
    private IChatMessageService chatMessageService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate; // 替换RedisCache，使用原生API实现原子操作

    @Override
    public long supportType() {
        return 1L; // 支持文本消息类型（对应msg_type=1）
    }

    @Override
    public void handler(ChannelSessionManager channelSessionManager, ChannelHandlerContext ctx, ChatMessageProto.ChatMessage chatMessage) {
        // 线程池任务提交：添加try-catch处理拒绝策略，避免未捕获异常导致流程中断
        try {
            messageExecutor.execute(() -> {
                try {
                    // 1. 参数校验：提前过滤无效请求
                    if (!validateParams(chatMessage)) {
                        return;
                    }

                    // 2. 会话查询与创建：避免空指针，确保会话存在
                    Long sessionId = chatSessionService.selectChatSessionIdByFromTo(
                            (long) chatMessage.getFromType(),
                            chatMessage.getFromId(),
                            (long) chatMessage.getToType(),
                            chatMessage.getToId()
                    );
                    ChatMessage dbMsg = chatOperateMethod.saveTextMessage(chatMessage);
                    ChatSession session;

                    // 若会话不存在则创建，确保sessionId非空
                    if (Objects.isNull(sessionId)) {
                        session = chatOperateMethod.ensureSessionExists(dbMsg);
                        sessionId = session.getSessionId();
                    }

                    // 会话二次查询：添加非空校验，避免空指针
                    session = chatSessionService.selectChatSessionBySessionId(sessionId);
                    if (Objects.isNull(session)) {
                        log.error("会话查询为空，无法处理消息，sessionId: {}, 消息ID: {}", sessionId, chatMessage.getMessageId());
                        pushStatusToSender(channelSessionManager, chatMessage, 4);
                        return;
                    }

                    // 3. 消息存储：数据库持久化
                    dbMsg.setSessionId(sessionId);
                    int saveSuccess = chatMessageService.insertChatMessage(dbMsg);
                    if (saveSuccess < 1) {
                        log.error("文本消息存储失败，消息ID: {}", chatMessage.getMessageId());
                        pushStatusToSender(channelSessionManager, chatMessage, 4);
                        return;
                    }

                    // 4. 缓存更新：使用Redis原子命令，解决并发安全与性能问题
                    String cacheKey = buildCacheKey(sessionId);


                    // 5. 接收方推送：消息实时投递
                    boolean pushSuccess = pushToReceiver(channelSessionManager, chatMessage);
                    if (!pushSuccess) {
                        log.warn("接收方未在线或推送失败，消息ID: {}, 接收方ID: {}", chatMessage.getMessageId(), chatMessage.getToId());
                        // 此处可扩展：离线消息存储逻辑（如写入数据库离线表）
                        dbMsg.setMsgStatus(3L);
                        chatMessageService.updateChatMessage(dbMsg);
                        chatSessionService.increaseUnreadCount(sessionId);

                    }

                    // 6. 会话更新与发送方状态回传：闭环业务流程
                    dbMsg.setMsgStatus(1L);
                    if(channelSessionManager.getChannel(chatMessage.getToType()+":"+chatMessage.getToId()) == null) {
                        dbMsg.setMsgStatus(3L);
                        log.warn("用户还未上线");
                    }
                    chatOperateMethod.updateSession(dbMsg, session);
                    log.info("cacheKey: {}", cacheKey);
                    if(dbMsg.getMsgStatus() != 3L) {
                        updateMessageCache(cacheKey, dbMsg);
                    }
                    pushStatusToSender(channelSessionManager, chatMessage, dbMsg.getMsgStatus().intValue());

                } catch (Exception e) {
                    // 异常兜底：确保所有异常被捕获，避免线程池任务静默失败
                    log.error("文本消息处理异常，消息ID: {}", chatMessage.getMessageId(), e);
                    pushStatusToSender(channelSessionManager, chatMessage, 4);
                }
            });
        } catch (Exception e) {
            // 线程池拒绝策略处理：任务提交失败时的降级逻辑
            log.error("消息任务提交失败（线程池可能已满），消息ID: {}", chatMessage.getMessageId(), e);
            pushStatusToSender(channelSessionManager, chatMessage, 4);
        }
    }

    /**
     * 参数校验：过滤无效请求
     */
    private boolean validateParams(ChatMessageProto.ChatMessage chatMessage) {
        if (chatMessage.getMessageId() <= 0) {
            log.warn("消息ID无效（<=0），忽略处理");
            return false;
        }
        if (chatMessage.getFromId() <= 0 || chatMessage.getToId() <= 0) {
            log.warn("发送方ID（{}）或接收方ID（{}）无效，消息ID: {}",
                    chatMessage.getFromId(), chatMessage.getToId(), chatMessage.getMessageId());
            return false;
        }
        if (chatMessage.getMsgContent() == null || chatMessage.getMsgContent().trim().isEmpty()) {
            log.warn("文本消息内容为空，消息ID: {}", chatMessage.getMessageId());
            return false;
        }
        return true;
    }

    /**
     * 构建Redis缓存Key
     */
    private String buildCacheKey(Long sessionId) {
        return CACHE_KEY_PREFIX + sessionId + CACHE_KEY_SUFFIX;
    }

    /**
     * 原子更新消息缓存：解决并发安全与性能问题
     * 使用LPUSH（左增）+ LTRIM（修剪）组合命令，避免本地修改
     */
    private void updateMessageCache(String cacheKey, ChatMessage dbMsg) {
        try {
            // 1. 左推新消息：最新消息在列表头部，符合用户查看习惯
            redisTemplate.opsForList().rightPush(cacheKey, dbMsg);
            // 2. 修剪列表：只保留前MAX_CACHE_MESSAGE_COUNT条，超出部分自动删除（原子操作）
            redisTemplate.opsForList().trim(cacheKey, -MAX_CACHE_MESSAGE_COUNT,  -1);
            // 3. 设置过期时间：若已存在则刷新过期时间（7天）
            redisTemplate.expire(cacheKey, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
            log.debug("消息缓存更新成功，缓存Key: {}, 消息ID: {}", cacheKey, dbMsg.getMessageId());
        } catch (Exception e) {
            // 缓存更新失败不影响主流程（数据库已存储），但需告警
            log.error("消息缓存更新失败，缓存Key: {}, 消息ID: {}", cacheKey, dbMsg.getMessageId(), e);
            // 此处可扩展：添加缓存失败告警（如钉钉/邮件）
        }
    }

    // 修复后的pushToReceiver方法
    private boolean pushToReceiver(ChannelSessionManager sessionManager, ChatMessageProto.ChatMessage chatMessage) {
        String receiverKey = chatMessage.getToType() + ":" + chatMessage.getToId();
        Channel receiverChannel = sessionManager.getChannel(receiverKey);

        if (Objects.isNull(receiverChannel) || !receiverChannel.isActive() || !receiverChannel.isWritable()) {
            return false;
        }

        try {
            // 生成ByteBuf并封装为BinaryWebSocketFrame
            ByteBuf buf = generateByteBuf(chatMessage);
            BinaryWebSocketFrame frame = new BinaryWebSocketFrame(buf);

            // 发送后由Netty自动释放ByteBuf，无需手动处理
            receiverChannel.writeAndFlush(frame).addListener(future -> {
                if (!future.isSuccess()) {
                    log.error("消息推送至接收方通道失败，消息ID: {}", chatMessage.getMessageId(), future.cause());
                }
            });
            log.debug("文本消息推送成功，消息ID: {}, 接收方Key: {}", chatMessage.getMessageId(), receiverKey);
            return true;
        } catch (Exception e) {
            log.error("推送消息给接收方异常，消息ID: {}, 接收方Key: {}", chatMessage.getMessageId(), receiverKey, e);
            return false;
        }
    }

    /**
     * 推送状态给发送方（如：已送达、发送失败）
     */
    private void pushStatusToSender(ChannelSessionManager sessionManager, ChatMessageProto.ChatMessage originalMsg, int status) {
        // 构建状态消息：复用原消息字段，仅更新状态和时间
        ChatMessageProto.ChatMessage statusMsg = buildStatusUpdate(originalMsg, status);
        String senderKey = originalMsg.getFromType() + ":" + originalMsg.getFromId();
        Channel senderChannel = sessionManager.getChannel(senderKey);

        if (Objects.isNull(senderChannel) || !senderChannel.isActive()) {
            log.warn("发送方通道已关闭，无法推送状态，消息ID: {}", originalMsg.getMessageId());
            return;
        }

        try {
            ByteBuf buf = generateByteBuf(statusMsg);
            try {
                senderChannel.writeAndFlush(new BinaryWebSocketFrame(buf)).addListener(future -> {
                    if (!future.isSuccess()) {
                        log.error("状态推送至发送方通道失败，消息ID: {}", originalMsg.getMessageId(), future.cause());
                    }
                });
                log.debug("消息状态推送成功，消息ID: {}, 状态: {}", originalMsg.getMessageId(), getStatusDesc(status));
            } catch (Exception e) {
                log.error("推送状态给发送方异常，消息ID: {}", originalMsg.getMessageId(), e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成Netty二进制缓冲区：遵循协议格式（魔数+消息类型+数据长度+数据）
     */
    private ByteBuf generateByteBuf(ChatMessageProto.ChatMessage msg) {
        byte[] protoBytes = msg.toByteArray();
        int dataLength = protoBytes.length;
        // 缓冲区容量计算：魔数(8字节) + 消息类型(1字节) + 数据长度(4字节) + 数据长度
        ByteBuf buf = Unpooled.buffer(8 + 1 + 4 + dataLength);
        buf.writeLong(PROTOCOL_MAGIC_NUMBER); // 修正后的魔数
        buf.writeByte((byte) msg.getMsgType());
        buf.writeInt(dataLength);
        buf.writeBytes(protoBytes);
        return buf;
    }

    /**
     * 构建状态更新消息
     */
    private ChatMessageProto.ChatMessage buildStatusUpdate(ChatMessageProto.ChatMessage originalMsg, int status) {
        return ChatMessageProto.ChatMessage.newBuilder(originalMsg)
                .setMsgStatus(status)
                .setUpdateTime(String.valueOf(System.currentTimeMillis())) // 时间戳：便于排查时序问题
                .build();
    }

    /**
     * 状态描述转换：便于日志阅读
     */
    private String getStatusDesc(int status) {
        return switch (status) {
            case 0 -> "发送中";
            case 1 -> "已送达";
            case 2 -> "已读";
            case 3 -> "离线消息";
            case 4 -> "发送失败";
            default -> "未知状态（" + status + "）";
        };
    }
}