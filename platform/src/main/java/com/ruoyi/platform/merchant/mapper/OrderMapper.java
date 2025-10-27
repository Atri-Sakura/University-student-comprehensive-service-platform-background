package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.merchant.vo.MerchantOrderStatusVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {
    @Update("UPDATE order_main SET order_status = 2 WHERE order_no = #{orderNoId}")
    void acceptOrder(Long orderNoId);

    @Select("SELECT * FROM order_main WHERE order_no = #{orderNoId}")
    OrderMain selectOrderMainByNoId(Long orderNoId);

    MerchantOrderStatusVO getMerchantOrderStatus(Long MerchantId);
}
