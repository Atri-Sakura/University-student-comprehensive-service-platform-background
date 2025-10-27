package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.platform.domain.MerchantWithdrawAccount;
import com.ruoyi.platform.domain.dto.MerchantWithdrawAccountAddDTO;
import com.ruoyi.platform.domain.vo.MerchantWithdrawAccountVO;
import com.ruoyi.platform.merchant.mapper.MerchantWithdrawAccountMapper;
import com.ruoyi.platform.merchant.service.IMerchantWithdrawAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商家提现账户 Service 业务层处理
 *
 * 封装了商家提现账户的增删改查逻辑。
 *
 * @author Jinx
 * @date 2025-10-24
 */
@Service
public class MerchantWithdrawAccountServiceImpl implements IMerchantWithdrawAccountService {
    @Autowired
    private MerchantWithdrawAccountMapper merchantWithdrawAccountMapper;

    /**
     * 设置商家提现账户为默认账户
     *
     * @param merchantBaseId 商家ID
     * @param accountId 提现账户ID
     * @return 设置结果
     */
    @Override
    @Transactional
    public int setDefaultWithdrawAccount(Long merchantBaseId, Long accountId) {
        // 校验账户是否属于该商家
        MerchantWithdrawAccount account = merchantWithdrawAccountMapper.selectMerchantWithdrawAccountById(accountId);
        if (account == null || !account.getMerchantBaseId().equals(merchantBaseId)) {
            throw new RuntimeException("无权操作此账户或账户不存在");
        }

        // 先取消该商家的其他默认账户
        merchantWithdrawAccountMapper.cancelDefaultAccount(merchantBaseId);

        // 再设置当前账户为默认
        return merchantWithdrawAccountMapper.setDefaultAccount(accountId);
    }


    /**
     * 删除商家提现账户
     *
     * @param merchantBaseId 商家ID
     * @param accountId 提现账户ID
     * @return 删除结果
     */
    @Override
    public int deleteWithdrawAccount(Long merchantBaseId, Long accountId) {
        // 校验账户是否属于该商家
        MerchantWithdrawAccount account = merchantWithdrawAccountMapper.selectMerchantWithdrawAccountById(accountId);
        if (account == null || !account.getMerchantBaseId().equals(merchantBaseId)) {
            throw new RuntimeException("无权操作此账户或账户不存在");
        }
        return merchantWithdrawAccountMapper.deleteMerchantWithdrawAccountById(accountId);
    }

    /**
     * 获取商家的提现账户列表
     */
    @Override
    public List<MerchantWithdrawAccountVO> getWithdrawAccountList(Long merchantBaseId) {
        List<MerchantWithdrawAccount> accounts =
                merchantWithdrawAccountMapper.selectMerchantAccountsByMerchantId(merchantBaseId);

        /* *
         * 1. 脱敏显示账号（仅显示前3后4位）
         */
        // 将 domain 转换为 VO，并对账号做脱敏
        return accounts.stream().map(acc -> {
            MerchantWithdrawAccountVO vo = new MerchantWithdrawAccountVO();
            vo.setAccountId(acc.getAccountId());
            vo.setAccountType(acc.getAccountType());
            vo.setAccountName(acc.getAccountName());
            vo.setBankName(acc.getBankName());
            vo.setIsDefault(acc.getIsDefault());
            vo.setStatus(acc.getStatus());
            vo.setCreateTime(acc.getCreateTime());

            // 脱敏显示账号（仅显示前3后4位）
            String number = acc.getAccountNumber();
            if (number != null && number.length() > 7) {
                vo.setAccountNumber(number.substring(0, 3) + "****" + number.substring(number.length() - 4));
            } else {
                vo.setAccountNumber("****");
            }
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 添加商家提现账户
     *
     * @param merchantBaseId 商家ID
     * @param dto 提现账户信息
     * @return 添加结果
     */
    @Override
    @Transactional
    public int addWithdrawAccount(Long merchantBaseId, MerchantWithdrawAccountAddDTO dto) {
        MerchantWithdrawAccount account = new MerchantWithdrawAccount();
        account.setMerchantBaseId(merchantBaseId);
        account.setAccountType(dto.getAccountType());
        account.setAccountName(dto.getAccountName());
        account.setAccountNumber(dto.getAccountNumber());
        account.setBankName(dto.getBankName());
        account.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : 0);
        account.setStatus(1); // 默认启用状态
        account.setCreateTime(new Date());
        account.setUpdateTime(new Date());

        // 如果设置为默认账户，则取消商家的其他默认账户
        if (account.getIsDefault() == 1) {
            merchantWithdrawAccountMapper.cancelDefaultAccount(merchantBaseId);
        }

        return merchantWithdrawAccountMapper.insertMerchantWithdrawAccount(account);
    }
    /**
     * 查询商家提现账户
     *
     * @param accountId 提现账户ID
     * @return 商家提现账户
     */
    @Override
    public MerchantWithdrawAccount selectMerchantWithdrawAccountById(Long accountId)
    {
        return merchantWithdrawAccountMapper.selectMerchantWithdrawAccountById(accountId);
    }

    /**
     * 查询商家提现账户列表
     *
     * @param merchantWithdrawAccount 查询条件
     * @return 商家提现账户集合
     */
    @Override
    public List<MerchantWithdrawAccount> selectMerchantWithdrawAccountList(MerchantWithdrawAccount merchantWithdrawAccount)
    {
        return merchantWithdrawAccountMapper.selectMerchantWithdrawAccountList(merchantWithdrawAccount);
    }

    /**
     * 新增商家提现账户
     *
     * @param merchantWithdrawAccount 数据
     * @return 结果
     */
    @Override
    public int insertMerchantWithdrawAccount(MerchantWithdrawAccount merchantWithdrawAccount)
    {
        return merchantWithdrawAccountMapper.insertMerchantWithdrawAccount(merchantWithdrawAccount);
    }

    /**
     * 修改商家提现账户
     *
     * @param merchantWithdrawAccount 数据
     * @return 结果
     */
    @Override
    public int updateMerchantWithdrawAccount(MerchantWithdrawAccount merchantWithdrawAccount)
    {
        return merchantWithdrawAccountMapper.updateMerchantWithdrawAccount(merchantWithdrawAccount);
    }

    /**
     * 批量删除商家提现账户
     *
     * @param accountIds 要删除的ID数组
     * @return 结果
     */
    @Override
    public int deleteMerchantWithdrawAccountByIds(Long[] accountIds)
    {
        return merchantWithdrawAccountMapper.deleteMerchantWithdrawAccountByIds(accountIds);
    }

    /**
     * 删除单个商家提现账户
     *
     * @param accountId 提现账户ID
     * @return 结果
     */
    @Override
    public int deleteMerchantWithdrawAccountById(Long accountId)
    {
        return merchantWithdrawAccountMapper.deleteMerchantWithdrawAccountById(accountId);
    }
}
