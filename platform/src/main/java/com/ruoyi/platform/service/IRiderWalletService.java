package com.ruoyi.platform.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.platform.domain.RiderWallet;

/**
 * 骑手钱包Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IRiderWalletService {

    public BigDecimal getWalletBalance(Long riderBaseId);
    /**
     * 查询骑手钱包
     * 
     * @param riderWalletId 骑手钱包主键
     * @return 骑手钱包
     */
    public RiderWallet selectRiderWalletByRiderWalletId(Long riderWalletId);

    /**
     * 根据骑手ID查询钱包
     */
    RiderWallet selectRiderWalletByRiderBaseId(Long riderBaseId);


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
     * 批量删除骑手钱包
     * 
     * @param riderWalletIds 需要删除的骑手钱包主键集合
     * @return 结果
     */
    public int deleteRiderWalletByRiderWalletIds(Long[] riderWalletIds);

    /**
     * 删除骑手钱包信息
     * 
     * @param riderWalletId 骑手钱包主键
     * @return 结果
     */
    public int deleteRiderWalletByRiderWalletId(Long riderWalletId);
}
