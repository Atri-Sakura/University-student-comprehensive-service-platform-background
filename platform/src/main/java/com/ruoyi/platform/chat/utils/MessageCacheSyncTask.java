package com.ruoyi.platform.chat.utils;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.platform.domain.ChatMessage;
import com.ruoyi.platform.service.IChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MessageCacheSyncTask {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IChatMessageService chatMessageService;

    @Autowired
    private ChatCacheUtils chatCacheUtils;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //锁名称
    private static final String SYN_LOCK_KEY = "chat:message:sync:lock";
    //超时时长（超时则过期)
    private static final long SYNC_TIME = 1000 * 60 * 5;
    // 每小时执行一次
    @Scheduled(cron = "0 0 * * * ?")
    public void syncCacheAndDb() {
        //获取分布式锁
        RLock lock = redissonClient.getLock(SYN_LOCK_KEY);
        boolean isLocked = false;
        try{
            isLocked = lock.tryLock(10,SYNC_TIME, TimeUnit.SECONDS);
            if (!isLocked) {
                log.warn("锁同步失效,本次同步任务跳过");
                return;
            }

        log.info("开始同步消息缓存与数据库状态");
        // 1. 扫描最近1天内有状态更新的消息
        List<ChatMessage> updatedMessages = chatMessageService.selectRecentlyUpdatedMessages(1);
        for (ChatMessage msg : updatedMessages) {
            String cacheKey = chatCacheUtils.buildCacheKey(msg.getSessionId());
            // 2. 检查缓存中该消息的状态是否与DB一致
            boolean isConsistent = checkCacheConsistency(cacheKey, msg);
            if (!isConsistent) {
                log.warn("消息状态不一致，消息ID: {}，DB状态: {}，缓存状态: ...", msg.getMessageId(), msg.getMsgStatus());
                // 3. 不一致则删除缓存，下次读取时从DB加载
                chatCacheUtils.updateMessageInCache(msg);
            }
        }
        log.info("消息缓存与数据库状态同步完成");
    }catch (InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }finally {
            if(isLocked == true && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
        }

    // 校验单条消息的缓存与DB状态是否一致
    private boolean checkCacheConsistency(String cacheKey, ChatMessage dbMsg) {
        List<Object> cacheList = redisTemplate.opsForList().range(cacheKey, 0, -1);
        if (cacheList == null) {
            return false; // 缓存不存在，无需校验
        }
        for (Object obj : cacheList) {
            ChatMessage cacheMsg = JSON.parseObject(JSON.toJSONString(obj), ChatMessage.class);
            if (cacheMsg.getMessageId().equals(dbMsg.getMessageId())) {
                boolean consistent = cacheMsg.getVersion() == dbMsg.getVersion();
                if (!consistent) {
                    log.debug("消息{}版本不一致，缓存版本:{}", cacheMsg.getMessageId(), cacheMsg.getVersion());
                }
                // 状态不一致
                return consistent;
            }
        }
        log.debug("缓存中未找到消息{},需要同步",dbMsg.getMessageId());
        return false; // 缓存中不存在该消息，无需校验
    }
}