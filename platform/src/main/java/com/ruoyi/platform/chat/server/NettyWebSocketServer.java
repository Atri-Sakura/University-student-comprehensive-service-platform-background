package com.ruoyi.platform.chat.server;

import com.ruoyi.platform.chat.codec.WebSocketMessageDecoder;
import com.ruoyi.platform.chat.codec.WebSocketMessageEncoder;
import com.ruoyi.platform.chat.utils.NettyServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.SameLen;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NettyWebSocketServer implements CommandLineRunner , DisposableBean {

    ServerBootstrap bootstrap = new ServerBootstrap();

    NioEventLoopGroup boss = new NioEventLoopGroup();

    NioEventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel ;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private NettyServerChannelInitializer nettyServerChannelInitializer;

    /**
     * 关闭服务器
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        if(channel != null){
            channel.close();
        }
        boss.shutdownGracefully();
        worker.shutdownGracefully();

    }

    /**
     * 启动服务器
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        threadPoolTaskExecutor.execute(() -> {
            try {
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(nettyServerChannelInitializer);

                ChannelFuture future = bootstrap.bind(8010).sync();
                channel = future.channel();
                log.info("Netty服务器启动成功，监听端口:8010");
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error(e.getMessage(),e);
            }
        });
    }
}
