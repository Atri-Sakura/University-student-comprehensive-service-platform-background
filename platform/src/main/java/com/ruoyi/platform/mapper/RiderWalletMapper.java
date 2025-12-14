package com.ruoyi.platform.mapper;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.platform.domain.RiderWallet;
import com.ruoyi.platform.domain.RiderWalletRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 骑手钱包Mapper接口
 *
 * @author ruoyi
 * @date 2025-10-20
 */
public interface RiderWalletMapper
{
    /**
     * 根据骑手ID查询钱包
     */
    RiderWallet selectRiderWalletByRiderBaseId(Long riderBaseId);

    /**
     * 查询骑手钱包列表
     *
     * @param riderWallet 骑手钱包
     * @return 骑手钱包
     */
    List<RiderWallet> selectRiderWalletList(RiderWallet riderWallet);

    /**
     * 新增骑手钱包
     *
     * @param riderWallet 骑手钱包
     * @return 结果
     */
    int insertRiderWallet(RiderWallet riderWallet);

    /**
     * 修改骑手钱包
     *
     * @param riderWallet 骑手钱包
     * @return 结果
     */
    int updateRiderWallet(RiderWallet riderWallet);

    /**
     * 批量删除骑手钱包
     *
     * @param riderWalletIds 需要删除的骑手钱包主键
     * @return 结果
     */
    int deleteRiderWalletByRiderWalletIds(Long[] riderWalletIds);

    /**
     * 删除骑手钱包信息
     *
     * @param riderWalletId 骑手钱包主键
     * @return 结果
     */
    int deleteRiderWalletByRiderWalletId(Long riderWalletId);

    /**
     * 增加骑手余额
     *
     * @param riderId 骑手ID
     * @param amount 金额
     * @return 影响行数
     */
    int increaseBalance(@Param("riderId") Long riderId,
                        @Param("amount") BigDecimal amount);

    @Select("select * from rider_wallet_record where rider_base_id = #{riderId} order by trade_time desc")
    List<RiderWalletRecord> selectRiderWalletRecordByRiderBaseId(Long riderId);
}