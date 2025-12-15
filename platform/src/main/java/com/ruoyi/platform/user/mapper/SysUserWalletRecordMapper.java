package com.ruoyi.platform.user.mapper;

import com.ruoyi.platform.domain.UserWallet;
import com.ruoyi.platform.user.vo.UserWalletRecordVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SysUserWalletRecordMapper {
    @Select("Select * from user_wallet_record where user_base_id = #{userId} order by trade_time desc")
    List<UserWalletRecordVO> getUserWalletRecordByUserId(Long userId);

    @Select("Select * from user_wallet where user_base_id = #{userId}")
    UserWallet getUserWalletBalanceByUserId(Long userId);

    @Insert("insert into user_wallet(user_wallet_id,user_base_id,status) values(#{userWalletId},#{userId},1)")
    int addWallet(Long userId,Long userWalletId);

    @Update("update user_wallet set status = 0 where user_base_id = #{userId}")
    int freezeWallet(Long userId);

    @Update("update user_wallet set status = 1 where user_base_id = #{userId}")
    int unfreezeWallet(Long userId);

    @Select("select pay_password from user_base where user_base_id = #{userId}")
    String getPayPasswordByUserId(Long userId);

    @Update("update user_base set pay_password = #{newPayPassword} where user_base_id = #{userId}")
    int setPayPassword(Long userId, String newPayPassword);
}
