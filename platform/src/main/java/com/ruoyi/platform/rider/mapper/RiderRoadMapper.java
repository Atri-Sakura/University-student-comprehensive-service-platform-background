package com.ruoyi.platform.rider.mapper;

import com.ruoyi.platform.domain.OrderDelivery;
import com.ruoyi.platform.domain.UserBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RiderRoadMapper {

    @Select("select * from order_delivery where rider_id = #{riderBaseId} and delivery_status = 1")
    List<OrderDelivery> getgetPickupAddress(Long riderBaseId);

    @Select("select * from order_delivery where rider_id = #{riderBaseId} and delivery_status = 2")
    List<OrderDelivery> getTargetAddress(Long riderBaseId);

    @Select("select user_id from order_main where order_main_id = #{orderMainId}")
    Long getUserBaseIdByOrderMainId(Long orderMainId);

    @Select("select * from user_base where user_base_id = #{userBaseId}")
    UserBase getUserBaseByUserBaseId(Long userBaseId);
}
