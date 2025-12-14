package com.ruoyi.platform.service.impl;

import com.ruoyi.platform.chat.factory.MessageHandlerFactory;
import com.ruoyi.platform.chat.manager.ChannelSessionManager;
import com.ruoyi.platform.chat.handler.MessageHandler;
import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import com.ruoyi.platform.chat.utils.SnowflakeIdGenerator;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.dto.CreateOrderDTO;
import com.ruoyi.platform.domain.vo.CreateErrandOrderDto;
import com.ruoyi.platform.service.IOrderMainService;
import com.ruoyi.platform.service.IOrderNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单通知服务实现类（修复商家类型错误+补全接口）
 * @author ruoyi
 * @date 2025-12-12
 */
@Service
@Slf4j
public class OrderNotifyServiceImpl implements IOrderNotifyService {

    private static final int SYSTEM_MSG_TYPE = 4;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ChannelSessionManager channelSessionManager;
    @Autowired
    private MessageHandlerFactory messageHandlerFactory;
    @Autowired
    private IOrderMainService orderMainService;

    // ------------------------------ 外卖订单商家通知 ------------------------------
    @Override
    public void sendTakeoutOrderNotify(OrderMain orderMain, CreateOrderDTO createOrderDTO) {
        String notifyContent = String.format(
                "新订单提醒：\n订单号：%s\n收货地址：%s\n金额：%.2f元\n请及时接单",
                orderMain.getOrderNo(),
                orderMain.getDeliverAddress(),
                orderMain.getPayAmount() != null ? orderMain.getPayAmount().doubleValue() : 0.00
        );

        // 修复：商家接收方类型应为2（原代码错误混用3）
        ChatMessageProto.ChatMessage chatMessage = buildChatMessage(
                orderMain.getMerchantId(), // 接收方：商家ID
                2, // 接收方类型：商家（2）
                notifyContent
        );
        sendViaSystemHandler(chatMessage);
        log.info("外卖订单商家通知已发送，订单号：{}，商家ID：{}", orderMain.getOrderNo(), orderMain.getMerchantId());
    }

    // ------------------------------ 骑手接单通知 ------------------------------
    @Override
    public void sendPickOrderToUserNotify(Long riderId, Long orderMainId) {
        OrderMain orderMain = orderMainService.selectOrderMainByOrderMainId(orderMainId);
        if (orderMain == null) {
            log.warn("骑手接单通知失败：订单ID{}不存在", orderMainId);
            return;
        }
        // 给骑手发通知
        ChatMessageProto.ChatMessage riderMessage = buildChatMessage(
                riderId,
                3, // 接收方类型：骑手（3）
                "您已成功接收到订单，订单号: " + orderMain.getOrderNo()
        );
        // 给用户发通知
        ChatMessageProto.ChatMessage userMessage = buildChatMessage(
                orderMain.getUserId(),
                1, // 接收方类型：用户（1）
                "骑手已经成功接到您的订单,订单号: " + orderMain.getOrderNo()
        );
        sendViaSystemHandler(userMessage);
        sendViaSystemHandler(riderMessage);

        // 给商家发通知
        if (orderMain.getMerchantId() != null) {
            ChatMessageProto.ChatMessage merchantMessage = buildChatMessage(
                    orderMain.getMerchantId(),
                    2, // 修复：商家类型为2
                    "骑手已经成功接到订单,订单号: " + orderMain.getOrderNo()
            );
            sendViaSystemHandler(merchantMessage);
        }
    }

