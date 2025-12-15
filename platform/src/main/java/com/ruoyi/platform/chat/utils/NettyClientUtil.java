package com.ruoyi.platform.chat.utils;

import com.ruoyi.platform.chat.codec.ProtobufToWebSocketEncoder;
import com.ruoyi.platform.chat.codec.WebSocketToProtobufDecoder;
import com.ruoyi.platform.chat.factory.MessageHandlerFactory;
import com.ruoyi.platform.chat.handler.ClientWebSocketHandler;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class NettyClientUtil {

    @Autowired
    private MessageHandlerFactory messageHandlerFactory;

    // 心跳间隔（秒）
    private static final int HEARTBEAT_INTERVAL = 10;
    // 最大重连次数（超过后停止重试）
    private static final int MAX_RECONNECT_ATTEMPTS = 10;
    // 初始重连延迟（秒）
    private static final int INITIAL_RECONNECT_DELAY = 1;
    // 最大重连延迟（秒，防止间隔过大）
    private static final int MAX_RECONNECT_DELAY = 60;

    private String webSocketUri = "ws://localhost:8010/ws";

    // 全局唯一通道（线程安全处理）
    private static final AtomicReference<Channel> channelRef = new AtomicReference<>();
    // 当前连接的用户信息（用于重连）
    private static Long currentUserType;
    private static Long currentUserId;
    // 重连计数器（记录当前重试次数）
    private static final AtomicInteger reconnectAttempts = new AtomicInteger(0);
    // Netty客户端线程池（全局唯一，指定2个线程）
    private static final NioEventLoopGroup group = new NioEventLoopGroup(2);

    @Autowired
    private ScheduledExecutorService executorService;
    @Autowired
    private ChannelSessionManager channelSessionManager;
    // 保存当前重连任务，避免重复提交
    private static volatile ScheduledFuture<?> reconnectFuture;
    // 保存心跳任务引用，避免重复提交
    private ScheduledFuture<?> heartbeatFuture;


    /**
     * 连接WebSocket服务器并注册用户
     */
    public boolean connectAndRegister(Long userType, Long userBaseId) {
        // 保存当前用户信息（用于重连）
        currentUserType = userType;
        currentUserId = userBaseId;
        // 重置重连计数器（新连接/首次连接时）
        resetReconnectAttempts();

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

            // 构建SSL上下文（wss协议时需要，生产环境需替换为合法证书）
            final SslContext sslCtx = "wss".equals(scheme) ?
                    SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build() : null;

            // 握手倒计时锁（最多等待5秒）
            CountDownLatch handshakeLatch = new CountDownLatch(1);
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                    uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()
            );
            ClientWebSocketHandler clientHandler = new ClientWebSocketHandler(this, handshaker, handshakeLatch);

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
            channelRef.set(future.channel()); // 线程安全设置通道

            // 等待握手完成（最多5秒）
            boolean handshakeSuccess = handshakeLatch.await(5, java.util.concurrent.TimeUnit.SECONDS);
            if (!handshakeSuccess) {
                log.error("WebSocket握手超时（5秒），用户[{}:{}]", userType, userBaseId);
                disconnect();
                return false;
            }

            // 握手成功后发送注册消息
            sendRegisterMessage(userType, userBaseId);
            return true;

        } catch (URISyntaxException | InterruptedException | SSLException e) {
            log.error("WebSocket连接失败，用户[{}:{}]，重试次数[{}/{}]",
                    userType, userBaseId, reconnectAttempts, MAX_RECONNECT_ATTEMPTS, e);
            disconnect();
            // 连接失败后触发重连（如果未达最大次数）
            scheduleReconnect();
            return false;
        }
    }


    /**
     * 断线重连（带指数退避和最大次数限制）
     */
    public void reconnect() {
        // 取消当前可能存在的重连任务，避免重复执行
        cancelExistingReconnectTask();

        // 检查是否已达最大重连次数
        if (reconnectAttempts.get() >= MAX_RECONNECT_ATTEMPTS) {
            log.error("重连次数已达上限[{}/{}]，停止重试，用户[{}:{}]",
                    reconnectAttempts, MAX_RECONNECT_ATTEMPTS, currentUserType, currentUserId);
            // 此处可添加告警逻辑（如发送邮件/短信通知）
            return;
        }

        // 计算指数退避延迟（公式：min(初始延迟 * 2^重试次数, 最大延迟)）
        long delaySeconds = (long) Math.min(
                INITIAL_RECONNECT_DELAY * Math.pow(2, reconnectAttempts.get()),
                MAX_RECONNECT_DELAY
        );

        // 提交重连任务
        reconnectFuture = executorService.schedule(() -> {
            try {
                int count = reconnectAttempts.incrementAndGet(); // 递增重试次数
                log.info("开始第{}次重连，延迟{}秒，用户[{}:{}]",
                        count, delaySeconds, currentUserType, currentUserId);
                connectAndRegister(currentUserType, currentUserId);
            } catch (Exception e) {
                log.error("重连任务执行异常", e);
            }
        }, delaySeconds, java.util.concurrent.TimeUnit.SECONDS);
    }


    /**
     * 取消已存在的重连任务（避免重复提交）
     */
    private void cancelExistingReconnectTask() {
        if (reconnectFuture != null && !reconnectFuture.isDone()) {
            reconnectFuture.cancel(false);
            log.info("已取消当前重连任务，用户[{}:{}]", currentUserType, currentUserId);
        }
    }


    /**
     * 重置重连计数器（连接成功时调用）
     */
    private void resetReconnectAttempts() {
        reconnectAttempts.set(0);
        cancelExistingReconnectTask(); // 连接成功后取消所有待执行的重连任务
    }


    /**
     * 主动触发重连（连接失败时调用）
     */
    private void scheduleReconnect() {
        if (currentUserType != null && currentUserId != null) {
            reconnect();
        }
    }


    /**
     * 断开当前连接
     */
    public void disconnect() {
        Channel channel = channelRef.get();
        if (channel != null && channel.isActive()) {
            try {
                // 异步关闭通道，避免阻塞
                channel.writeAndFlush(new CloseWebSocketFrame())
                        .addListener(ChannelFutureListener.CLOSE)
                        .addListener(future -> {
                            if (future.isSuccess()) {
                                log.info("WebSocket通道已关闭");
                            } else {
                                log.error("通道关闭失败", future.cause());
                            }
                        });
            } finally {
                channelRef.set(null); // 清空通道引用
            }
        }
    }


    /**
     * 发送注册消息（连接成功后调用）
     */
    private void sendRegisterMessage(Long userType, Long userBaseId) {
        executorService.execute(() -> {
            try {
                if (!isConnected()) {
                    log.warn("通道未连接，无法发送注册消息，用户[{}:{}]", userType, userBaseId);
                    return;
                }

                ChatMessageProto.ChatMessage registerMsg = ChatMessageProto.ChatMessage.newBuilder()
                        .setMsgType(0)
                        .setFromType(userType.intValue())
                        .setFromId(userBaseId)
                        .build();

                channelRef.get().writeAndFlush(registerMsg).addListener(future -> {
                    if (future.isSuccess()) {
                        resetReconnectAttempts(); // 注册成功，重置重连计数器
//                        startHeartbeatTask();
                        log.info("注册消息发送成功，用户[{}:{}]", userType, userBaseId);
                        pullOfflineMessage(userType, userBaseId);
                    } else {
                        log.error("注册消息发送失败，用户[{}:{}]", userType, userBaseId, future.cause());
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
        }, retryCount * 2, java.util.concurrent.TimeUnit.SECONDS); // 指数退避重试（2s,4s,6s）
    }


    /**
     * 启动心跳任务（优化重复任务问题）
     */
    private void startHeartbeatTask() {
        // 取消已有心跳任务，避免重复
        if (heartbeatFuture != null && !heartbeatFuture.isCancelled()) {
            heartbeatFuture.cancel(false);
        }

        log.info("启动心跳任务，间隔{}秒，用户[{}:{}]",
                HEARTBEAT_INTERVAL, currentUserType, currentUserId);
        heartbeatFuture = executorService.scheduleAtFixedRate(() -> {
            if (isConnected()) {
                ChatMessageProto.ChatMessage chatMessage = ChatMessageProto.ChatMessage.newBuilder()
                        .setMessageId(System.currentTimeMillis())
                        .setMsgType(6)
                        .setFromId(currentUserId)
                        .setFromType(Math.toIntExact(currentUserType))
                        .build();
                channelRef.get().writeAndFlush(chatMessage).addListener(future -> {
                    if (!future.isSuccess()) {
                        log.error("心跳发送失败，用户[{}:{}]", currentUserType, currentUserId, future.cause());
                        reconnect(); // 心跳失败触发重连
                    } else {
                        log.info("心跳消息发送成功，用户[{}:{}]，消息ID:{}",
                                currentUserType, currentUserId, chatMessage.getMessageId());
                    }
                });
            }
        }, 5, HEARTBEAT_INTERVAL, java.util.concurrent.TimeUnit.SECONDS);
    }


    /**
     * 拉取离线消息方法
     */
    private void pullOfflineMessage(Long userType, Long userId) {
        if (isConnected()) {
            ChatMessageProto.ChatMessage pullMsg = ChatMessageProto.ChatMessage.newBuilder()
                    .setMsgType(3) // 对应OfflineMessagePullHandler的supportType=3
                    .setFromId(userId)
                    .setFromType(userType.intValue())
                    .build();
            channelRef.get().writeAndFlush(pullMsg);
            log.info("已发送离线消息拉取请求，用户[{}:{}]", userType, userId);
        }
    }


    /**
     * 检查通道是否活跃（线程安全）
     */
    public boolean isConnected() {
        Channel channel = channelRef.get();
        return channel != null && channel.isActive();
    }


    /**
     * 应用退出时关闭资源
     */
    @PreDestroy
    public void shutdown() {
        cancelExistingReconnectTask(); // 取消重连任务
        if (heartbeatFuture != null) {
            heartbeatFuture.cancel(false); // 取消心跳任务
        }
        disconnect(); // 关闭通道
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