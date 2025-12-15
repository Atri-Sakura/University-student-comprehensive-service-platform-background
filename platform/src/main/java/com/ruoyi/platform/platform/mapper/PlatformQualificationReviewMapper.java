package com.ruoyi.platform.platform.mapper;

import com.ruoyi.platform.domain.MerchantBase;
import com.ruoyi.platform.domain.RiderBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PlatformQualificationReviewMapper {
    @Select("select * from rider_base")
    List<RiderBase> getAllRiderQualificationStatus();

    @Update("update rider_base set audit_status = #{status} where rider_base_id = #{riderId}")
    int setRiderQualificationStatus(Integer status, Integer riderId);

    @Update("update merchant_base set audit_status = #{status} where merchant_base_id = #{merchantId}")
    int setMerchantQualificationStatus(Integer status, Long merchantId);

    @Select("select * from merchant_base")
    List<MerchantBase> getAllMerchantQualificationStatus();
}
