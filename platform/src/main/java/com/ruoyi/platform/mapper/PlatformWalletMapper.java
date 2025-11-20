package com.ruoyi.platform.mapper;

import com.ruoyi.platform.domain.PlatformWallet;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;

/**
 * 平台钱包Mapper接口
 *
 * @author ruoyi
 * @date 2025-11-13
 */
public interface PlatformWalletMapper {

    /**
     * 查询平台钱包（加锁）
     *
     * @param platformWalletId 平台钱包ID
     * @return 平台钱包
     */
    PlatformWallet selectPlatformWalletForUpdate(@Param("platformWalletId") Long platformWalletId);

    /**
     * 增加余额和冻结金额（用户支付时）
     *
     * @param platformWalletId 平台钱包ID
     * @param amount 金额
     * @return 影响行数
     */
    int increaseBalanceAndFreeze(@Param("platformWalletId") Long platformWalletId,
                                 @Param("amount") BigDecimal amount);

    /**
     * 减少余额和冻结金额（结算给商家/骑手时）
     *
     * @param platformWalletId 平台钱包ID
     * @param amount 金额
     * @return 影响行数
     */
    int decreaseBalanceAndFreeze(@Param("platformWalletId") Long platformWalletId,
                                 @Param("amount") BigDecimal amount);
}