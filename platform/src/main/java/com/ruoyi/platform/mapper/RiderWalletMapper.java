package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.RiderWallet;

/**
 * 骑手钱包Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface RiderWalletMapper 
{
    /**
     * 查询骑手钱包
     * 
     * @param riderWalletId 骑手钱包主键
     * @return 骑手钱包
     */
    public RiderWallet selectRiderWalletByRiderWalletId(Long riderWalletId);

    /**
     * 查询骑手钱包列表
     * 
     * @param riderWallet 骑手钱包
     * @return 骑手钱包集合
     */
    public List<RiderWallet> selectRiderWalletList(RiderWallet riderWallet);

    /**
     * 新增骑手钱包
     * 
     * @param riderWallet 骑手钱包
     * @return 结果
     */
    public int insertRiderWallet(RiderWallet riderWallet);

    /**
     * 修改骑手钱包
     * 
     * @param riderWallet 骑手钱包
     * @return 结果
     */
    public int updateRiderWallet(RiderWallet riderWallet);

    /**
     * 删除骑手钱包
     * 
     * @param riderWalletId 骑手钱包主键
     * @return 结果
     */
    public int deleteRiderWalletByRiderWalletId(Long riderWalletId);

    /**
     * 批量删除骑手钱包
     * 
     * @param riderWalletIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRiderWalletByRiderWalletIds(Long[] riderWalletIds);
}
