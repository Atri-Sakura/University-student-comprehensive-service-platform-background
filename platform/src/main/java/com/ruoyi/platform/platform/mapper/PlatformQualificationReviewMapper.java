package com.ruoyi.platform.platform.mapper;

import com.ruoyi.platform.domain.RiderBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PlatformQualificationReviewMapper {
    @Select("select * from rider_base")
    RiderBase getAllRiderQualificationStatus();

    @Update("update rider_base set audit_status = #{status} where rider_base_id = #{riderId}")
    int setRiderQualificationStatus(Integer status, Integer riderId);

    @Update("update merchant_base set audit_status = #{status} where merchant_base_id = #{merchantId}")
    int setMerchantQualificationStatus(Integer status, Long merchantId);
}
