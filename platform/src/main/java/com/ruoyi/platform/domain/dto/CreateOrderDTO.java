package com.ruoyi.platform.domain.dto;

import lombok.Data;

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

    private Long deliverAddressId;

    /** 商家ID */
    private Long merchantId;

    /** 商家名称（冗余） */
    private String merchantName;

    /** 订单备注 */
    private String remark;

    /** 订单商品明细列表 */
    private List<OrderItemDTO> items;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getDeliverAddressId() {
        return deliverAddressId;
    }

    public void setDeliverAddressId(Long deliverAddressId) {
        this.deliverAddressId = deliverAddressId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CreateOrderDTO{" +
                "userId=" + userId +
                ", userNickname='" + userNickname + '\'' +
                ", merchantId=" + merchantId +
                ", merchantName='" + merchantName + '\'' +
                ", deliverAddressId=" + deliverAddressId +
                ", remark='" + remark + '\'' +
                ", items=" + items +
                '}';
    }
}