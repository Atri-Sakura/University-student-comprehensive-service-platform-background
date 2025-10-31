package com.ruoyi.platform.chat.factory;

import com.ruoyi.platform.chat.handler.MessageHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessageHandlerFactory {

    private final Map<Long, MessageHandler> handlerMap;

    public MessageHandlerFactory(List<MessageHandler> handlers) {
        handlerMap = new HashMap<>();
        for (MessageHandler handler : handlers) {
            handlerMap.put(handler.supportType(), handler);
        }
    }

    public MessageHandler getMessageHandler(Long supportType) {
        return handlerMap.get(supportType);
    }
}