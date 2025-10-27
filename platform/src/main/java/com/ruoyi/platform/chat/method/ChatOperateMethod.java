package com.ruoyi.platform.chat.method;

import com.ruoyi.common.core.domain.entity.ChatMessage;
import com.ruoyi.platform.domain.ChatSession;
import com.ruoyi.platform.service.IChatMessageService;
import com.ruoyi.platform.service.IChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ChatOperateMethod {

    @Autowired
    private IChatMessageService chatMessageService;

    @Autowired
    private IChatSessionService chatSessionService;

    public ChatSession initSession(ChatMessage chatMessage) {
        ChatSession chatSession = new ChatSession();
        chatSession.setFromId(chatMessage.getFromId());
        chatSession.setToId(chatMessage.getToId());
        chatSession.setFromType(chatMessage.getFromType());
        chatSession.setToType(chatMessage.getToType());
        return chatSession;
    }

    public void updateSession(ChatMessage chatMessage,ChatSession chatSession) {
        chatSession.setSessionId(chatMessage.getSessionId());
        chatSession.setLastMsgContent(chatMessage.getMsgContent());
        chatSession.setLastMsgId(chatMessage.getMessageId());
        chatSession.setLastMsgType(chatMessage.getMsgType());
        chatMessageService.updateChatMessageStatus(chatMessage,1L);
        chatSessionService.updateChatSession(chatSession);
    }

}
