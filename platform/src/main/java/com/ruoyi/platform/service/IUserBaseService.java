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
     * 上传用户头像
     * @param file
     * @return
     */
    public String updateAvatar(MultipartFile file,Long userBaseId);
}
