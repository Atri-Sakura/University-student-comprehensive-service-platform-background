package com.ruoyi.platform.chat.utils;

import com.ruoyi.platform.chat.codec.*;
import com.ruoyi.platform.chat.handler.ChatHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

@Component
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final ChatHandler chatHandler;

    public NettyServerChannelInitializer(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

//    @Autowired
//    private WebSocketMessageEncoder webSocketMessageEncoder;
//
//    @Autowired
//    private WebSocketMessageDecoder webSocketMessageDecoder;
//
//    @Autowired
//    private NettyServerHandler nettyServerHandler;

//    @Autowired
//    private BinaryMessageEncoder binaryMessageEncoder;
//
//    @Autowired
//    private BinaryMessagedDecoder binaryMessageDecoder;

//    @Autowired
//    private BinaryChatHandler binaryChatHandler;


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(64*1024))
//                .addLast(new WebSocketServerCompressionHandler())
                .addLast(new WebSocketServerProtocolHandler("/ws", "", true, 64*1024))
                .addLast(new IdleStateHandler(30, 0, 0))
//                .addLast(new BinaryMessagedDecoder())
//                .addLast(new WebSocketFrameToBinaryMessageDecoder())
//                .addLast(new BinaryMessageEncoder())
                .addLast(new WebSocketToProtobufDecoder())
                .addLast(new ProtobufToWebSocketEncoder())
                .addLast(chatHandler);
//                .addLast(binaryChatHandler);
//                .addLast(webSocketMessageDecoder)
//                .addLast(webSocketMessageEncoder)
//                .addLast(nettyServerHandler);

    }
}