    // ------------------------------ 商家接单通知 ------------------------------
    @Override
    public void sendMerchantAcceptOrderToUserNotify(Long orderMainId, Long merchantId) {
        OrderMain orderMain = orderMainService.selectOrderMainByOrderMainId(orderMainId);
        if (orderMain == null) {
            log.warn("商家接单通知失败：订单ID{}不存在", orderMainId);
            return;
        }
        // 给商家发通知
        ChatMessageProto.ChatMessage merchantMessage = buildChatMessage(
                merchantId,
                2, // 接收方类型：商家（2）
                "您已成功接收到该订单,订单号为：" + orderMain.getOrderNo()
        );
        // 给用户发通知
        ChatMessageProto.ChatMessage userMessage = buildChatMessage(
                orderMain.getUserId(),
                1, // 接收方类型：用户（1）
                "商家已成功接受您的订单，订单号为：" + orderMain.getOrderNo()
        );
        sendViaSystemHandler(userMessage);
        sendViaSystemHandler(merchantMessage);
    }

    // ------------------------------ 跑腿订单通知 ------------------------------
    @Override
    public void sendErrandOrderNotify(OrderMain orderMain, CreateErrandOrderDto createErrandOrderDto) {
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

        // 骑手池广播（toId=0，类型=3）
        ChatMessageProto.ChatMessage chatMessage = buildChatMessage(
                0L,
                3, // 接收方类型：骑手（3）
                notifyContent
        );
        sendViaSystemHandler(chatMessage);
        log.info("跑腿订单通知已发送，订单号：{}", orderMain.getOrderNo());
    }

    // ------------------------------ 用户下单成功通知 ------------------------------
    @Override
    public void sendUserOrderSuccessNotify(OrderMain orderMain, Long userId) {
        String createTime = orderMain.getCreateTime() != null
                ? DATE_FORMAT.format(orderMain.getCreateTime())
                : DATE_FORMAT.format(new Date());

        String notifyContent = String.format(
                "订单创建成功！\n订单号：%s\n创建时间：%s\n联系电话：%s",
                orderMain.getOrderNo(),
                createTime,
                orderMain.getDeliverPhone()
        );

        ChatMessageProto.ChatMessage userChatMessage = buildChatMessage(
                userId,
                1, // 接收方类型：用户（1）
                notifyContent
        );
        sendViaSystemHandler(userChatMessage);

        // 商家通知（修复类型错误）
        if (orderMain.getMerchantId() != null) {
            String merchantNotifyContent = String.format(
                    "您有新的订单啦！\n订单号：%s \n用户电话：%s \n总金额：%s",
                    orderMain.getOrderNo(), orderMain.getDeliverPhone(), orderMain.getTotalAmount()
            );
            ChatMessageProto.ChatMessage merchantChatMessage = buildChatMessage(
                    orderMain.getMerchantId(),
                    2, // 修复：商家类型为2（原代码错误用3）
                    merchantNotifyContent
            );
            sendViaSystemHandler(merchantChatMessage);
        }
        log.info("用户下单成功通知已发送，用户ID：{}，订单号：{}", userId, orderMain.getOrderNo());
    }

    // ------------------------------ 通用方法 ------------------------------
    private ChatMessageProto.ChatMessage buildChatMessage(Long toId, int toType, String content) {
        return ChatMessageProto.ChatMessage.newBuilder()
                .setMessageId(SnowflakeIdGenerator.getInstance().nextId())
                .setMsgType(SYSTEM_MSG_TYPE)
                .setMsgContent(content)
                .setFromType(4) // 发送方：系统
                .setFromId(0L)
                .setToType(toType)
                .setToId(toId)
                .setSendTime(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    private void sendViaSystemHandler(ChatMessageProto.ChatMessage msg) {
        MessageHandler systemHandler = messageHandlerFactory.getMessageHandler((long) SYSTEM_MSG_TYPE);
        if (systemHandler == null) {
            log.error("未找到系统消息处理器（msgType={}），消息发送失败", SYSTEM_MSG_TYPE);
            return;
        }
        try {
            systemHandler.handler(channelSessionManager, null, msg);
        } catch (Exception e) {
            log.error("系统消息处理器执行失败，消息ID：{}", msg.getMessageId(), e);
        }
    }
}