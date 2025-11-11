package com.ruoyi.platform.chat.handler;

import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("30秒内未收到客户端[{}]心跳，关闭连接", ctx.channel().remoteAddress());
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ChatMessageProto.ChatMessage) {
            ChatMessageProto.ChatMessage message = (ChatMessageProto.ChatMessage) msg;
            if (message.getMsgType() == 6) {
                log.info("收到客户端心跳: 用户[{}/{}]", message.getFromType(), message.getFromId());
                // 可以回复心跳响应
                ChatMessageProto.ChatMessage ack = ChatMessageProto.ChatMessage.newBuilder()
                        .setMsgType(6)  // 心跳响应
                        .setMsgContent("pong")
                        .build();
                ctx.writeAndFlush(ack);
                return;  // 心跳消息不再向下传递
            }
        }
        ctx.fireChannelRead(msg);  // 非心跳消息继续传递
    }
}