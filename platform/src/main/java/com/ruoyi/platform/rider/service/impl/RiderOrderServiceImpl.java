package com.ruoyi.platform.rider.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com. ruoyi.platform.domain. OrderMain;
import com.ruoyi.platform.rider.mapper.RiderOrderMapper;
import com.ruoyi. platform.rider.service.IRiderOrderService;
import org. slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 骑手订单服务实现类
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Service
public class RiderOrderServiceImpl implements IRiderOrderService {

    private static final Logger log = LoggerFactory.getLogger(RiderOrderServiceImpl.class);

    @Autowired
    private RiderOrderMapper riderOrderMapper;

    /**
     * 查询可接单的订单列表（待取货状态且未被接单）
     *
     * @param orderMain 查询条件
     * @return 订单列表
     */
    @Override
    public List<OrderMain> selectAvailableOrderList(OrderMain orderMain) {
        return riderOrderMapper.selectAvailableOrderList(orderMain);
    }

    /**
     * 查询骑手自己的订单列表
     *
     * @param riderId 骑手ID
     * @param orderMain 查询条件
     * @return 订单列表
     */
    @Override
    public List<OrderMain> selectRiderOrderList(Long riderId, OrderMain orderMain) {
        // 验证骑手ID不能为空
        if (riderId == null) {
            log.error("查询骑手订单列表失败：骑手ID为空");
            throw new ServiceException("骑手ID不能为空");
        }

        log.info("查询骑手订单列表 - 骑手ID: {}, 查询条件: {}", riderId, orderMain);

        // 直接传递两个参数，不再使用 params
        List<OrderMain> orders = riderOrderMapper.selectRiderOrderList(riderId, orderMain);

        log.info("查询骑手订单列表成功 - 骑手ID: {}, 结果数量: {}", riderId, orders.size());

        return orders;
    }

    /**
     * 查询骑手订单详情
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 订单详情
     */
    @Override
    public OrderMain selectRiderOrderById(Long riderId, Long orderMainId) {
        if (riderId == null) {
            log.error("查询骑手订单详情失败：骑手ID为空");
            throw new ServiceException("骑手ID不能为空");
        }

        if (orderMainId == null) {
            log.error("查询骑手订单详情失败：订单ID为空");
            throw new ServiceException("订单ID不能为空");
        }

        log.info("查询骑手订单详情 - 骑手ID: {}, 订单ID: {}", riderId, orderMainId);

        OrderMain order = riderOrderMapper.selectRiderOrderById(riderId, orderMainId);

        if (order == null) {
            log.warn("订单不存在或无权查看 - 骑手ID: {}, 订单ID: {}", riderId, orderMainId);
            throw new ServiceException("订单不存在或无权查看");
        }

        log.info("查询骑手订单详情成功 - 订单号: {}", order.getOrderNo());

        return order;
    }
}