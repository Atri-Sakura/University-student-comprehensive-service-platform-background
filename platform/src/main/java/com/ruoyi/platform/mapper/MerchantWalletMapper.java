package com.ruoyi.platform.mapper;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.platform.domain.MerchantWallet;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 商家钱包Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface MerchantWalletMapper 
{
    /**
     * 查询商家钱包
     * 
     * @param merchantWalletId 商家钱包主键
     * @return 商家钱包
     */
    public MerchantWallet selectMerchantWalletByMerchantWalletId(Long merchantWalletId);

    /**
     * 查询商家钱包列表
     * 
     * @param merchantWallet 商家钱包
     * @return 商家钱包集合
     */
    public List<MerchantWallet> selectMerchantWalletList(MerchantWallet merchantWallet);

    /**
     * 新增商家钱包
     * 
     * @param merchantWallet 商家钱包
     * @return 结果
     */
    public int insertMerchantWallet(MerchantWallet merchantWallet);

    /**
     * 修改商家钱包
     * 
     * @param merchantWallet 商家钱包
     * @return 结果
     */
    public int updateMerchantWallet(MerchantWallet merchantWallet);

    /**
     * 删除商家钱包
     * 
     * @param merchantWalletId 商家钱包主键
     * @return 结果
     */
    public int deleteMerchantWalletByMerchantWalletId(Long merchantWalletId);

    /**
     * 批量删除商家钱包
     * 
     * @param merchantWalletIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMerchantWalletByMerchantWalletIds(Long[] merchantWalletIds);

    /**
     * 根据商家ID查询钱包信息
     *
     * @param merchantBaseId 商家ID
     * @return 钱包信息
     */
    MerchantWallet selectMerchantWalletByMerchantId(@Param("merchantBaseId") Long merchantBaseId);


    /**
     * 更新商家钱包余额
     *
     * @param merchantBaseId 商家卡包id
     * @return 钱包数据
     */
    MerchantWallet selectWalletForUpdate(Long merchantBaseId);

    /**
     * 更新商家钱包余额
     *
     * @param merchantBaseId 商家卡包id
     * @param total          金额
     * @return 影响行数
     */
    int updateBalanceAndFreeze(Long merchantBaseId, BigDecimal total);

    /**
     * 出款成功后扣减冻结金额
     *
     * @param merchantBaseId 商家ID
     * @param totalAmount    总金额（提现金额 + 手续费）
     * @return 影响行数
     */
    int decreaseFreeze(@Param("merchantBaseId") Long merchantBaseId,
                       @Param("totalAmount") BigDecimal totalAmount);


    /**
     * 出款失败后退回余额并解冻
     *
     * @param merchantBaseId 商家ID
     * @param totalAmount    总金额（提现金额 + 手续费）
     * @return 影响行数
     */
    int rollbackFreeze(@Param("merchantBaseId") Long merchantBaseId,
                       @Param("totalAmount") BigDecimal totalAmount);
}
