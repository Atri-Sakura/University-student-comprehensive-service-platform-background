package com.ruoyi.platform.service. impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.platform.domain.MerchantWallet;
import com.ruoyi.platform.domain.MerchantWalletFlow;
import com.ruoyi.platform.domain.enums.OrderStatusEnum;
import com.ruoyi.platform.merchant.mapper.MerchantWalletFlowMapper;
import com.ruoyi.platform.mapper.MerchantWalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.OrderMainMapper;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.service.IOrderMainService;
import org.springframework.transaction.annotation. Transactional;

/**
 * 订单主Service业务层处理
 */
@Service
public class OrderMainServiceImpl implements IOrderMainService
{
    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private MerchantWalletMapper merchantWalletMapper;

    @Autowired
    private MerchantWalletFlowMapper merchantWalletFlowMapper;

    /**
     * 订单完成处理(增加商家余额并记录流水)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int handleOrderComplete(Long orderMainId) {
        // 1. 查询订单详情
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 校验订单状态 - 使用枚举
        if (OrderStatusEnum.COMPLETED.getCode().equals(order.getOrderStatus())) {
            throw new ServiceException("订单已完成,请勿重复操作");
        }

        if (order.getPayStatus() != 1) {
            throw new ServiceException("订单未支付,无法完成");
        }

        // 3. 查询商家钱包(加锁)
        MerchantWallet wallet = merchantWalletMapper.selectWalletForUpdate(order.getMerchantId());
        if (wallet == null) {
            throw new ServiceException("商家钱包不存在");
        }

        // 4. 计算商家收入(商品金额,不含配送费)
        BigDecimal merchantIncome = order.getGoodsAmount();
        if (merchantIncome == null || merchantIncome.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("商品金额异常");
        }

        // 5. 计算变动后余额
        BigDecimal balanceAfter = wallet.getBalance().add(merchantIncome);

        // 6. 更新商家钱包余额
        int updateCount = merchantWalletMapper.increaseBalance(order.getMerchantId(), merchantIncome);
        if (updateCount == 0) {
            throw new ServiceException("钱包余额更新失败");
        }

        // 7. 插入流水记录
        MerchantWalletFlow flow = new MerchantWalletFlow();
        flow.setMerchantBaseId(order.getMerchantId());
        flow.setOrderMainId(orderMainId);
        flow.setFlowType("INCOME");
        flow.setFlowAmount(merchantIncome);
        flow.setGoodsAmount(order.getGoodsAmount());
        flow.setBalanceAfter(balanceAfter);
        flow.setDescription("订单收入,订单号:" + order.getOrderNo());
        flow.setCreateTime(new Date());

        merchantWalletFlowMapper.insertMerchantWalletFlow(flow);

        // 8. 更新订单状态为已完成 - 使用枚举
        order.setOrderStatus(OrderStatusEnum.COMPLETED.getCode());
        order.setCompleteTime(new Date());
        return orderMainMapper.updateOrderMain(order);
    }

    /**
     * 订单退款处理(扣减商家余额并记录流水)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int handleOrderRefund(Long orderMainId, String refundReason) {
        // 1. 查询订单详情
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 校验订单状态
        if (order.getPayStatus() == 3) {
            throw new ServiceException("订单已退款,请勿重复操作");
        }

        if (order.getPayStatus() != 1) {
            throw new ServiceException("订单未支付,无法退款");
        }

        // 3. 查询商家钱包(加锁)
        MerchantWallet wallet = merchantWalletMapper.selectWalletForUpdate(order.getMerchantId());
        if (wallet == null) {
            throw new ServiceException("商家钱包不存在");
        }

        // 4. 计算退款金额(商品金额)
        BigDecimal refundAmount = order.getGoodsAmount();
        if (refundAmount == null || refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("退款金额异常");
        }

        // 5. 校验余额是否充足
        if (wallet.getBalance().compareTo(refundAmount) < 0) {
            throw new ServiceException("商家余额不足,无法退款");
        }

        // 6. 计算变动后余额
        BigDecimal balanceAfter = wallet.getBalance().subtract(refundAmount);

        // 7. 扣减商家钱包余额
        int updateCount = merchantWalletMapper.increaseBalance(
                order.getMerchantId(),
                refundAmount. negate()
        );
        if (updateCount == 0) {
            throw new ServiceException("钱包余额更新失败");
        }

        // 8. 插入流水记录(负数)
        MerchantWalletFlow flow = new MerchantWalletFlow();
        flow.setMerchantBaseId(order.getMerchantId());
        flow.setOrderMainId(orderMainId);
        flow.setFlowType("REFUND");
        flow.setFlowAmount(refundAmount. negate());
        flow.setGoodsAmount(order.getGoodsAmount());
        flow.setBalanceAfter(balanceAfter);
        flow.setDescription("订单退款,订单号:" + order.getOrderNo() + ",原因:" + refundReason);
        flow.setCreateTime(new Date());

        merchantWalletFlowMapper.insertMerchantWalletFlow(flow);

        // 9. 更新订单状态为已取消 - 使用枚举
        order.setPayStatus(3L);
        order.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCancelReason(refundReason);
        return orderMainMapper.updateOrderMain(order);
    }


    @Override
    public OrderMain selectOrderMainByOrderMainId(Long orderMainId)
    {
        return orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
    }

    @Override
    public List<OrderMain> selectOrderMainList(OrderMain orderMain)
    {
        return orderMainMapper.selectOrderMainList(orderMain);
    }

    @Override
    public int insertOrderMain(OrderMain orderMain)
    {
        orderMain.setCreateTime(DateUtils.getNowDate());
        return orderMainMapper.insertOrderMain(orderMain);
    }

    @Override
    public int updateOrderMain(OrderMain orderMain)
    {
        orderMain.setUpdateTime(DateUtils.getNowDate());
        return orderMainMapper.updateOrderMain(orderMain);
    }

    @Override
    public int deleteOrderMainByOrderMainIds(Long[] orderMainIds)
    {
        return orderMainMapper.deleteOrderMainByOrderMainIds(orderMainIds);
    }

    @Override
    public int deleteOrderMainByOrderMainId(Long orderMainId)
    {
        return orderMainMapper.deleteOrderMainByOrderMainId(orderMainId);
    }

    @Override
    public Map<String, Object> selectMerchantTodayIncome(Long merchantId, Date startTime, Date endTime)
    {
        return orderMainMapper.selectMerchantTodayIncome(merchantId, startTime, endTime);
    }

    @Override
    public BigDecimal selectMerchantTodayRefund(Long merchantId, Date startTime, Date endTime)
    {
        return orderMainMapper.selectMerchantTodayRefund(merchantId, startTime, endTime);
    }

    /**
     * 根据orderNo查询订单
     * @param orderNo
     * @return
     */
    @Override
    public OrderMain selectByOrderNo(String orderNo) {
        return orderMainMapper.selectByOrderNo(orderNo);
    }

    /**
     * 统计商家近30天销量（核心实现）
     * @param map key: OrderMain 订单主信息, value: 对应商品购买数量
     * @return 近30天有效销量总和
     */
    @Override
    public int countMonthSaleCounts(HashMap<OrderMain, Long> map) {
        // 1. 计算30天前的时间（当前时间往前推30天）
        Date thirtyDaysAgo = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30));

        // 2. 流式计算：过滤近30天完成的订单 + 累加销量
        return map.entrySet().stream()
                // 过滤条件：订单非空 + 完成时间非空 + 完成时间在近30天内
                .filter(entry -> {
                    OrderMain orderMain = entry.getKey();
                    if (orderMain == null) {
                        return false;
                    }
                    Date completeTime = orderMain.getCompleteTime();
                    return completeTime != null && completeTime.after(thirtyDaysAgo);
                })
                // 空值防护：数量为null时按0处理
                .mapToInt(entry -> entry.getValue() == null ? 0 : entry.getValue().intValue())
                // 累加所有符合条件的销量
                .sum();
    }
}