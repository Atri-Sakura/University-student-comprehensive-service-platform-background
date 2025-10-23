package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.RiderWalletMapper;
import com.ruoyi.platform.domain.RiderWallet;
import com.ruoyi.platform.service.IRiderWalletService;

/**
 * 骑手钱包Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class RiderWalletServiceImpl implements IRiderWalletService 
{
    @Autowired
    private RiderWalletMapper riderWalletMapper;


    /**
     * 查询骑手钱包
     * 
     * @return 骑手钱包
     */
    @Override
    public RiderWallet selectRiderWalletByRiderWalletId(Long riderWalletId) {
        return null;
    }

    @Override
    public RiderWallet selectRiderWalletByRiderBaseId(Long riderBaseId) {
        return riderWalletMapper.selectRiderWalletByRiderBaseId(riderBaseId);
    }
    /**
     * 查询骑手钱包列表
     * 
     * @param riderWallet 骑手钱包
     * @return 骑手钱包
     */
    @Override
    public List<RiderWallet> selectRiderWalletList(RiderWallet riderWallet)
    {
        return riderWalletMapper.selectRiderWalletList(riderWallet);
    }

    /**
     * 新增骑手钱包
     * 
     * @param riderWallet 骑手钱包
     * @return 结果
     */
    @Override
    public int insertRiderWallet(RiderWallet riderWallet)
    {
        riderWallet.setCreateTime(DateUtils.getNowDate());
        return riderWalletMapper.insertRiderWallet(riderWallet);
    }

    /**
     * 修改骑手钱包
     * 
     * @param riderWallet 骑手钱包
     * @return 结果
     */
    @Override
    public int updateRiderWallet(RiderWallet riderWallet)
    {
        riderWallet.setUpdateTime(DateUtils.getNowDate());
        return riderWalletMapper.updateRiderWallet(riderWallet);
    }

    /**
     * 批量删除骑手钱包
     * 
     * @param riderWalletIds 需要删除的骑手钱包主键
     * @return 结果
     */
    @Override
    public int deleteRiderWalletByRiderWalletIds(Long[] riderWalletIds)
    {
        return riderWalletMapper.deleteRiderWalletByRiderWalletIds(riderWalletIds);
    }

    /**
     * 删除骑手钱包信息
     * 
     * @param riderWalletId 骑手钱包主键
     * @return 结果
     */
    @Override
    public int deleteRiderWalletByRiderWalletId(Long riderWalletId)
    {
        return riderWalletMapper.deleteRiderWalletByRiderWalletId(riderWalletId);
    }
}
