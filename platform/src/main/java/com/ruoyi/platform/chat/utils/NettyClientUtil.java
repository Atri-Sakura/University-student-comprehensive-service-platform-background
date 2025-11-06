package com.ruoyi.platform.chat.utils;

import com.fasterxml.jackson.core.JsonProcessingException;


import com.ruoyi.common.utils.json.JacksonUtils;
import com.ruoyi.platform.chat.codec.ProtobufToWebSocketEncoder;
import com.ruoyi.platform.chat.codec.WebSocketToProtobufDecoder;
import com.ruoyi.platform.chat.factory.MessageHandlerFactory;
import com.ruoyi.platform.chat.handler.ClientWebSocketHandler;
import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import com.ruoyi.platform.domain.ChatMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NettyClientUtil {

    @Autowired
    private MessageHandlerFactory messageHandlerFactory;

    private String webSocketUri = "ws://localhost:8010/ws";

    // 全局唯一通道
    private static volatile Channel channel;
    // 当前连接的用户信息（用于重连）
    private static Long currentUserType;
    private static Long currentUserId;
    // Netty客户端线程池（全局唯一）
    private static final NioEventLoopGroup group = new NioEventLoopGroup();

    @Autowired
    private ScheduledExecutorService executorService;


    /**
     * 连接WebSocket服务器并注册用户
     */
    public boolean connectAndRegister(Long userType, Long userBaseId) {
        // 保存当前用户信息（用于重连）
        currentUserType = userType;
        currentUserId = userBaseId;

        // 若已有活跃连接，直接发送注册消息
        if (isConnected()) {
            log.info("已有活跃连接，直接发送注册消息，用户[{}:{}]", userType, userBaseId);
            sendRegisterMessage(userType, userBaseId);
            return true;
        }

        try {
            URI uri = new URI(webSocketUri);
            String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
            String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
            int port = uri.getPort() == -1 ? ("ws".equals(scheme) ? 80 : 443) : uri.getPort();

            // 构建SSL上下文（wss协议时需要）
            final SslContext sslCtx = "wss".equals(scheme) ?
                    SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build() : null;

            // 握手倒计时锁（最多等待5秒）
            CountDownLatch handshakeLatch = new CountDownLatch(1);
            ClientWebSocketHandler clientHandler = new ClientWebSocketHandler(
                    WebSocketClientHandshakerFactory.newHandshaker(
                            uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()),
                    handshakeLatch // 传入当前工具类，用于重连回调
            );

            // 初始化Netty客户端
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            if (sslCtx != null) {
                                pipeline.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                            }
                            pipeline.addLast(new HttpClientCodec())
                                    .addLast(new HttpObjectAggregator(8192))
                                    .addLast(WebSocketClientCompressionHandler.INSTANCE)
                                    .addLast(new ProtobufToWebSocketEncoder())
                                    .addLast(new WebSocketToProtobufDecoder())
                                    .addLast(clientHandler);
                        }
                    });

            // 连接服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();

            // 等待握手完成
            boolean handshakeSuccess = handshakeLatch.await(5, TimeUnit.SECONDS);
            if (!handshakeSuccess) {
                log.error("WebSocket握手超时（5秒），用户[{}:{}]", userType, userBaseId);
                disconnect();
                return false;
            }

            // 握手成功后发送注册消息
            sendRegisterMessage(userType, userBaseId);
            return true;

        } catch (URISyntaxException | InterruptedException | SSLException e) {
            log.error("WebSocket连接失败，用户[{}:{}]", userType, userBaseId, e);
            disconnect();
            return false;
        }
    }


    /**
     * 发送注册消息（异步执行）
     */
    private void sendRegisterMessage(Long userType, Long userBaseId) {
        executorService.execute(() -> {
            try {
                if (!isConnected()) {
                    log.warn("通道未连接，无法发送注册消息，用户[{}:{}]", userType, userBaseId);
                    return;
                }

                // 构建注册消息（msgType=0表示注册）
                ChatMessageProto.ChatMessage registerMsg = ChatMessageProto.ChatMessage.newBuilder()
                        .setMsgType(0)
                        .setFromType(userType.intValue())
                        .setFromId(userBaseId)
                        .build();

                // 异步发送消息，不阻塞线程
                channel.writeAndFlush(registerMsg).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("注册消息发送成功，用户[{}:{}]", userType, userBaseId);
                    } else {
                        log.error("注册消息发送失败，用户[{}:{}]", userType, userBaseId, future.cause());
                        // 发送失败重试（最多3次）
                        retryRegister(userType, userBaseId, 1);
                    }
                });
            } catch (Exception e) {
                log.error("注册消息处理异常，用户[{}:{}]", userType, userBaseId, e);
            }
        });
    }


    /**
     * 重试发送注册消息
     */
    private void retryRegister(Long userType, Long userBaseId, int retryCount) {
        if (retryCount > 3) { // 最多重试3次
            log.error("注册消息重试次数耗尽，用户[{}:{}]", userType, userBaseId);
            return;
        }

        executorService.schedule(() -> {
            log.info("第{}次重试发送注册消息，用户[{}:{}]", retryCount, userType, userBaseId);
            sendRegisterMessage(userType, userBaseId);
        }, retryCount * 2, TimeUnit.SECONDS); // 指数退避重试（2s,4s,6s）
    }


    /**
     * 断线重连（由ClientWebSocketHandler调用）
     */
    public void reconnect() {
        if (currentUserType == null || currentUserId == null) {
            log.warn("无当前用户信息，无法重连");
            return;
        }

        executorService.schedule(() -> {
            log.info("开始重连WebSocket，用户[{}:{}]", currentUserType, currentUserId);
            connectAndRegister(currentUserType, currentUserId);
        }, 3, TimeUnit.SECONDS); // 延迟3秒重连
    }


    /**
     * 断开当前连接（仅关闭通道，不关闭线程池）
     */
    public void disconnect() {
        if (channel != null && channel.isActive()) {
            try {
                channel.writeAndFlush(new CloseWebSocketFrame()).sync();
                channel.close().sync();
                log.info("WebSocket通道已关闭");
            } catch (InterruptedException e) {
                log.error("关闭通道异常", e);
                Thread.currentThread().interrupt();
            } finally {
                channel = null;
            }
        }
    }


    /**
     * 检查通道是否活跃
     */
    public boolean isConnected() {
        return channel != null && channel.isActive();
    }


    /**
     * 应用退出时关闭线程池
     */
    @PreDestroy
    public void shutdown() {
        if (!group.isShutdown()) {
            group.shutdownGracefully();
            log.info("Netty客户端线程池已关闭");
        }
    }


    // getter（供ClientWebSocketHandler获取当前用户信息）
    public Long getCurrentUserType() {
        return currentUserType;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }
}
