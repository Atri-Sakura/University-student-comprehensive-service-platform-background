package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户行为记录对象 user_behavior_log
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class UserBehaviorLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一ID */
    private Long userBehaviorLogId;

    /** 所属用户ID（关联user_base.user_base_id） */
    @Excel(name = "所属用户ID", readConverterExp = "关=联user_base.user_base_id")
    private Long userBaseId;

    /** 行为类型：1-浏览商品 2-收藏商品 3-加入购物车 4-下单购买 5-取消订单 6-评价商品 */
    @Excel(name = "行为类型：1-浏览商品 2-收藏商品 3-加入购物车 4-下单购买 5-取消订单 6-评价商品")
    private Long behaviorType;

    /** 行为对象ID（如商品ID=123/商家ID=45） */
    @Excel(name = "行为对象ID", readConverterExp = "如=商品ID=123/商家ID=45")
    private Long targetId;

    /** 对象类型：1-商品 2-商家 3-订单 4-活动 */
    @Excel(name = "对象类型：1-商品 2-商家 3-订单 4-活动")
    private Long targetType;

    /** 对象名称（冗余，如"珍珠奶茶"） */
    @Excel(name = "对象名称", readConverterExp = "冗=余，如珍珠奶茶")
    private String targetName;

    /** 行为发生时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "行为发生时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date behaviorTime;

    /** 行为设备（如APP/小程序/H5） */
    @Excel(name = "行为设备", readConverterExp = "如=APP/小程序/H5")
    private String device;

    /** 行为场景（如HOME-首页/SEARCH-搜索页） */
    @Excel(name = "行为场景", readConverterExp = "如=HOME-首页/SEARCH-搜索页")
    private String scene;

    /** 停留时长（秒，仅behavior_type=1时有效） */
    @Excel(name = "停留时长", readConverterExp = "秒=，仅behavior_type=1时有效")
    private Long duration;

    /** 额外信息（如搜索关键词"平价奶茶"） */
    @Excel(name = "额外信息", readConverterExp = "如=搜索关键词平价奶茶")
    private String extra;

    public void setUserBehaviorLogId(Long userBehaviorLogId) 
    {
        this.userBehaviorLogId = userBehaviorLogId;
    }

    public Long getUserBehaviorLogId() 
    {
        return userBehaviorLogId;
    }

    public void setUserBaseId(Long userBaseId) 
    {
        this.userBaseId = userBaseId;
    }

    public Long getUserBaseId() 
    {
        return userBaseId;
    }

    public void setBehaviorType(Long behaviorType) 
    {
        this.behaviorType = behaviorType;
    }

    public Long getBehaviorType() 
    {
        return behaviorType;
    }

    public void setTargetId(Long targetId) 
    {
        this.targetId = targetId;
    }

    public Long getTargetId() 
    {
        return targetId;
    }

    public void setTargetType(Long targetType) 
    {
        this.targetType = targetType;
    }

    public Long getTargetType() 
    {
        return targetType;
    }

    public void setTargetName(String targetName) 
    {
        this.targetName = targetName;
    }

    public String getTargetName() 
    {
        return targetName;
    }

    public void setBehaviorTime(Date behaviorTime) 
    {
        this.behaviorTime = behaviorTime;
    }

    public Date getBehaviorTime() 
    {
        return behaviorTime;
    }

    public void setDevice(String device) 
    {
        this.device = device;
    }

    public String getDevice() 
    {
        return device;
    }

    public void setScene(String scene) 
    {
        this.scene = scene;
    }

    public String getScene() 
    {
        return scene;
    }

    public void setDuration(Long duration) 
    {
        this.duration = duration;
    }

    public Long getDuration() 
    {
        return duration;
    }

    public void setExtra(String extra) 
    {
        this.extra = extra;
    }

    public String getExtra() 
    {
        return extra;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userBehaviorLogId", getUserBehaviorLogId())
            .append("userBaseId", getUserBaseId())
            .append("behaviorType", getBehaviorType())
            .append("targetId", getTargetId())
            .append("targetType", getTargetType())
            .append("targetName", getTargetName())
            .append("behaviorTime", getBehaviorTime())
            .append("device", getDevice())
            .append("scene", getScene())
            .append("duration", getDuration())
            .append("extra", getExtra())
            .toString();
    }
}
