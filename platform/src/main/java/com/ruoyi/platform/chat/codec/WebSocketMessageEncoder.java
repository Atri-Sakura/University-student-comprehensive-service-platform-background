package com.ruoyi.platform.chat.codec;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.entity.ChatMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ChannelHandler.Sharable
public class WebSocketMessageEncoder extends MessageToMessageEncoder<ChatMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, List<Object> list) throws Exception {
        String json = JSON.toJSONString(chatMessage);
        list.add(new TextWebSocketFrame(json));
    }
}
