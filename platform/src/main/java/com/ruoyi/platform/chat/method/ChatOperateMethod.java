package com.ruoyi.platform.chat.method;


import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import com.ruoyi.platform.domain.ChatAttachment;
import com.ruoyi.platform.domain.ChatMessage;
import com.ruoyi.platform.domain.ChatSession;
import com.ruoyi.platform.service.IChatMessageService;
import com.ruoyi.platform.service.IChatSessionService;
import com.ruoyi.platform.service.impl.ChatAttachmentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
@Slf4j
public class ChatOperateMethod {

    @Autowired
    private IChatMessageService chatMessageService;

    @Autowired
    private IChatSessionService chatSessionService;

//    @Autowired
//    private RedisClient redisClient;


    /**
     * 保存文本消息到数据库
     * @param protoMsg Protobuf文本消息
     * @return 数据库实体
     */
    public ChatMessage saveTextMessage(ChatMessageProto.ChatMessage protoMsg) {
        ChatMessage dbMsg = new ChatMessage();
        dbMsg.setMessageId(protoMsg.getMessageId());
        dbMsg.setFromId(protoMsg.getFromId());
        dbMsg.setFromType((long) protoMsg.getFromType());
        dbMsg.setToId(protoMsg.getToId());
        dbMsg.setToType((long) protoMsg.getToType());
        dbMsg.setMsgType((long) protoMsg.getMsgType());
        dbMsg.setMsgContent(protoMsg.getMsgContent());
        dbMsg.setMsgStatus(0L); // 0-发送中
        dbMsg.setSendTime(new Date());
        dbMsg.setSessionId(protoMsg.getSessionId() != 0 ? protoMsg.getSessionId() : null);
        dbMsg.setCreateTime(new Date());
        dbMsg.setUpdateTime(new Date());
        dbMsg.setCreateBy(String.valueOf(protoMsg.getFromId()));
        dbMsg.setVersion(0);
        return dbMsg;
    }

    /**
     * 保存图片消息到数据库（关联MinIO图片URL）
     * @param attachment Protobuf图片消息
     * @param imageUrl 图片存储URL
     * @return 数据库实体
     */
    public ChatAttachment saveImageMessage(ChatMessageProto.Attachment attachment, String imageUrl, Long messageId) {
        ChatAttachment dbMsg = new ChatAttachment();
        dbMsg.setMessageId(messageId);
        dbMsg.setAttachmentUrl(imageUrl);
        dbMsg.setAttachmentType(1l);
        dbMsg.setFileSize(attachment.getFileSize());
        dbMsg.setIsValid(1l);
        dbMsg.setCreateTime(new Date());
        dbMsg.setUpdateTime(new Date());
        dbMsg.setFileName(attachment.getFileName());
        dbMsg.setFileExt(attachment.getFileExt());


        return dbMsg;
    }

    /**
     * 初始化聊天会话（首次消息触发）
     * @param chatMessage 聊天消息实体
     * @return 会话实体
     */
    public ChatSession initSession(ChatMessage chatMessage) {
        if (chatMessage == null) {
            throw new IllegalArgumentException("ChatMessage不能为空");
        }
        ChatSession chatSession = new ChatSession();
        chatSession.setFromId(chatMessage.getFromId());
        chatSession.setToId(chatMessage.getToId());
        chatSession.setFromType(chatMessage.getFromType());
        chatSession.setToType(chatMessage.getToType());
        chatSession.setCreateTime(new Date());
        chatSession.setUpdateTime(new Date());
        // 区分消息类型设置最后一条消息内容
        chatSession.setLastMsgContent(chatMessage.getMsgType() == 2 ? "[图片]" : chatMessage.getMsgContent());
        chatSession.setLastMsgType(chatMessage.getMsgType());
        chatSession.setLastMsgId(chatMessage.getMessageId());
        return chatSession;
    }

    /**
     * 更新聊天会话（最新消息触发）
     * @param chatMessage 最新聊天消息
     * @param chatSession 会话实体
     */
    public void updateSession(ChatMessage chatMessage, ChatSession chatSession) {
        if (chatMessage == null || chatSession == null) {
            log.error("更新会话失败：ChatMessage或ChatSession为空");
            return;
        }
        // 区分消息类型设置最后一条消息内容
        chatSession.setLastMsgContent(chatMessage.getMsgType() == 2 ? "[图片]" : chatMessage.getMsgContent());
        chatSession.setLastMsgId(chatMessage.getMessageId());
        chatSession.setLastMsgType(chatMessage.getMsgType());
        chatSession.setUpdateTime(new Date());
        chatSession.setLastMsgTime(new Date());
        // 更新消息状态为“已送达”
        chatMessage.setMsgStatus(chatMessage.getMsgStatus()); // 1-已送达 3-离线消息
        chatMessage.setDeliverTime(new Date());
        chatMessageService.updateChatMessage(chatMessage);
        // 更新会话
        chatSession.setUnreadCount(chatSession.getUnreadCount() + 1);
        chatSessionService.updateChatSession(chatSession);
        log.info("当前消息的状态为:{}",chatMessage.getMsgStatus());
    }

    /**
     * 初始化附件消息（用于文件分片传输场景）
     * @param messageId 消息ID
     * @param attachmentMeta 附件元数据
     * @return 聊天消息实体
     */
    public ChatMessage initAttachmentMessage(Long messageId, ChatAttachment attachmentMeta) {
        if (attachmentMeta == null || messageId == null) {
            throw new IllegalArgumentException("AttachmentMeta或messageId不能为空");
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageId(messageId);
        chatMessage.setMsgType(2L); // 图片/文件类型（2）
        chatMessage.setMsgContent(attachmentMeta.getFileName());
        chatMessage.setMsgStatus(0L); // 0-未发送
        chatMessage.setSendTime(new Date());
        return chatMessage;
    }

    /**
     * 推送消息状态更新（给发送方）
     * @param originalMsg 原始Protobuf消息
     * @param status 状态码（0-发送中，1-已送达，4-发送失败）
     */
    public ChatMessageProto.ChatMessage buildStatusUpdate(ChatMessageProto.ChatMessage originalMsg, int status) {
        return ChatMessageProto.ChatMessage.newBuilder(originalMsg)
                .setMsgStatus(status)
                .setUpdateTime(String.valueOf(System.currentTimeMillis())) // 时间戳（毫秒）
                .build();
    }

    /**
     * 校验会话是否存在，不存在则初始化
     * @param chatMessage 聊天消息
     * @return 会话实体
     */
    public ChatSession ensureSessionExists(ChatMessage chatMessage) {
        ChatSession session;
        Long sessionId = chatSessionService.selectChatSessionIdByFromTo(
                chatMessage.getFromId(), chatMessage.getToId(),
                chatMessage.getFromType(), chatMessage.getToType()
        );

        if (sessionId == null) {
            // 初始化并插入新会话
            session = initSession(chatMessage);
            chatSessionService.insertChatSession(session);
            // 关键修复：将新生成的会话ID赋值给sessionId
            sessionId = session.getSessionId();
            // 更新消息关联的会话ID
            chatMessage.setSessionId(sessionId);
            chatMessageService.updateChatMessage(chatMessage);
            log.info("初始化新会话，会话ID: {}", sessionId);
        }

        // 此时sessionId已确保不为null（要么原本存在，要么新生成）
        session = chatSessionService.selectChatSessionBySessionId(sessionId);
        return session;
    }

}