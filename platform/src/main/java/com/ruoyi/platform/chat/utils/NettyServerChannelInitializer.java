package com.ruoyi.platform.chat.utils;

import com.ruoyi.platform.chat.codec.WebSocketMessageDecoder;
import com.ruoyi.platform.chat.codec.WebSocketMessageEncoder;
import com.ruoyi.platform.chat.handler.MessageHandler;
import com.ruoyi.platform.chat.handler.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private WebSocketMessageEncoder webSocketMessageEncoder;

    @Autowired
    private WebSocketMessageDecoder webSocketMessageDecoder;

    @Autowired
    private NettyServerHandler nettyServerHandler;


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(64*1024))
                .addLast(new WebSocketServerCompressionHandler())
                .addLast(new WebSocketServerProtocolHandler("/ws", "", true, 64*1024))
                .addLast(webSocketMessageDecoder)
                .addLast(webSocketMessageEncoder)
                .addLast(nettyServerHandler);

    }
}
