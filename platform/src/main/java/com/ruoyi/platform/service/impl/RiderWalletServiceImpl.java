package com.ruoyi.platform.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.platform.chat.utils.SnowflakeIdGenerator;
import com.ruoyi.platform.domain.RiderWalletRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.RiderWalletMapper;
import com.ruoyi.platform.domain.RiderWallet;
import com.ruoyi.platform.service.IRiderWalletService;
import org.springframework.transaction.annotation.Transactional;

import static com.ruoyi.framework.datasource.DynamicDataSourceContextHolder.log;

/**
 * 骑手钱包Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class RiderWalletServiceImpl implements IRiderWalletService {
    @Autowired
    private RiderWalletMapper riderWalletMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal getWalletBalance(Long riderBaseId) {
        RiderWallet wallet = riderWalletMapper.selectRiderWalletByRiderBaseId(riderBaseId);
        if (wallet != null) {
            return wallet.getBalance();
        }
        wallet = new RiderWallet();
        wallet.setRiderWalletId(generateId());
        wallet.setRiderBaseId(riderBaseId);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setFreezeAmount(BigDecimal.ZERO);

        try{
            riderWalletMapper.insertRiderWallet(wallet);
        }catch (DuplicateKeyException e) {
            // 并发情况下可能别人已经插入了，忽略即可
            log.warn("并发创建骑手钱包，riderBaseId={}", riderBaseId);
        } catch (Exception e) {
            log.error("创建骑手钱包失败，riderBaseId={}", riderBaseId, e);
            throw e; // 这里可以直接抛，让事务回滚
        }


        // 3. 再查一次，一定能查到
        RiderWallet finalWallet = riderWalletMapper.selectRiderWalletByRiderBaseId(riderBaseId);

        return finalWallet.getBalance();
    }

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

    @Override
    public List<RiderWalletRecord> selectRiderWalletRecordByRiderBaseId(Long riderId) {
        return riderWalletMapper.selectRiderWalletRecordByRiderBaseId(riderId);
    }


    private Long generateId() {
        return SnowflakeIdGenerator.getInstance().nextId();
    }

}
