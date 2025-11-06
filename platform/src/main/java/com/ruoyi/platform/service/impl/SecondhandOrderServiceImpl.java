package com.ruoyi.platform.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.chat.utils.SnowflakeIdGenerator;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.OrderSecondhandDetail;
import com.ruoyi.platform.domain.SecondhandGoods;
import com.ruoyi.platform.domain.dto.SecondhandOrderCreatDTO;
import com.ruoyi.platform.mapper.OrderMainMapper;
import com.ruoyi.platform.mapper.OrderSecondhandDetailMapper;
import com.ruoyi.platform.mapper.SecondhandGoodsMapper;
import com.ruoyi.platform.service.ISecondhandOrderService;
import com.ruoyi.platform.utils.OrderNoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
public class SecondhandOrderServiceImpl implements ISecondhandOrderService {

    @Autowired
    private SecondhandGoodsMapper secondhandGoodsMapper;

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private OrderSecondhandDetailMapper orderSecondhandDetailMapper;

    /**
     * 确认订单
     * @param orderNo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean confirmOrder(String orderNo) {
        // 获取订单信息
        OrderMain order = orderMainMapper.selectOrderByOrderNo(orderNo);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 校验订单状态（只允许“待线下交付”或“待收货”的订单确认）
        if (!Objects.equals(order.getOrderStatus(), 2L)) {
            throw new ServiceException("当前订单状态无法确认收货");
        }

        // 查询订单明细获取商品ID
        Long goodsId = orderSecondhandDetailMapper.selectGoodsIdByOrderMainId(order.getOrderMainId());
        if (goodsId == null) {
            throw new ServiceException("未找到该订单对应的商品信息");
        }

        // 更新订单状态 -> 已完成，记录完成时间
        int rows1 = orderMainMapper.updateOrderComplete(orderNo, LocalDateTime.now());

        // 更新商品状态 -> 已售出（status=2）
        int rows2 = secondhandGoodsMapper.updateSecondhandGoodsStatus(goodsId, 2L);

        return rows1 > 0 && rows2 > 0;
    }

    /**
     * 模拟订单支付
     * @param orderNo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean payOrder(String orderNo) {
        // 查询订单信息
        OrderMain order = orderMainMapper.selectOrderByOrderNo(orderNo);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 校验状态（只有待支付状态才允许支付）
        if (!Objects.equals(order.getPayStatus(), 0L)) {
            throw new ServiceException("当前订单状态无法支付");
        }

        boolean payResult = mockOnlinePay(order.getPayType(), order.getPayAmount());

        if (!payResult) {
            // 模拟支付失败 → 标记为无效订单
            orderMainMapper.updateOrderStatus(orderNo, 5L, 0L, "支付失败");
            return false;
        }
        // 支付成功 → 更新订单支付状态
        orderMainMapper.updatePayStatus(orderNo, 1L, 2L, LocalDateTime.now());

        // 查询二手订单明细获取 goodsId
        Long goodsId = orderSecondhandDetailMapper.selectGoodsIdByOrderMainId(order.getOrderMainId());
        if (goodsId == null) {
            throw new ServiceException("未找到该订单对应的商品信息");
        }

        // 更新商品状态为“预定中”(status=3)
        secondhandGoodsMapper.updateSecondhandGoodsStatus(goodsId, 3L);

        return true;
    }

    /**
     * 模拟支付行为（90% 成功）
     */
    private boolean mockOnlinePay(Long payType, BigDecimal amount) {
        System.out.println("模拟第三方支付 -> payType=" + payType + ", 金额=" + amount);
        return new Random().nextInt(10) != 0; // 90% 成功率
    }
    /**
     * 创建订单
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createSecondhandOrder(SecondhandOrderCreatDTO dto){
        //这需要加一个检查，用户是否存在
        Long userId = SecurityUtils.getUserBaseId();

        //查询目标二手商品并使用行锁锁定
        SecondhandGoods goods = secondhandGoodsMapper.selectSecondhandGoodsForUpdate(dto.getGoodsId());

        if(goods == null || goods.getStatus() != 1){
            throw new ServiceException("商品不存在或已下架");
        }

        if(Objects.equals(goods.getUserBaseId(), userId)){
            throw new ServiceException("不能购买自己的商品");
        }
        Long orderMainId = SnowflakeIdGenerator.getInstance().nextId();
        String orderNo = OrderNoUtils.generate(userId);

        boolean isOfflinePay = (dto.getPayType()==4L);

        OrderMain order = new OrderMain();
        order.setOrderMainId(orderMainId);
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setUserNickname(dto.getReceiverName());
        order.setOrderType(3L);
        order.setTotalAmount(goods.getPrice());
        order.setPayAmount(goods.getPrice());
        order.setPayType(dto.getPayType());
        order.setRemark(dto.getRemark());
        order.setDeliverAddress(dto.getTradePlace());
        order.setDeliverContact(dto.getReceiverName());
        order.setDeliverPhone(dto.getReceiverPhone());
        //由于数据库字段定义限制，所以这里先写死 0默认取货与收货地址相同
        order.setPickAddressId(0L); // 没有“取货地址”表就先写 0，务必不是 null
        order.setPickAddress(dto.getTradePlace());
        order.setPickContact(dto.getReceiverName());
        order.setPickPhone(dto.getReceiverPhone());


        order.setOrderStatus(isOfflinePay ? 2L : 1L); // 面付=待线下交付，线上=待支付
        order.setPayStatus(0L); // 未支付
        orderMainMapper.insertSecondhandOrderMain(order);
        Long orderSecondhandDetailId = SnowflakeIdGenerator.getInstance().nextId();
        OrderSecondhandDetail detail = new OrderSecondhandDetail();
        detail.setOrderSecondhandDetailId(orderSecondhandDetailId);
        detail.setOrderMainId(order.getOrderMainId());
        detail.setGoodsId(goods.getSecondhandGoodsId());
        detail.setGoodsName(goods.getGoodsName());
        detail.setSellerId(goods.getUserBaseId());
//        detail.setSellerNickname(goods.getSellerNickname());
        detail.setSellWay(isOfflinePay ? 2L : 1L);
        detail.setDepositAmount(BigDecimal.ZERO);
        orderSecondhandDetailMapper.insertOrderSecondhandDetail(detail);

        // 面付立即锁定商品
        if (isOfflinePay) {
            secondhandGoodsMapper.updateSecondhandGoodsStatus(dto.getGoodsId(), 3L);
        }

        return orderNo;
    }
}
