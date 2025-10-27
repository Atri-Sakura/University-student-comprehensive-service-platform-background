package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantWithdrawAccount;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商家提现账户 Mapper 接口
 *
 */
@Mapper
public interface MerchantWithdrawAccountMapper {

    /**
     * 查询商家提现账户
     *
     * @param accountId 提现账户ID
     * @return 提现账户
     */
    MerchantWithdrawAccount selectMerchantWithdrawAccountById(Long accountId);

    /**
     * 查询商家提现账户列表
     *
     * @param merchantWithdrawAccount 查询条件
     * @return 提现账户集合
     */
    List<MerchantWithdrawAccount> selectMerchantWithdrawAccountList(MerchantWithdrawAccount merchantWithdrawAccount);

    /**
     * 新增商家提现账户
     *
     * @param merchantWithdrawAccount 数据
     * @return 结果
     */
    int insertMerchantWithdrawAccount(MerchantWithdrawAccount merchantWithdrawAccount);

    /**
     * 修改商家提现账户
     */
    int updateMerchantWithdrawAccount(MerchantWithdrawAccount merchantWithdrawAccount);

    /**
     * 删除商家提现账户
     */
    int deleteMerchantWithdrawAccountById(Long accountId);

    /**
     * 批量删除商家提现账户
     */
    int deleteMerchantWithdrawAccountByIds(Long[] accountIds);

    /**
     * 取消商家的其他默认账户（设置 is_default=0）
     */
    int cancelDefaultAccount(@Param("merchantBaseId") Long merchantBaseId);

    /**
     * 查询商家所有提现账户（启用状态）
     */
    List<MerchantWithdrawAccount> selectMerchantAccountsByMerchantId(Long merchantBaseId);

    /**
     * 将指定账户设为默认账户
     */
    int setDefaultAccount(Long accountId);

}
