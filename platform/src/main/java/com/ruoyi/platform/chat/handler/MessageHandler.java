package com.ruoyi.platform.chat.handler;

import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import com.ruoyi.platform.domain.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public interface MessageHandler {

    Long supportType();

    void handler(ChannelSessionManager channelSessionManager, ChannelHandlerContext channelHandlerContext , ChatMessage chatMessage);

}
