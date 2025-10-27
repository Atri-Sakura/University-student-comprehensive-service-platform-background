package com.ruoyi.platform.chat.handler;

import com.ruoyi.common.core.domain.entity.ChatMessage;
import com.ruoyi.platform.chat.factory.MessageHandlerFactory;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ChannelSessionManager channelSessionManager;

    @Autowired
    private MessageHandlerFactory messageHandlerFactory;

    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            ChatMessage response = new ChatMessage();
            response.setMsgType(-1L);
            response.setMsgContent("连接成功，请发送注册消息");
            ctx.writeAndFlush(response);
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.info("客户端建立连接{}",remoteAddress);
        CHANNEL_GROUP.add(ctx.channel());

    }

    /**
     * 连接后的发送逻辑
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof ChatMessage message){
            log.info("接收客户端消息: 客户端={}, 消息类型={}, 发送方={}:{}, 接收方={}:{}",
                    ctx.channel().remoteAddress().toString(),
                    message.getMsgType(),
                    message.getFromType(),
                    message.getFromId(),
                    message.getToType(),
                    message.getToId());
            if(message.getMsgType() == 0){
                if(message.getFromId() == null){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setMsgType(-1L);
                    chatMessage.setMsgContent("不存在用户ID，注册失败");
                    ctx.writeAndFlush(chatMessage);
                    return;
                }
                channelSessionManager.registerChannel(message.getFromId(),message.getFromType(),ctx.channel());
                log.info("用户注册成功{}:{}",message.getFromType(),message.getFromId());
                ChatMessage response = new ChatMessage();
                response.setMsgType(-1L);
                response.setMsgContent("注册成功");
                ctx.writeAndFlush(response);

            }
            MessageHandler messageHandler = messageHandlerFactory.getMessageHandler(message.getMsgType());
            if(messageHandler != null){
                messageHandler.handler(channelSessionManager,ctx,message);
            }else{
                log.debug("未支持的消息类型");
            }

        }
    }

    /**
     * 取消连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelSessionManager.removeChannel(ctx.channel());
        CHANNEL_GROUP.remove(ctx.channel());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("caught exception", cause);
        if (!(cause instanceof IOException && "Connection reset by peer".equals(cause.getMessage()))) {
            ChatMessage errorMsg = new ChatMessage();
            errorMsg.setMsgType(-1L);
            errorMsg.setMsgContent("服务端处理异常：" + cause.getMessage());
            ctx.writeAndFlush(errorMsg);
        } else {
            // 严重错误才关闭连接
            ctx.close();
        }
    }}
