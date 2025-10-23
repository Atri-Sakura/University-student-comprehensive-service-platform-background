package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.RiderWallet;

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

}
