package com.ruoyi.platform.platform.vo;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RiderBaseVO {
    /** 骑手唯一ID */
    private Long riderBaseId;

    /** 骑手昵称 */
    @Excel(name = "骑手昵称")
    private String nickname;

    /** 头像URL */
    @Excel(name = "头像URL")
    private String avatar;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 身份证号(AES加密) */
    @Excel(name = "身份证号(AES加密)")
    private String idCard;

    /** 身份证正面照URL */
    @Excel(name = "身份证正面照URL")
    private String idCardFront;

    /** 身份证反面照URL */
    @Excel(name = "身份证反面照URL")
    private String idCardBack;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 审核状态：0-待审核 1-通过 2-拒绝 */
    @Excel(name = "审核状态：0-待审核 1-通过 2-拒绝")
    private Long auditStatus;

    /** 工作状态：0-下线 1-上线 2-忙碌 */
    @Excel(name = "工作状态：0-下线 1-上线 2-忙碌")
    private Long workStatus;

    /** 服务信用分 */
    @Excel(name = "服务信用分")
    private Long creditScore;

    /** 账号状态：0-禁用 1-正常 */
    @Excel(name = "账号状态：0-禁用 1-正常")
    private Long accountStatus;

}
