package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.OrderTakeoutDetail;
import com.ruoyi.platform.service.IOrderMainService;
import com.ruoyi.platform.service.IOrderTakeoutDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.merchant.mapper.MerchantGoodsInfoMapper;
import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.merchant.service.IMerchantGoodsInfoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品基础信息Service实现
 */
@Service
public class MerchantGoodsInfoServiceImpl implements IMerchantGoodsInfoService {

    @Autowired
    private MerchantGoodsInfoMapper merchantGoodsInfoMapper;

    @Autowired
    private IOrderTakeoutDetailService orderTakeoutDetailService;

    @Autowired
    private IOrderMainService orderMainService;

    @Override
    public MerchantGoods selectMerchantGoodsByMerchantGoodsId(Long merchantGoodsId) {
        return merchantGoodsInfoMapper.selectMerchantGoodsByMerchantGoodsId(merchantGoodsId);
    }

    @Override
    public int updateMerchantGoods(MerchantGoods merchantGoods) {
        merchantGoods.setUpdateTime(DateUtils.getNowDate());
        return merchantGoodsInfoMapper.updateMerchantGoods(merchantGoods);
    }

    @Override
    public List<MerchantGoods> selectMerchantGoodsListForCustomer(Long merchantBaseId, long status, Integer pageNum, Integer pageSize) {
        MerchantGoods query = new MerchantGoods();
        query.setMerchantBaseId(merchantBaseId);
        query.setStatus(status); // 只查上架商品
        // pageNum、pageSize 可以不处理，或者用 Mybatis分页插件。
        return merchantGoodsInfoMapper.selectMerchantGoodsList(query);
    }

    /**
     * 获取商品近30天销量
     * @param merchantGoodsId 商家商品ID
     * @return 近30天销量总和
     */
    @Override
    public int getMonthlySaleCounts(Long merchantGoodsId) {
        // 1. 参数校验
        if (merchantGoodsId == null || merchantGoodsId <= 0) {
            return 0;
        }

        // 2. 查询该商品的所有订单明细
        OrderTakeoutDetail queryDetail = new OrderTakeoutDetail();
        queryDetail.setGoodsId(merchantGoodsId);
        List<OrderTakeoutDetail> detailList = orderTakeoutDetailService.selectOrderTakeoutDetailList(queryDetail);

        // 3. 无明细直接返回0
        if (detailList == null || detailList.isEmpty()) {
            return 0;
        }

        // 4. 构建订单-数量映射（兼容原有逻辑，避免修改接口）
        Map<OrderMain, Long> map = new HashMap<>();
        for (OrderTakeoutDetail detail : detailList) {
            // 跳过无效订单ID
            if (detail.getOrderMainId() == null) {
                continue;
            }
            // 查询订单主信息
            OrderMain orderMain = orderMainService.selectOrderMainByOrderMainId(detail.getOrderMainId());
            // 订单非空时才放入Map（避免空指针）
            if (orderMain != null) {
                map.put(orderMain, detail.getQuantity());
            }
        }

        // 5. 调用订单服务统计近30天销量
        return orderMainService.countMonthSaleCounts((HashMap<OrderMain, Long>) map);
    }
}