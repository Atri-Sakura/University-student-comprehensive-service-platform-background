package com.ruoyi.platform.chat.handler.impl;

import com.ruoyi.common.core.domain.entity.ChatMessage;
import com.ruoyi.platform.chat.handler.MessageHandler;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import com.ruoyi.platform.chat.method.ChatOperateMethod;
import com.ruoyi.platform.domain.ChatSession;
import com.ruoyi.platform.service.IChatMessageService;
import com.ruoyi.platform.service.IChatSessionService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
@Slf4j
public class TextMessageHandler implements MessageHandler {

    @Autowired
    private ExecutorService messageExecutor;

    @Autowired
    private IChatSessionService chatSessionService;

    @Autowired
    private IChatMessageService chatMessageService;

    @Autowired
    private ChatOperateMethod chatOperateMethod;

    /**
     * 支持文本类型
     * @return
     */
    @Override
    public Long supportType() {
        return 1L;
    }

    @Override
    public void handler(ChannelSessionManager channelSessionManager, ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage) {
        messageExecutor.execute(() -> {
            try{
                Long sessionId = chatSessionService.selectChatSessionIdByFromTo(chatMessage.getFromType(),chatMessage.getFromId(),chatMessage.getToType(),chatMessage.getToId());
                ChatSession chatSession = chatOperateMethod.initSession(chatMessage);
                log.info("sessionId:{}",sessionId);
                if(sessionId == null){

                    sessionId = Long.valueOf(chatSessionService.insertChatSession(chatSession));

                }
                chatMessage.setSessionId(sessionId);
                chatMessageService.insertChatMessage(chatMessage);


                Long finalSessionId = sessionId;
                channelHandlerContext.channel().eventLoop().execute(() -> {
                    String key = chatMessage.getToType() + ":" + chatMessage.getToId();
                    Channel receiver = channelSessionManager.getChannel(key);

                    if(receiver != null && receiver.isActive()){
                        receiver.writeAndFlush(chatMessage);
                        chatMessage.setMsgStatus(1L);
                    }else {
                        chatMessage.setMsgStatus(4L);
                    }
                    chatOperateMethod.updateSession(chatMessage,chatSession);
//                    chatSessionService.updateUnreadCount(finalSessionId);
                });
            }catch (Exception e){
                log.error(e.getMessage(),e);
                channelHandlerContext.channel().eventLoop().execute(() -> {
                    ChatMessage errorResponse = new ChatMessage();
                    errorResponse.setMsgStatus(-1l);
                    errorResponse.setMsgContent("处理消息失败："+e.getMessage());
                    channelHandlerContext.writeAndFlush(errorResponse);
                });
            }
        });
    }
}
