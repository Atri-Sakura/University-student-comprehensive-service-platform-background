package com.ruoyi.platform.user.mapper;

import com.ruoyi.platform.domain.UserWallet;
import com.ruoyi.platform.user.vo.UserWalletRecordVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserWalletRecordMapper {
    @Select("Select * from user_wallet_record where user_base_id = #{userId} order by trade_time desc")
    List<UserWalletRecordVO> getUserWalletRecordByUserId(Long userId);

    @Select("Select * from user_wallet where user_base_id = #{userId}")
    UserWallet getUserWalletBalanceByUserId(Long userId);

    @Insert("insert into user_wallet(user_base_id,status) values(#{userId},1)")
    int addWallet(Long userId);
}
