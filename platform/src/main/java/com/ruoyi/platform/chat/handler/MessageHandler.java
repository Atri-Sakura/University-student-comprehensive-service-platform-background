package com.ruoyi.platform.chat.handler;

import com.ruoyi.common.core.domain.entity.ChatMessage;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public interface MessageHandler {

    Long supportType();

    void handler(ChannelSessionManager channelSessionManager, ChannelHandlerContext channelHandlerContext , ChatMessage chatMessage);

}
