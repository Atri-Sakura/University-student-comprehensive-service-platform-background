package com.ruoyi.platform.chat.handler;

import com.ruoyi.platform.chat.factory.MessageHandlerFactory;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import com.ruoyi.platform.chat.protobuf.ChatMessageProto;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@ChannelHandler.Sharable
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<ChatMessageProto.ChatMessage> {

    @Autowired
    private ChannelSessionManager channelSessionManager;

    @Autowired
    private MessageHandlerFactory messageHandlerFactory;



    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 发送连接成功消息
            ChatMessageProto.ChatMessage chatMessage = ChatMessageProto.ChatMessage.newBuilder()
                    .setMsgType(4)
                    .setMsgContent("连接已成功建立，请发送注册消息")
                    .setCreateTime(String.valueOf(new Date()))
                    .build();
            ctx.writeAndFlush(chatMessage);
            log.info("WebSocket连接已建立，发送欢迎消息");
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessageProto.ChatMessage chatMessage) throws Exception {
        try {
            MessageHandler messageHandler = messageHandlerFactory.getMessageHandler(Long.valueOf(chatMessage.getMsgType()));
            log.info("收到消息: {}", chatMessage);

            // 验证必需字段
            if (messageHandler == null && chatMessage.getMsgType() != 0 && chatMessage.getMsgType() != 4 && chatMessage.getMsgType() != 6) {
                sendErrorResponse(ctx, "不支持的消息类型: " + chatMessage.getMsgType());
                return;
            }
            // 验证基本字段
            if (Long.valueOf(chatMessage.getFromId()) == null) {
                sendErrorResponse(ctx, "发送方ID不能为空");
                return;
            }

            // 根据消息类型处理
            switch (chatMessage.getMsgType()) {
                case 0: // 注册消息
                    handleRegisterMessage(ctx, chatMessage);
                    break;
                case 1: // 文本消息
                    messageHandler.handler(channelSessionManager, ctx, chatMessage);
                    break;
                case 2:
                    messageHandler.handler(channelSessionManager, ctx, chatMessage);
                    break;
                case 3:
                    messageHandler.handler(channelSessionManager, ctx, chatMessage);
                    break;
                case 4:
                    messageHandler.handler(channelSessionManager, ctx, chatMessage);
                    break;
                case 5:
                    messageHandler.handler(channelSessionManager, ctx, chatMessage);
                    break;
//                case 6:
//                    log.debug("收到心跳消息，用户为[{}:{}]",chatMessage.getFromType(),chatMessage.getFromId());
//                    break;
                default:
                    sendErrorResponse(ctx, "不支持的消息类型: " + chatMessage.getMsgType());
            }

        } catch (Exception e) {
            log.error("处理消息异常", e);
            sendErrorResponse(ctx, "处理消息异常: " + e.getMessage());
        }
    }

    /**
     * 处理注册消息
     */
    private void handleRegisterMessage(ChannelHandlerContext ctx, ChatMessageProto.ChatMessage chatMessage) {
        long fromId = chatMessage.getFromId();
        long fromType = chatMessage.getFromType();

        // 验证注册参数
        if (fromId == 0 || fromType == 0) {
            sendErrorResponse(ctx, "注册失败：FromId 和 FromType 不能为空");
            return;
        }

        // 注册Channel
        channelSessionManager.registerChannel(fromId, fromType, ctx.channel());
        log.info("用户注册成功: {}:{}", fromType, fromId);

        // 发送注册成功响应
        ChatMessageProto.ChatMessage successMsg = ChatMessageProto.ChatMessage.newBuilder()
                .setMsgContent("注册成功")
                .setMsgType(4)
                .build();
        ctx.writeAndFlush(successMsg);
    }


    /**
     * 发送错误响应
     */
    private void sendErrorResponse(ChannelHandlerContext ctx, String errorMsg) {
        try {
            ChatMessageProto.ChatMessage errorResponse = ChatMessageProto.ChatMessage
                    .newBuilder()
                    .setMsgContent("错误信息")
                    .setMsgType(4)
                    .build();
            ctx.writeAndFlush(errorResponse);
            log.error("发送错误响应: {}", errorMsg);
        } catch (Exception e) {
            log.error("发送错误响应失败", e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("通道异常", cause);

        if (cause instanceof java.io.IOException && "Connection reset by peer".equals(cause.getMessage())) {
            log.warn("客户端主动断开连接");
        } else {
            sendErrorResponse(ctx, "服务端异常: " + cause.getMessage());
        }

        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 通道断开时清理注册信息
        channelSessionManager.unregisterChannel(ctx.channel());
        log.info("通道已断开: {}", ctx.channel().id());
        super.channelInactive(ctx);
    }
}