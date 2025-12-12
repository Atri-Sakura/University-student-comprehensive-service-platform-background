package com.ruoyi.platform.service.impl;

import com.ruoyi.platform.chat.factory.MessageHandlerFactory;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import com.ruoyi.platform.chat.handler.MessageHandler;
import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import com.ruoyi.platform.chat.utils.SnowflakeIdGenerator;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.dto.CreateOrderDTO;
import com.ruoyi.platform.domain.vo.CreateErrandOrderDto;
import com.ruoyi.platform.service.IOrderNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单通知服务实现类（改造后：复用SystemMessageHandler做持久化+推送）
 * @author ruoyi
 * @date 2025-12-12
 */
@Service
@Slf4j
public class OrderNotifyServiceImpl implements IOrderNotifyService {

    // 复用系统消息的魔术数和消息类型
    private static final int SYSTEM_MSG_TYPE = 4;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ChannelSessionManager channelSessionManager;

    // 核心新增：注入消息处理器工厂，用于获取SystemMessageHandler
    @Autowired
    private MessageHandlerFactory messageHandlerFactory;

    /**
     * 发送外卖订单通知（给商家）
     */
    @Override
    public void sendTakeoutOrderNotify(OrderMain orderMain, CreateOrderDTO createOrderDTO) {
        // 1. 构建商家通知内容
        String notifyContent = String.format(
                "新订单提醒：\n订单号：%s\n商家：%s\n收货地址：%s\n金额：%.2f元\n请及时接单",
                orderMain.getOrderNo(),
                orderMain.getMerchantId(),
                orderMain.getDeliverAddress(),
                orderMain.getPayAmount() != null ? orderMain.getPayAmount().doubleValue() : 0.00
        );

        // 2. 构建Protobuf消息
        ChatMessageProto.ChatMessage chatMessage = buildChatMessage(
                orderMain.getMerchantId(), // 接收方：商家ID
                2, // 接收方类型：商家（2）
                notifyContent
        );

        // 3. 复用SystemMessageHandler处理（自动持久化+推送+离线处理）
        sendViaSystemHandler(chatMessage);

        log.info("外卖订单通知已提交处理，订单号：{}，商家ID：{}",
                orderMain.getOrderNo(), orderMain.getMerchantId());
    }

    /**
     * 发送跑腿订单通知（给骑手/相关方）
     */
    @Override
    public void sendErrandOrderNotify(OrderMain orderMain, CreateErrandOrderDto createErrandOrderDto) {
        // 1. 构建跑腿订单通知内容
        String errandType = orderMain.getOrderType() != null && orderMain.getOrderType().equals(2L)
                ? "帮我买" : "帮我送";
        String notifyContent = String.format(
                "新跑腿订单提醒：\n订单号：%s\n类型：%s\n商品描述：%s\n收货地址：%s\n金额：%.2f元\n请及时接单",
                orderMain.getOrderNo(),
                errandType,
                createErrandOrderDto.getGoodsDesc(),
                orderMain.getDeliverAddress(),
                orderMain.getPayAmount() != null ? orderMain.getPayAmount().doubleValue() : 0.00
        );

        // 2. 构建Protobuf消息（推送给骑手池，这里先推送给所有在线骑手示例）
        ChatMessageProto.ChatMessage chatMessage = buildChatMessage(
                0L, // 骑手池广播（实际可改为骑手ID）
                3, // 接收方类型：骑手（3）
                notifyContent
        );

        // 3. 复用SystemMessageHandler处理
        sendViaSystemHandler(chatMessage);

        log.info("跑腿订单通知已提交处理，订单号：{}", orderMain.getOrderNo());
    }

    /**
     * 发送用户下单成功通知（给下单用户）
     */
    @Override
    public void sendUserOrderSuccessNotify(OrderMain orderMain, Long userId) {
        // 1. 构建用户通知内容
        String createTime = orderMain.getCreateTime() != null
                ? DATE_FORMAT.format(orderMain.getCreateTime())
                : DATE_FORMAT.format(new Date());

        String notifyContent = String.format(
                "订单创建成功！\n订单号：%s\n商家ID：%s\n创建时间：%s\n用户手机号：%s",
                orderMain.getOrderNo(),
                orderMain.getMerchantId(),
                createTime,
                orderMain.getDeliverPhone()
        );

        // 2. 构建用户通知的Protobuf消息
        ChatMessageProto.ChatMessage userChatMessage = buildChatMessage(
                userId, // 接收方：用户ID
                1, // 接收方类型：用户（1）
                notifyContent
        );

        // 3. 复用SystemMessageHandler处理
        sendViaSystemHandler(userChatMessage);

        // ========== 可选：同时给商家发送订单创建成功通知 ==========
        if (orderMain.getMerchantId() != null) {
            String merchantNotifyContent = String.format(
                    "新订单创建成功！\n订单号：%s",
                    orderMain.getOrderNo()
            );
            ChatMessageProto.ChatMessage merchantChatMessage = buildChatMessage(
                    orderMain.getMerchantId(), // 接收方：商家ID
                    2, // 接收方类型：商家（2）
                    merchantNotifyContent
            );
            sendViaSystemHandler(merchantChatMessage);
        }

        log.info("用户下单成功通知已提交处理，用户ID：{}，订单号：{}", userId, orderMain.getOrderNo());
    }

    /**
     * 构建通用的Protobuf消息对象
     */
    private ChatMessageProto.ChatMessage buildChatMessage(Long toId, int toType, String content) {
        return ChatMessageProto.ChatMessage.newBuilder()
                .setMessageId(SnowflakeIdGenerator.getInstance().nextId()) // 唯一消息ID
                .setMsgType(SYSTEM_MSG_TYPE) // 系统消息类型
                .setMsgContent(content) // 消息内容
                .setFromType(4) // 发送方类型：系统（4）
                .setFromId(0L) // 系统发送方ID
                .setToType(toType) // 接收方类型
                .setToId(toId) // 接收方ID
                .setSendTime(String.valueOf(System.currentTimeMillis())) // 发送时间（时间戳）
                .build();
    }

    /**
     * 核心方法：通过SystemMessageHandler处理消息（自动持久化+推送+离线处理）
     */
    private void sendViaSystemHandler(ChatMessageProto.ChatMessage msg) {
        // 1. 获取系统消息处理器
        MessageHandler systemHandler = messageHandlerFactory.getMessageHandler((long) SYSTEM_MSG_TYPE);
        if (systemHandler == null) {
            log.error("未找到系统消息处理器（msgType={}），消息发送失败，消息ID：{}", SYSTEM_MSG_TYPE, msg.getMessageId());
            return;
        }

        try {
            // 2. 调用Handler的核心方法（ctx传null，因SystemMessageHandler未使用ctx）
            systemHandler.handler(channelSessionManager, null, msg);
            log.debug("消息已通过SystemMessageHandler处理完成，消息ID：{}，目标：{}:{}",
                    msg.getMessageId(), msg.getToType(), msg.getToId());
        } catch (Exception e) {
            log.error("调用SystemMessageHandler处理消息失败，消息ID：{}", msg.getMessageId(), e);
        }
    }
}