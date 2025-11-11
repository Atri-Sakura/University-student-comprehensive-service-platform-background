package com.ruoyi.platform.chat.utils;

import com.ruoyi.platform.chat.codec.*;
import com.ruoyi.platform.chat.handler.ChatHandler;

import com.ruoyi.platform.chat.handler.HeartBeatServerHandler;
import com.ruoyi.platform.chat.ssl.SslServerContextFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLException;

@Component
@Slf4j
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {


    @Autowired
    private final ChatHandler chatHandler;

    @Autowired
    private SslServerContextFactory sslServerContextFactory;

    @Autowired
    private HeartBeatServerHandler heartBeatServerHandler;


    private boolean sslEnabled = true;

    private SslContext sslContext;



    public NettyServerChannelInitializer(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @PostConstruct
    public void init() {
        // 预初始化SSL上下文
        if (sslEnabled && sslServerContextFactory.isSslAvailable()) {
            try {
                this.sslContext = sslServerContextFactory.createSslContext();
                log.info("SSL预初始化成功，SSL已启用");
            } catch (Exception e) {
                log.warn("SSL预初始化失败，将使用非安全连接", e);
                this.sslEnabled = false;
            }
        } else {
            log.info("SSL未启用或证书不可用");
            this.sslEnabled = false;
        }
    }



    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

//        if (sslEnabled && sslContext != null) {
//            SslHandler sslHandler = sslContext.newHandler(socketChannel.alloc());
//            socketChannel.pipeline().addLast("ssl", sslHandler);
//            log.debug("添加SSL处理器到管道");
//        }
        socketChannel.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(64*1024))
//                .addLast(new WebSocketServerCompressionHandler())
                .addLast(new IdleStateHandler(30, 0, 0))
                .addLast(new WebSocketServerProtocolHandler("/ws", "", true, 64*1024))
//                .addLast(new BinaryMessagedDecoder())
//                .addLast(new WebSocketFrameToBinaryMessageDecoder())
//                .addLast(new BinaryMessageEncoder())
                .addLast(new WebSocketToProtobufDecoder())
                .addLast(heartBeatServerHandler)
                .addLast(new ProtobufToWebSocketEncoder())
                .addLast(chatHandler);
//                .addLast(binaryChatHandler);
//                .addLast(webSocketMessageDecoder)
//                .addLast(webSocketMessageEncoder)
//                .addLast(nettyServerHandler);

        log.debug("通道管道初始化完成，SSL状态: {}", sslEnabled ? "启用" : "禁用");

    }
}
