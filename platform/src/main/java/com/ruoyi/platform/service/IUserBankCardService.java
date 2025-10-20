package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.UserBankCard;

/**
 * 用户银行卡绑定Service接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface IUserBankCardService 
{
    /**
     * 查询用户银行卡绑定
     * 
     * @param id 用户银行卡绑定主键
     * @return 用户银行卡绑定
     */
    public UserBankCard selectUserBankCardById(Long id);

    /**
     * 查询用户银行卡绑定列表
     * 
     * @param userBankCard 用户银行卡绑定
     * @return 用户银行卡绑定集合
     */
    public List<UserBankCard> selectUserBankCardList(UserBankCard userBankCard);

    /**
     * 新增用户银行卡绑定
     * 
     * @param userBankCard 用户银行卡绑定
     * @return 结果
     */
    public int insertUserBankCard(UserBankCard userBankCard);

    /**
     * 修改用户银行卡绑定
     * 
     * @param userBankCard 用户银行卡绑定
     * @return 结果
     */
    public int updateUserBankCard(UserBankCard userBankCard);

    /**
     * 批量删除用户银行卡绑定
     * 
     * @param ids 需要删除的用户银行卡绑定主键集合
     * @return 结果
     */
    public int deleteUserBankCardByIds(Long[] ids);

    /**
     * 删除用户银行卡绑定信息
     * 
     * @param id 用户银行卡绑定主键
     * @return 结果
     */
    public int deleteUserBankCardById(Long id);
}
