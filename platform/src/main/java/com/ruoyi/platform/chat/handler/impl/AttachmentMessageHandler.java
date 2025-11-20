package com.ruoyi.platform.chat.handler.impl;


import com.ruoyi.common.utils.file.MinioFileUtils;
import com.ruoyi.platform.chat.handler.MessageHandler;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import com.ruoyi.platform.chat.method.ChatOperateMethod;
import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import com.ruoyi.platform.chat.utils.ChatCacheUtils;
import com.ruoyi.platform.domain.ChatAttachment;
import com.ruoyi.platform.domain.ChatMessage;
import com.ruoyi.platform.domain.ChatSession;
import com.ruoyi.platform.service.IChatAttachmentService;
import com.ruoyi.platform.service.IChatMessageService;
import com.ruoyi.platform.service.IChatSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 附件消息处理器（基于现有Minio工具类实现图片传输）
 */
@Component
@Slf4j
public class AttachmentMessageHandler implements MessageHandler {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private ChatOperateMethod chatOperateMethod;
    @Autowired
    private IChatMessageService chatMessageService;
    @Autowired
    private IChatSessionService chatSessionService;
    @Autowired
    private IChatAttachmentService chatAttachmentService; // 需确保该Service已实现
    @Autowired
    private MinioFileUtils minioFileUtils; // 复用你的MinIO工具类
    @Autowired
    private ObjectMapper objectMapper; // 用于JSON序列化/反序列化


    // 临时缓存分片数据（key：messageId，value：合并后的字节数组）
    private final Map<Long, byte[]> chunkCache = new HashMap<>();
    @Autowired
    private ChatCacheUtils chatCacheUtils;

    @Override
    public long supportType() {
        return 2L; // 对应BinaryMessage.type=2（附件消息）
    }

    @Override
    public void handler(ChannelSessionManager channelSessionManager, ChannelHandlerContext ctx, ChatMessageProto.ChatMessage chatMessage) {
        threadPoolTaskExecutor.execute(() -> {
            try {
                Integer length = Integer.valueOf(chatMessage.getAttachmentsCount());

                // 1. 反序列化Protobuf消息体（增加空值判断）
                if (length == null) {
                    throw new IllegalArgumentException("附件不能为空");
                }

                //2. session则初始化
                Long otherSessionId = chatSessionService.selectChatSessionIdByFromTo((long) chatMessage.getToType(),chatMessage.getToId(),(long) chatMessage.getFromType(),chatMessage.getFromId());
                Long sessionId = chatSessionService.selectChatSessionIdByFromTo((long) chatMessage.getFromType(), chatMessage.getFromId(), (long) chatMessage.getToType(), chatMessage.getToId());
                ChatMessage dbMsg = chatOperateMethod.saveTextMessage(chatMessage);

                ChatSession session;
                ChatSession otherSession;
                if (sessionId == null || otherSessionId == null)
                {
                    session = chatOperateMethod.ensureSessionExists(dbMsg) ;
                    dbMsg.setSessionId(session.getSessionId());
                }
                session = chatSessionService.selectChatSessionBySessionId(sessionId);
                otherSession = chatSessionService.selectChatSessionBySessionId(otherSessionId);
                dbMsg.setSessionId(sessionId);
                chatMessageService.insertChatMessage(dbMsg);
                Long messageId = dbMsg.getMessageId();
                for (ChatMessageProto.Attachment attachment : chatMessage.getAttachmentsList()) {
                    if (attachment.getFileSize() <= 0 ||
                            attachment.getFileExt().isEmpty() ||
                            attachment.getImageData().isEmpty()) {
                        sendErrorResponse(ctx, "图片信息不完整");
                        return;
                    }

                    byte[] imageDate = attachment.getImageData().toByteArray();

                    String url = minioFileUtils.upload(imageDate, attachment.getFileName(), chatMessage.getFromId());
                    if (url == null) {
                        log.error("minio上传失败");
                        sendErrorResponse(ctx,"图片上传失败");
                        return;
                    }
                    chatAttachmentService.insertChatAttachment(chatOperateMethod.saveImageMessage(attachment, url, messageId));
                    chatOperateMethod.updateSession(dbMsg,session);
                    chatOperateMethod.updateSessionWithUnreadCount(dbMsg,otherSession);
                    chatCacheUtils.cacheChatMessage(dbMsg);
                    forwardMessageToRecipient(channelSessionManager,chatMessage);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * 转发消息给接收方
     */
    private void forwardMessageToRecipient(ChannelSessionManager channelSessionManager, ChatMessageProto.ChatMessage chatMessage) {
        // 获取接收方的Channel
        String key = chatMessage.getToType() + ":" + chatMessage.getToId();
        var recipientChannel = channelSessionManager.getChannel(key);
        if (recipientChannel != null && recipientChannel.isActive()) {
            recipientChannel.writeAndFlush(chatMessage);
            log.info("图片消息已转发: from={}, to={}", chatMessage.getFromId(), chatMessage.getToId());
        } else {
            log.warn("接收方不在线: toType={}, toId={}", chatMessage.getToType(), chatMessage.getToId());
            // 可以在这里实现消息离线存储逻辑
        }
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(ChannelHandlerContext ctx, String errorMsg) {
        ChatMessageProto.ChatMessage errorResponse = ChatMessageProto.ChatMessage.newBuilder()
                .setMsgType(4) // 4-系统通知
                .setMsgContent(errorMsg)
                .setCreateTime(new Date().toString())
                .build();
        ctx.writeAndFlush(errorResponse);
    }
}







