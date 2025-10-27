package com.ruoyi.common.utils.netty;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.ruoyi.common.core.domain.entity.ChatMessage;
import com.ruoyi.common.utils.handler.ClientWebSocketHandler;
import com.ruoyi.common.utils.json.JacksonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NettyClientUtil {

    private String webSocketUri = "ws://localhost:8010/ws";

    private Channel channel;

    private  NioEventLoopGroup group = new NioEventLoopGroup();

    public boolean connectAndRegister(Long userType,Long userBaseId) {
        try{
            URI uri = new URI(webSocketUri);
            String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
            final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
            final int port;
            if(uri.getPort() == -1) {
                port = "ws".equals(scheme) ? 80 : "wss".equals(scheme) ? 443 : 80;
            }else{
                port = uri.getPort();
            }
            final SslContext sslCtx;
            if("wss".equals(scheme)) {
                sslCtx = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            }else{
                sslCtx = null;
            }

            //juc包中的倒计时锁存器
            CountDownLatch handshakeLatch = new CountDownLatch(1);
            ClientWebSocketHandler clientWebSocketHandler = new ClientWebSocketHandler(
                    WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13,null,true,new DefaultHttpHeaders()),
                    handshakeLatch
            );
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //判断是否为HTTPS协议（是否使用了TLS/SSL协议）
                            if(sslCtx !=null){
                                pipeline.addLast(sslCtx.newHandler(socketChannel.alloc(),host,port));
                            }
                            pipeline.addLast(new HttpClientCodec())
                                    .addLast(new HttpObjectAggregator(8192))
                                    .addLast(WebSocketClientCompressionHandler.INSTANCE)
                                    .addLast(clientWebSocketHandler);

                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channel = channelFuture.channel();

            //等待握手（最多5s)
            boolean handshakeSuccess = handshakeLatch.await(5, TimeUnit.SECONDS);
            if(!handshakeSuccess){
                log.error("WebSocket handshake over time");
                disconnect();
                return false;
            }
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMsgType(0L);
            chatMessage.setFromType(userType);
            chatMessage.setFromId(userBaseId);
            String jsonMsg = JacksonUtils.toJson(chatMessage);
            channel.writeAndFlush(new TextWebSocketFrame(jsonMsg)).sync();

            log.info("用户[{}:{}]已通过WebSocket注册到聊天服务器", userType, userBaseId);
            return true;


        } catch (URISyntaxException | InterruptedException | SSLException | JsonProcessingException e) {
            log.error("WebSocket连接或注册失败", e);
            disconnect();
            return false;
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if(channel != null && channel.isActive()) {
            channel.writeAndFlush(new CloseWebSocketFrame());
            channel.close();
        }
    }


}
