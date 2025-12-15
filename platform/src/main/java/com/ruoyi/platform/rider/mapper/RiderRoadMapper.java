package com.ruoyi.platform.rider.mapper;

import com.ruoyi.platform.domain.OrderDelivery;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.UserBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RiderRoadMapper {

    @Select("select * from order_delivery od left join order_main om on od.order_main_id = om.order_main_id where rider_id = #{riderBaseId} and delivery_status = 1 and om.order_status = 3")
    List<OrderDelivery> getPickupAddress(Long riderBaseId);

    @Select("select * from order_delivery od left join order_main om on od.order_main_id = om.order_main_id where rider_id = #{riderBaseId} and delivery_status = 2 and om.order_status = 4")
    List<OrderDelivery> getTargetAddress(Long riderBaseId);

    @Select("select user_id from order_main where order_main_id = #{orderMainId}")
    Long getUserBaseIdByOrderMainId(Long orderMainId);

    @Select("select * from user_base where user_base_id = #{userBaseId}")
    UserBase getUserBaseByUserBaseId(Long userBaseId);
}
