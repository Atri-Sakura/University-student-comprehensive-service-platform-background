// package com.ruoyi.platform.chat.codec;

// import com.alibaba.fastjson2.JSON;

// import com.ruoyi.common.core.domain.entity.ChatMessage;
// import io.netty.channel.ChannelHandler;
// import io.netty.channel.ChannelHandlerContext;
// import io.netty.handler.codec.MessageToMessageDecoder;
// import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
// import org.springframework.stereotype.Component;

// import java.util.List;

// @Component
// @ChannelHandler.Sharable
// public class WebSocketMessageDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {
//     @Override
//     protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg, List<Object> list) throws Exception {
//         String json = msg.text();
//         ChatMessage chatMessage = JSON.parseObject(json, ChatMessage.class);
//         list.add(chatMessage);

//     }
// }
