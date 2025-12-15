package com.ruoyi.platform.rider.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.OrderDelivery;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.UserBase;
import com.ruoyi.platform.rider.mapper.RiderRoadMapper;
import org.apache.poi.hpsf.Decimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/riderRoad")
public class RiderRoadController {
    @Autowired
    private RiderRoadMapper riderRoadMapper;
    //骑手导航逻辑
    @GetMapping("/pickup")
    public AjaxResult getPickupAddress(){
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        if (riderBaseId == null) {
            return AjaxResult.error("请登录");
        }

        // 1. 核心前提：确保原始订单列表本身是有序的（数据库查询建议加排序，比如按订单创建时间/配送时间）
        List<OrderDelivery> orderDeliveryList = riderRoadMapper.getTargetAddress(riderBaseId);
        if (CollectionUtils.isEmpty(orderDeliveryList)) {
            return AjaxResult.success("暂无配送订单数据")
                    .put("目的地经纬度", new ArrayList<>())
                    .put("配送时间", new ArrayList<>())
                    .put("用户手机号", new ArrayList<>());
        }

        // 2. 经纬度列表：串行流处理，保持原始顺序
        List<Map<BigDecimal, BigDecimal>> latLngList = orderDeliveryList.stream()
                // 关键：使用stream()（串行流）而非parallelStream()（并行流），parallelStream会打乱顺序
                .map(orderDelivery -> {
                    Map<BigDecimal, BigDecimal> latLngMap = new HashMap<>();
                    BigDecimal latitude = orderDelivery.getActualPickLatitude();
                    BigDecimal longitude = orderDelivery.getActualPickLongitude();
                    if (latitude != null && longitude != null) {
                        latLngMap.put(latitude, longitude);
                    }
                    return latLngMap;
                })
                .collect(Collectors.toList()); // 有序收集，保持原始顺序

        // 3. 配送时间列表：同样串行流，顺序一致
        List<Date> deliveryTimeList = orderDeliveryList.stream()
                .map(OrderDelivery::getDeliverTime) // 简化写法，等价于orderDelivery -> orderDelivery.getDeliverTime()
                .collect(Collectors.toList());

        // 4. 用户手机号列表：增强for循环按原始顺序遍历，保证顺序一致
        List<Map<String, String>> phoneList = new ArrayList<>();
        for (OrderDelivery orderDelivery : orderDeliveryList) { // 按orderDeliveryList的顺序逐个遍历
            Map<String, String> map = new HashMap<>();
            // 空指针防护：避免查询结果为null导致报错，不影响顺序
            Long orderMainId = orderDelivery.getOrderMainId();
            if (orderMainId == null) {
                phoneList.add(map); // 空map占位，保证列表长度和顺序一致
                continue;
            }
            Long userBaseId = riderRoadMapper.getUserBaseIdByOrderMainId(orderMainId);
            if (userBaseId == null) {
                phoneList.add(map);
                continue;
            }
            UserBase userBase = riderRoadMapper.getUserBaseByUserBaseId(userBaseId);
            if (userBase != null) {
                map.put("nickname", userBase.getNickname()); // 建议用固定key，避免后续解析混乱
                map.put("phone", userBase.getPhone());
            }
            phoneList.add(map); // 按遍历顺序添加，和原始列表一一对应
        }

        return AjaxResult.success("获取订单信息成功")
                .put("目的地经纬度", latLngList)
                .put("配送时间", deliveryTimeList)
                .put("用户信息", phoneList);
    }
    @GetMapping("/target")
    public AjaxResult getTargetAddress(){
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        if (riderBaseId == null) {
            return AjaxResult.error("请登录");
        }

        // 1. 核心前提：确保原始订单列表本身是有序的（数据库查询建议加排序，比如按订单创建时间/配送时间）
        List<OrderDelivery> orderDeliveryList = riderRoadMapper.getTargetAddress(riderBaseId);
        if (CollectionUtils.isEmpty(orderDeliveryList)) {
            return AjaxResult.success("暂无配送订单数据")
                    .put("目的地经纬度", new ArrayList<>())
                    .put("配送时间", new ArrayList<>())
                    .put("用户手机号", new ArrayList<>());
        }

        // 2. 经纬度列表：串行流处理，保持原始顺序
        List<Map<BigDecimal, BigDecimal>> latLngList = orderDeliveryList.stream()
                // 关键：使用stream()（串行流）而非parallelStream()（并行流），parallelStream会打乱顺序
                .map(orderDelivery -> {
                    Map<BigDecimal, BigDecimal> latLngMap = new HashMap<>();
                    BigDecimal latitude = orderDelivery.getActualDeliverLatitude();
                    BigDecimal longitude = orderDelivery.getActualDeliverLongitude();
                    if (latitude != null && longitude != null) {
                        latLngMap.put(latitude, longitude);
                    }
                    return latLngMap;
                })
                .collect(Collectors.toList()); // 有序收集，保持原始顺序

        // 3. 配送时间列表：同样串行流，顺序一致
        List<Date> deliveryTimeList = orderDeliveryList.stream()
                .map(OrderDelivery::getDeliverTime) // 简化写法，等价于orderDelivery -> orderDelivery.getDeliverTime()
                .collect(Collectors.toList());

        // 4. 用户手机号列表：增强for循环按原始顺序遍历，保证顺序一致
        List<Map<String, String>> phoneList = new ArrayList<>();
        for (OrderDelivery orderDelivery : orderDeliveryList) { // 按orderDeliveryList的顺序逐个遍历
            Map<String, String> map = new HashMap<>();
            // 空指针防护：避免查询结果为null导致报错，不影响顺序
            Long orderMainId = orderDelivery.getOrderMainId();
            if (orderMainId == null) {
                phoneList.add(map); // 空map占位，保证列表长度和顺序一致
                continue;
            }
            Long userBaseId = riderRoadMapper.getUserBaseIdByOrderMainId(orderMainId);
            if (userBaseId == null) {
                phoneList.add(map);
                continue;
            }
            UserBase userBase = riderRoadMapper.getUserBaseByUserBaseId(userBaseId);
            if (userBase != null) {
                map.put("nickname", userBase.getNickname()); // 建议用固定key，避免后续解析混乱
                map.put("phone", userBase.getPhone());
            }
            phoneList.add(map); // 按遍历顺序添加，和原始列表一一对应
        }

        return AjaxResult.success("获取订单信息成功")
                .put("目的地经纬度", latLngList)
                .put("配送时间", deliveryTimeList)
                .put("用户信息", phoneList);
    }

}
