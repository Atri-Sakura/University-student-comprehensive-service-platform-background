package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.UserBase;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户基础信息Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IUserBaseService 
{
    /**
     * 查询用户基础信息
     * 
     * @param userBaseId 用户基础信息主键
     * @return 用户基础信息
     */
    public UserBase selectUserBaseByUserBaseId(Long userBaseId);

    /**
     * 查询用户基础信息列表
     * 
     * @param userBase 用户基础信息
     * @return 用户基础信息集合
     */
    public List<UserBase> selectUserBaseList(UserBase userBase);

    /**
     * 新增用户基础信息
     * 
     * @param userBase 用户基础信息
     * @return 结果
     */
    public int insertUserBase(UserBase userBase);

    /**
     * 修改用户基础信息
     * 
     * @param userBase 用户基础信息
     * @return 结果
     */
    public int updateUserBase(UserBase userBase);

    /**
     * 批量删除用户基础信息
     * 
     * @param userBaseIds 需要删除的用户基础信息主键集合
     * @return 结果
     */
    public int deleteUserBaseByUserBaseIds(Long[] userBaseIds);

    /**
     * 删除用户基础信息信息
     * 
     * @param userBaseId 用户基础信息主键
     * @return 结果
     */
    public int deleteUserBaseByUserBaseId(Long userBaseId);

    /**
     * 根据电话号码查询用户Id
     * @param phone
     * @return
     */
    public Long selectUserBaseIdByPhone(String phone);
    /**
     * 头像
     * @param file
     * @param userBaseId
     * @return
     */
    public String updateAvatar(MultipartFile file,Long userBaseId);

    /**
     * 根据用户ID获取昵称
     *
     * @param userBaseId 用户ID
     * @return 昵称
     */
    String getNicknameById(Long userBaseId);

    /**
     * 根据手机号获取昵称
     *
     * @param phone 手机号
     * @return 昵称
     */
    String getNicknameByPhone(String phone);

    /**
     * 验证支付密码
     *
     * @param userBaseId 用户ID
     * @param payPassword 支付密码（明文）
     * @return 是否正确
     */
    boolean verifyPayPassword(Long userBaseId, String payPassword);
}
