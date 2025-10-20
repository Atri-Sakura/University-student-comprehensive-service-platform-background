package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.MerchantWalletMapper;
import com.ruoyi.platform.domain.MerchantWallet;
import com.ruoyi.platform.service.IMerchantWalletService;

/**
 * 商家钱包Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class MerchantWalletServiceImpl implements IMerchantWalletService 
{
    @Autowired
    private MerchantWalletMapper merchantWalletMapper;

    /**
     * 查询商家钱包
     * 
     * @param merchantWalletId 商家钱包主键
     * @return 商家钱包
     */
    @Override
    public MerchantWallet selectMerchantWalletByMerchantWalletId(Long merchantWalletId)
    {
        return merchantWalletMapper.selectMerchantWalletByMerchantWalletId(merchantWalletId);
    }

    /**
     * 查询商家钱包列表
     * 
     * @param merchantWallet 商家钱包
     * @return 商家钱包
     */
    @Override
    public List<MerchantWallet> selectMerchantWalletList(MerchantWallet merchantWallet)
    {
        return merchantWalletMapper.selectMerchantWalletList(merchantWallet);
    }

    /**
     * 新增商家钱包
     * 
     * @param merchantWallet 商家钱包
     * @return 结果
     */
    @Override
    public int insertMerchantWallet(MerchantWallet merchantWallet)
    {
        merchantWallet.setCreateTime(DateUtils.getNowDate());
        return merchantWalletMapper.insertMerchantWallet(merchantWallet);
    }

    /**
     * 修改商家钱包
     * 
     * @param merchantWallet 商家钱包
     * @return 结果
     */
    @Override
    public int updateMerchantWallet(MerchantWallet merchantWallet)
    {
        merchantWallet.setUpdateTime(DateUtils.getNowDate());
        return merchantWalletMapper.updateMerchantWallet(merchantWallet);
    }

    /**
     * 批量删除商家钱包
     * 
     * @param merchantWalletIds 需要删除的商家钱包主键
     * @return 结果
     */
    @Override
    public int deleteMerchantWalletByMerchantWalletIds(Long[] merchantWalletIds)
    {
        return merchantWalletMapper.deleteMerchantWalletByMerchantWalletIds(merchantWalletIds);
    }

    /**
     * 删除商家钱包信息
     * 
     * @param merchantWalletId 商家钱包主键
     * @return 结果
     */
    @Override
    public int deleteMerchantWalletByMerchantWalletId(Long merchantWalletId)
    {
        return merchantWalletMapper.deleteMerchantWalletByMerchantWalletId(merchantWalletId);
    }
}
