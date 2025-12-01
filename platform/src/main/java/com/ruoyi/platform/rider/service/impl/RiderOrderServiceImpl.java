package com.ruoyi.platform.rider.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.rider.domain.vo.RiderOrderListVO;
import com.ruoyi.platform.rider.mapper.RiderOrderMapper;
import com.ruoyi.platform.rider.service.IRiderOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        log.info("查询骑手订单列表成功 - 结果数量: {}", orders. size());

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

        log.info("查询骑手订单详情 - 骑手ID: {}, 订单ID: {}", riderId, orderMainId);

        OrderMain order = riderOrderMapper.selectRiderOrderById(riderId, orderMainId);

        if (order == null) {
            throw new ServiceException("订单不存在或无权查看");
        }

        return order;
    }

    @Override
    public Map<String, Object> getOrderStatistics(Long riderId) {
        if (riderId == null) {
            throw new ServiceException("骑手ID不能为空");
        }

        Map<String, Object> statistics = new HashMap<>();
        statistics. put("todayCount", riderOrderMapper.countByTimeRange(riderId, "today"));
        statistics.put("yesterdayCount", riderOrderMapper.countByTimeRange(riderId, "yesterday"));
        statistics. put("weekCount", riderOrderMapper.countByTimeRange(riderId, "week"));
        statistics.put("monthCount", riderOrderMapper.countByTimeRange(riderId, "month"));
        statistics.put("totalCount", riderOrderMapper.countByTimeRange(riderId, null));

        return statistics;
    }
}