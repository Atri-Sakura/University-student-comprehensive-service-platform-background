package com.ruoyi.platform.user.mapper;

import com.ruoyi.platform.user.vo.UserWalletRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserWalletRecordMapper {
    @Select("Select * from user_wallet_record where user_base_id = #{userId} order by trade_time desc")
    List<UserWalletRecordVO> getUserWalletRecordByUserId(Long userId);
}
