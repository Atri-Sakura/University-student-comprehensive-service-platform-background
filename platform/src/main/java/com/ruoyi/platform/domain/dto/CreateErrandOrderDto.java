package com.ruoyi.platform.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CreateErrandOrderDto {

    /** 用户ID（从SecurityUtils获取，前端不传） */
    private Long userId;

    /** 用户昵称（从数据库获取，前端不传） */
    private String userNickname;

    /** 订单类型：1-外卖单 2-跑腿单 3-二手交易单 */
    private Long orderType;

    /** 发送者ID */
    private Long senderId;

    /** 商家名称（冗余） */
    private String merchantName;

    @NotBlank(message = "取货省份不能为空")
    private String pickProvince;

    @NotBlank(message = "取货城市不能为空")
    private String pickCity;

    @NotBlank(message = "取货区县不能为空")
    private String pickDistrict;

    @NotBlank(message = "取货详细地址不能为空")
    private String pickDetailAddress;

    /** 送货地址ID */
    @NotNull(message = "收货地址ID不能为空")
    private Long deliverAddressId;

    /** 送货地址文本 */
    private String deliverAddress;

    /** 商品价格  */
    private BigDecimal goodsPrice;

    /** 收货联系人 */
    private String deliverContact;

    /** 收货电话 */
    private String deliverPhone;

    /** 运费 */
    private BigDecimal deliverAmount;

    /** 订单备注 */
    private String remark;

    /** 预期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectTime;


    /** 商品描述 */
    private String goodsDesc;


}
