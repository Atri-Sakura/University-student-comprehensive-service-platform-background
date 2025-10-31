package com.ruoyi.platform.chat.manager;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChannelSessionManager {

    private final Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    private final Map<Channel,String> channelKeyMap = new ConcurrentHashMap<>();

    public void registerChannel(Long Id,Long userType,Channel channel) {
        Objects.requireNonNull(Id,"Id不能为空");
        Objects.requireNonNull(userType,"userType不能为空");
        Objects.requireNonNull(channel,"channel不能为空");

        String key = userType + ":" + Id;

        Channel oldChannel = channelMap.get(key);
        if (oldChannel != null && oldChannel != channel) {
            oldChannel.close();
            channelKeyMap.remove(key);
            log.info("关闭连接，key:{}",key);
        }

        channelMap.put(key, channel);
        channelKeyMap.put(channel,key);
        log.info("register success key:{}",key);
    }

    public void removeChannel(Channel channel) {
        if(channel == null) {
            return;
        }
        // 通过反向映射快速获取key
        String key = channelKeyMap.remove(channel);
        if (key != null) {
            channelMap.remove(key);
            log.info("用户通道移除成功，key: {}", key);
        }
    }

    public Channel getChannel(String key) {
        return channelMap.get(key);
    }


    public void unregisterChannel(Channel channel) {
        String key = channelKeyMap.get(channel);
    }
}
