package com.ruoyi.platform.chat.handler.impl;

import com.ruoyi.platform.chat.handler.MessageHandler;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import com.ruoyi.platform.chat.method.ChatOperateMethod;
import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
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
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;



@Component
@Slf4j
public class SystemMessageHandler implements MessageHandler {

    private static final long PROTOCOL_MAGIC_NUMBER = 0xCAFEBABEL;

    @Autowired
    private ChannelSessionManager channelSessionManager;

    @Autowired
    private IChatMessageService chatMessageService;

    @Autowired
    private IChatSessionService chatSessionService;

    @Autowired
    private ChatOperateMethod chatOperateMethod;

    @Override
    public long supportType() {
        return 4;
    }

    @Override
    public void handler(ChannelSessionManager channelSessionManager, ChannelHandlerContext ctx, ChatMessageProto.ChatMessage chatMessage) {

        // 1. 校验系统通知必要参数（如接收方ID、类型、通知内容）
        if (!validateParams(chatMessage)) {
            log.warn("系统通知参数无效: {}", chatMessage);
            return;
        }

        // 2. 持久化系统通知到数据库
        ChatMessage dbMsg = convertToDbMessage(chatMessage);
        ChatSession chatSession = chatOperateMethod.ensureSystemSessionExists(dbMsg);
        dbMsg.setSessionId(chatSession.getSessionId());
        chatSession.setLastMsgContent(dbMsg.getMsgContent());
        chatSessionService.updateChatSession(chatSession);
        chatMessageService.insertChatMessage(dbMsg);
        // 3. 推送通知给接收方
        boolean pushSuccess = pushToReceiver(channelSessionManager, chatMessage);
        if (!pushSuccess) {
            // 接收方离线，标记为离线消息（后续由OfflineMessagePullHandler拉取）
            dbMsg.setMsgStatus(1L);
            chatMessageService.updateChatMessage(dbMsg);
            // 更新会话未读计数
            Long sessionId = chatSessionService.selectChatSessionIdByFromTo(
                    (long) chatMessage.getFromType(),
                    chatMessage.getFromId(),
                    (long) chatMessage.getToType(),
                    chatMessage.getToId()
            );
            chatSessionService.increaseUnreadCount(sessionId);
        }else {
            dbMsg.setMsgStatus(1L);
            dbMsg.setDeliverTime(new Date());
            chatMessageService.updateChatMessage(dbMsg);
            chatSession.setLastMsgTime(dbMsg.getDeliverTime());
            chatSessionService.updateChatSession(chatSession);
            chatSessionService.increaseUnreadCount(chatSession.getSessionId());
        }
    }

    // 参数校验
    private boolean validateParams(ChatMessageProto.ChatMessage msg) {
        return msg.getToId() > 0 && msg.getToType() > 0 &&
                msg.getMsgContent() != null && !msg.getMsgContent().isEmpty();
    }

    // 转换为数据库实体
    private ChatMessage convertToDbMessage(ChatMessageProto.ChatMessage protoMsg) {
        ChatMessage dbMsg = new ChatMessage();
        dbMsg.setMessageId(protoMsg.getMessageId());
        dbMsg.setFromType(4L); // 发送方类型为系统（4）
        dbMsg.setFromId(0L); // 系统发送方ID可设为0
        dbMsg.setToType((long) protoMsg.getToType());
        dbMsg.setToId(protoMsg.getToId());
        dbMsg.setMsgType(4L); // 系统通知类型
        dbMsg.setMsgContent(protoMsg.getMsgContent());
        dbMsg.setMsgStatus(0L); // 初始状态：发送中
        dbMsg.setSendTime(new Date());
        dbMsg.setCreateTime(new Date());
        return dbMsg;
    }

    // 推送通知（复用现有推送逻辑，生成符合协议的ByteBuf）
    private boolean pushToReceiver(ChannelSessionManager sessionManager, ChatMessageProto.ChatMessage msg) {
        String receiverKey = msg.getToType() + ":" + msg.getToId();
        Channel receiverChannel = sessionManager.getChannel(receiverKey);

        if (Objects.isNull(receiverChannel)) {
            log.warn("接收方Channel不存在，Key：{}", receiverKey);
        } else if (!receiverChannel.isActive()) {
            log.warn("接收方Channel非活跃，Key：{}，Channel：{}", receiverKey, receiverChannel);
        } else if (!receiverChannel.isWritable()) {
            log.warn("接收方Channel不可写，Key：，写缓冲区剩余：");
        }
        if (Objects.isNull(receiverChannel) || !receiverChannel.isActive() || !receiverChannel.isWritable()) {
            return false;
        }

        try {
            ByteBuf buf = generateByteBuf(msg);
            receiverChannel.writeAndFlush(new BinaryWebSocketFrame(buf))
                    .addListener(future -> {
                        if (!future.isSuccess()) {
                            log.error("系统通知推送失败，消息ID: {}", msg.getMessageId(), future.cause());
                        }
                    });
            return true;
        } catch (Exception e) {
            log.error("系统通知推送异常", e);
            return false;
        }
    }

    // 生成协议格式的ByteBuf（与其他消息保持一致）
    private ByteBuf generateByteBuf(ChatMessageProto.ChatMessage msg) {
        byte[] protoBytes = msg.toByteArray();
        int dataLength = protoBytes.length;
        ByteBuf buf = Unpooled.buffer(8 + 1 + 4 + dataLength);
        buf.writeLong(PROTOCOL_MAGIC_NUMBER);
        buf.writeByte((byte) msg.getMsgType());
        buf.writeInt(dataLength);
        buf.writeBytes(protoBytes);
        return buf;
    }
}

