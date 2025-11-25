package com.ruoyi.platform.rider.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RiderQualificationCertificationMapper {
    @Update("update rider_base set id_card_front = #{imgUrl},id_card_back = #{imgUrl1},id_card = #{idCardNumber} where rider_base_id = #{riderBaseId}")
    int addIdCardImage(String imgUrl,String imgUrl1,Long idCardNumber , Long riderBaseId);
}
