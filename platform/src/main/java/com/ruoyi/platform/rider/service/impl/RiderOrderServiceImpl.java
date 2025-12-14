package com.ruoyi.platform. rider.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.enums.OrderStatusEnum;
import com.ruoyi.platform.rider.domain.vo.RiderOrderListVO;
import com.ruoyi.platform.rider.mapper.RiderOrderMapper;
import com.ruoyi.platform.rider.service.IRiderOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<RiderOrderListVO> selectAvailableOrderList(OrderMain orderMain) {
        // 可接单的订单必须是状态 2-骑手待接单
        return riderOrderMapper.selectAvailableOrderList(orderMain);
    }

    @Override
    public List<RiderOrderListVO> selectRiderOrderList(Long riderId, OrderMain orderMain, String timeRange) {
        if (riderId == null) {
            log.error("查询骑手订单列表失败：骑手ID为空");
            throw new ServiceException("骑手ID不能为空");
        }

        log.info("查询骑手订单列表 - 骑手ID: {}, 时间范围: {}", riderId, timeRange);

        List<RiderOrderListVO> orders = riderOrderMapper.selectRiderOrderList(riderId, orderMain, timeRange);

        log.info("查询骑手订单列表成功 - 结果数量: {}", orders.size());

        return orders;
    }

    @Override
    public OrderMain selectRiderOrderById(Long riderId, Long orderMainId) {
        if (riderId == null) {
            throw new ServiceException("骑手ID不能为空");
        }

        if (orderMainId == null) {
            throw new ServiceException("订单ID不能为空");
        }

        log.info("查询骑手订单详情 - 骑手ID: {}, 订单ID:  {}", riderId, orderMainId);

        // 查询订单（不再在 SQL 层面限制骑手ID）
        OrderMain order = riderOrderMapper.selectRiderOrderById(riderId, orderMainId);

        if (order == null) {
            log.warn("订单不存在 - 订单ID: {}", orderMainId);
            throw new ServiceException("订单不存在或无权查看");
        }

        // 业务层权限校验：如果订单已被接单，必须是当前骑手
        if (order.getRiderId() != null && !order.getRiderId().equals(riderId)) {
            log.warn("权限不足 - 骑手 {} 尝试查看其他骑手的订单 {}", riderId, orderMainId);
            throw new ServiceException("订单不存在或无权查看");
        }

        log.info("查询骑手订单详情成功 - 订单状态: {}, 骑手ID: {}", order. getOrderStatus(), order.getRiderId());

        return order;
    }

    @Override
    public Map<String, Object> getOrderStatistics(Long riderId) {
        if (riderId == null) {
            throw new ServiceException("骑手ID不能为空");
        }

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("todayCount", riderOrderMapper.countByTimeRange(riderId, "today"));
        statistics.put("yesterdayCount", riderOrderMapper.countByTimeRange(riderId, "yesterday"));
        statistics.put("weekCount", riderOrderMapper.countByTimeRange(riderId, "week"));
        statistics.put("monthCount", riderOrderMapper.countByTimeRange(riderId, "month"));
        statistics.put("totalCount", riderOrderMapper.countByTimeRange(riderId, null));

        return statistics;
    }

    /**
     * 骑手异常报备
     * 将订单状态从 4-配送中 更新为 7-骑手异常报备
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean reportAbnormal(Long riderId, OrderMain orderMain) {
        if (riderId == null) {
            throw new ServiceException("骑手ID不能为空");
        }
        if (orderMain == null || orderMain.getOrderMainId() == null) {
            throw new ServiceException("订单信息不能为空");
        }
        if (orderMain.getCancelReason() == null || orderMain.getCancelReason().trim().isEmpty()) {
            throw new ServiceException("异常原因不能为空");
        }

        log.info("骑手异常报备 - 骑手ID: {}, 订单ID: {}, 原因: {}",
                riderId, orderMain. getOrderMainId(), orderMain.getCancelReason());

        // 更新订单状态为 7-骑手异常报备
        int result = riderOrderMapper.reportAbnormal(riderId, orderMain.getOrderMainId(), orderMain.getCancelReason());

        // 更新配送状态
        int result1 = riderOrderMapper.reportAbnormal1(riderId, orderMain.getOrderMainId());

        boolean success = (result + result1) > 1;

        if (success) {
            log.info("骑手异常报备成功 - 订单ID: {}", orderMain.getOrderMainId());
        } else {
            log.warn("骑手异常报备失败 - 订单ID: {}, 可能订单状态不是配送中", orderMain.getOrderMainId());
            throw new ServiceException("报备失败，订单状态必须是配送中");
        }

        return success;
    }
}