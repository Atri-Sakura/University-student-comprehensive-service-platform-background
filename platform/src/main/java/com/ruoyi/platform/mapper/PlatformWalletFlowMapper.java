package com.ruoyi.platform.mapper;

import com.ruoyi.platform.domain.PlatformWalletFlow;
import java.util.List;

/**
 * 平台钱包流水Mapper接口
 *
 * @author ruoyi
 * @date 2025-11-13
 */
public interface PlatformWalletFlowMapper {

    /**
     * 新增平台钱包流水
     *
     * @param platformWalletFlow 平台钱包流水
     * @return 影响行数
     */
    int insertPlatformWalletFlow(PlatformWalletFlow platformWalletFlow);

    /**
     * 查询平台钱包流水列表
     *
     * @param platformWalletFlow 平台钱包流水
     * @return 平台钱包流水集合
     */
    List<PlatformWalletFlow> selectPlatformWalletFlowList(PlatformWalletFlow platformWalletFlow);
}