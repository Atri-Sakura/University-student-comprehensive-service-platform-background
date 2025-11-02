package com.ruoyi.platform.chat.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class ClientWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private final WebSocketClientHandshaker handshaker;
    private final CountDownLatch handshakeLatch;

    public ClientWebSocketHandler(WebSocketClientHandshaker handshaker, CountDownLatch handshakeLatch) {
        this.handshaker = handshaker;
        this.handshakeLatch = handshakeLatch;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端通道激活，开始WebSocket握手...");
        handshaker.handshake(ctx.channel()); // 发送握手请求
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();

        // 处理未完成的握手
        if (!handshaker.isHandshakeComplete()) {
            log.info("收到握手响应，正在完成握手...");
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            log.info("WebSocket握手成功！");
            handshakeLatch.countDown(); // 通知主线程握手完成
            return;
        }

        // 处理已握手后的消息
        if (msg instanceof TextWebSocketFrame frame) {
            String response = frame.text();
            log.info("收到服务器文本消息: {}", response);
        } else if (msg instanceof CloseWebSocketFrame) {
            log.info("收到服务器关闭连接指令");
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("WebSocket客户端异常（可能导致握手失败）", cause);
        // 关键修复：无论握手是否完成，都释放锁存器，避免主线程一直等待
        if (!handshaker.isHandshakeComplete()) {
            handshakeLatch.countDown();
        }
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.warn("WebSocket连接已关闭");
        // 连接意外关闭时释放锁存器
        if (!handshaker.isHandshakeComplete()) {
            handshakeLatch.countDown();
        }
    }
}
