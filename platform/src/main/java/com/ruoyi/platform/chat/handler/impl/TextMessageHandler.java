package com.ruoyi.platform.chat.handler.impl;

import com.ruoyi.common.core.domain.entity.ChatMessage;
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
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

@Component
@Slf4j
public class TextMessageHandler implements MessageHandler {

    @Autowired
    private ExecutorService messageExecutor;

    @Autowired
    private ChatOperateMethod chatOperateMethod;

    @Autowired
    private IChatSessionService chatSessionService;

    @Autowired
    private IChatMessageService chatMessageService;

    @Override
    public long supportType() {
        return 1L; // 支持文本消息类型（对应msg_type=1）
    }

    @Override
    public void handler(ChannelSessionManager channelSessionManager, ChannelHandlerContext ctx, ChatMessageProto.ChatMessage chatMessage) {
        messageExecutor.execute(() -> {
            try {
                if (!validateParams(chatMessage)) {
                    return;
                }
                Long sessionId = chatSessionService.selectChatSessionIdByFromTo((long)chatMessage.getFromType(),chatMessage.getFromId(),(long)chatMessage.getToType(),chatMessage.getToId());
                ChatMessage dbMsg = chatOperateMethod.saveTextMessage(chatMessage);
                ChatSession session;
                session = chatSessionService.selectChatSessionBySessionId(sessionId);
                if(sessionId == null){
                     session = chatOperateMethod.ensureSessionExists(dbMsg);
                    dbMsg.setSessionId(session.getSessionId());
                }

                dbMsg.setSessionId(sessionId);
                int saveSuccess = chatMessageService.insertChatMessage(dbMsg);
                if (saveSuccess < 1) {
                    log.error("文本消息存储失败，消息ID: {}", chatMessage.getMessageId());
                    pushStatusToSender(channelSessionManager, chatMessage, 4);
                    return;
                }


                boolean pushSuccess = pushToReceiver(channelSessionManager, chatMessage);
                if (!pushSuccess) {
                    log.warn("接收方未在线，消息ID: {}", chatMessage.getMessageId());
                }

                chatOperateMethod.updateSession(dbMsg, session);
                pushStatusToSender(channelSessionManager, chatMessage, 1);

            } catch (Exception e) {
                log.error("文本消息处理异常，消息ID: {}", chatMessage.getMessageId(), e);
                pushStatusToSender(channelSessionManager, chatMessage, 4);
            }
        });
    }

    private boolean validateParams(ChatMessageProto.ChatMessage chatMessage) {
        if (chatMessage.getMessageId() <= 0) {
            log.warn("消息ID无效，忽略处理");
            return false;
        }
        if (chatMessage.getFromId() <= 0 || chatMessage.getToId() <= 0) {
            log.warn("发送/接收方ID无效，消息ID: {}", chatMessage.getMessageId());
            return false;
        }
        if (chatMessage.getMsgContent() == null || chatMessage.getMsgContent().trim().isEmpty()) {
            log.warn("文本消息内容为空，消息ID: {}", chatMessage.getMessageId());
            return false;
        }
        return true;
    }

    private boolean pushToReceiver(ChannelSessionManager sessionManager, ChatMessageProto.ChatMessage chatMessage) {
        String key = chatMessage.getToType() + ":" + chatMessage.getToId();
        Channel receiverChannel = sessionManager.getChannel(key);
        if (Objects.isNull(receiverChannel) || !receiverChannel.isActive()) {
            return false;
        }

        try {
            ByteBuf buf = generateByteBuf(chatMessage);
            receiverChannel.writeAndFlush(new BinaryWebSocketFrame(buf));
            log.debug("文本消息推送成功，消息ID: {}, 接收方ID: {}", chatMessage.getMessageId(), chatMessage.getToId());
            return true;
        } catch (Exception e) {
            log.error("推送消息给接收方失败，消息ID: {}", chatMessage.getMessageId(), e);
            return false;
        }
    }

    private void pushStatusToSender(ChannelSessionManager sessionManager, ChatMessageProto.ChatMessage originalMsg, int status) {
        ChatMessageProto.ChatMessage statusMsg = buildStatusUpdate(originalMsg, status);
        String key = originalMsg.getFromType() + ":" + originalMsg.getFromId();
        Channel senderChannel = sessionManager.getChannel(key);
        if (Objects.isNull(senderChannel) || !senderChannel.isActive()) {
            log.warn("发送方通道已关闭，无法推送状态，消息ID: {}", originalMsg.getMessageId());
            return;
        }

        try {
            ByteBuf buf = generateByteBuf(statusMsg);
            senderChannel.writeAndFlush(new BinaryWebSocketFrame(buf));
            log.debug("消息状态推送成功，消息ID: {}, 状态: {}", originalMsg.getMessageId(), getStatusDesc(status));
        } catch (Exception e) {
            log.error("推送状态给发送方失败，消息ID: {}", originalMsg.getMessageId(), e);
        }
    }

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

    private ChatMessageProto.ChatMessage buildStatusUpdate(ChatMessageProto.ChatMessage originalMsg, int status) {
        return ChatMessageProto.ChatMessage.newBuilder(originalMsg)
                .setMsgStatus(status)
                .setUpdateTime(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    private String getStatusDesc(int status) {
        return switch (status) {
            case 0 -> "发送中";
            case 1 -> "已送达";
            case 2 -> "已读";
            case 3 -> "已撤回";
            case 4 -> "发送失败";
            default -> "未知状态";
        };
    }
}