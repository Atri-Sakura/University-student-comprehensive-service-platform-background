package com.ruoyi.platform.platform.vo;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MerchantBaseVO {
    /** 商家唯一ID */
    private Long merchantBaseId;

    /** 商家名称 */
    @Excel(name = "商家名称")
    private String merchantName;

    private String description;

    /** 商家Logo URL */
    @Excel(name = "商家Logo URL")
    private String logo;

    /** 营业执照URL */
    @Excel(name = "营业执照URL")
    private String licenseImg;

    /** 商家评分 */
    @Excel(name = "商家评分")
    private BigDecimal rating;

    /** 月销量 */
    @Excel(name = "月销量")
    private Long monthSales;

    /** 审核状态：0-待审核 1-通过 2-拒绝 */
    @Excel(name = "审核状态：0-待审核 1-通过 2-拒绝")
    private Long auditStatus;

    /** 营业状态：0-停业 1-营业 */
    @Excel(name = "营业状态：0-停业 1-营业")
    private Long businessStatus;

    /** 店铺经度 */
    @Excel(name = "店铺经度")
    private BigDecimal longitude;

    /** 店铺纬度 */
    @Excel(name = "店铺纬度")
    private BigDecimal latitude;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;
}
