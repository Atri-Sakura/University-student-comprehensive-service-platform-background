package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.domain.MerchantWithdrawAccount;
import com.ruoyi.platform.domain.dto.MerchantWithdrawAccountAddDTO;
import com.ruoyi.platform.domain.vo.MerchantWithdrawAccountVO;

import java.util.List;

/**
 * 商家提现账户 Service 接口
 *
 * 提供商家提现账户的增删改查业务逻辑定义。
 *
 * @author Jinx
 * @date 2025-10-24
 */
public interface IMerchantWithdrawAccountService {

    /**
     * 设置提现账户为默认账户
     *
     * @param merchantBaseId 商家ID
     * @param accountId 账户ID
     * @return 是否成功
     */
    int setDefaultWithdrawAccount(Long merchantBaseId, Long accountId);

    /**
     * 删除商家提现账户
     *
     * @param merchantBaseId 商家ID
     * @param accountId 账户ID
     * @return 删除结果
     */
    int deleteWithdrawAccount(Long merchantBaseId, Long accountId);

    /**
     * 获取商家的提现账户列表
     */
    List<MerchantWithdrawAccountVO> getWithdrawAccountList(Long merchantBaseId);
    /**
     * 添加商家提现账户
     *
     * @param merchantBaseId 商家ID
     * @param dto 请求数据
     * @return 成功条数
     */
    int addWithdrawAccount(Long merchantBaseId, MerchantWithdrawAccountAddDTO dto);
    /**
     * 查询商家提现账户
     *
     * @param accountId 提现账户ID
     * @return 商家提现账户信息
     */
    MerchantWithdrawAccount selectMerchantWithdrawAccountById(Long accountId);

    /**
     * 查询商家提现账户列表
     *
     * @param merchantWithdrawAccount 查询条件
     * @return 商家提现账户集合
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
     *
     * @param merchantWithdrawAccount 数据
     * @return 结果
     */
    int updateMerchantWithdrawAccount(MerchantWithdrawAccount merchantWithdrawAccount);

    /**
     * 批量删除商家提现账户
     *
     * @param accountIds 要删除的ID数组
     * @return 结果
     */
    int deleteMerchantWithdrawAccountByIds(Long[] accountIds);

    /**
     * 删除单个商家提现账户
     *
     * @param accountId 提现账户ID
     * @return 结果
     */
    int deleteMerchantWithdrawAccountById(Long accountId);
}
