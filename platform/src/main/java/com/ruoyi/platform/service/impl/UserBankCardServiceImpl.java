package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserBankCardMapper;
import com.ruoyi.platform.domain.UserBankCard;
import com.ruoyi.platform.service.IUserBankCardService;

/**
 * 用户银行卡绑定Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class UserBankCardServiceImpl implements IUserBankCardService 
{
    @Autowired
    private UserBankCardMapper userBankCardMapper;

    /**
     * 查询用户银行卡绑定
     * 
     * @param id 用户银行卡绑定主键
     * @return 用户银行卡绑定
     */
    @Override
    public UserBankCard selectUserBankCardById(Long id)
    {
        return userBankCardMapper.selectUserBankCardById(id);
    }

    /**
     * 查询用户银行卡绑定列表
     * 
     * @param userBankCard 用户银行卡绑定
     * @return 用户银行卡绑定
     */
    @Override
    public List<UserBankCard> selectUserBankCardList(UserBankCard userBankCard)
    {
        return userBankCardMapper.selectUserBankCardList(userBankCard);
    }

    /**
     * 新增用户银行卡绑定
     * 
     * @param userBankCard 用户银行卡绑定
     * @return 结果
     */
    @Override
    public int insertUserBankCard(UserBankCard userBankCard)
    {
        userBankCard.setCreateTime(DateUtils.getNowDate());
        return userBankCardMapper.insertUserBankCard(userBankCard);
    }

    /**
     * 修改用户银行卡绑定
     * 
     * @param userBankCard 用户银行卡绑定
     * @return 结果
     */
    @Override
    public int updateUserBankCard(UserBankCard userBankCard)
    {
        userBankCard.setUpdateTime(DateUtils.getNowDate());
        return userBankCardMapper.updateUserBankCard(userBankCard);
    }

    /**
     * 批量删除用户银行卡绑定
     * 
     * @param ids 需要删除的用户银行卡绑定主键
     * @return 结果
     */
    @Override
    public int deleteUserBankCardByIds(Long[] ids)
    {
        return userBankCardMapper.deleteUserBankCardByIds(ids);
    }

    /**
     * 删除用户银行卡绑定信息
     * 
     * @param id 用户银行卡绑定主键
     * @return 结果
     */
    @Override
    public int deleteUserBankCardById(Long id)
    {
        return userBankCardMapper.deleteUserBankCardById(id);
    }
}
