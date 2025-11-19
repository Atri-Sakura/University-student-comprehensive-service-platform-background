package com.ruoyi.platform.domain.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单DTO
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Data
public class CreateOrderDTO {

    /** 用户ID（从SecurityUtils获取，前端不传） */
    private Long userId;

    /** 用户昵称（从数据库获取，前端不传） */
    private String userNickname;

    /** 订单类型：1-外卖单 2-跑腿单 3-二手交易单 */
    private Long orderType;

    /** 商家ID */
    private Long merchantId;

    /** 商家名称（冗余） */
    private String merchantName;

    /** 送货地址ID */
    private Long deliverAddressId;

    /** 送货地址文本 */
    private String deliverAddress;

    /** 收货联系人 */
    private String deliverContact;

    /** 收货电话 */
    private String deliverPhone;

    /** 送货经度 */
    private BigDecimal deliverLongitude;

    /** 送货纬度 */
    private BigDecimal deliverLatitude;

    /** 订单备注 */
    private String remark;

    /** 订单商品明细列表 */
    private List<OrderItemDTO> items;
}