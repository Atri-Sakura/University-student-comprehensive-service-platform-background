package com.ruoyi.platform.chat.handler;

import com.ruoyi.platform.chat.manager.ChannelSessionManager;

import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import io.netty.channel.ChannelHandlerContext;

public interface MessageHandler {

    long supportType();

    void handler(ChannelSessionManager channelSessionManager, ChannelHandlerContext ctx, ChatMessageProto.ChatMessage chatMessage);
}