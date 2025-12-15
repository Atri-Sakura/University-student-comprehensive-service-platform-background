package com.ruoyi.platform.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.MinioFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserBaseMapper;
import com.ruoyi.platform.domain.UserBase;
import com.ruoyi.platform.service.IUserBaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户基础信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class UserBaseServiceImpl implements IUserBaseService 
{
    @Autowired
    private UserBaseMapper userBaseMapper;

    @Autowired
    private MinioFileUtils minioFileUtils;

    /**
     * 查询用户基础信息
     * 
     * @param userBaseId 用户基础信息主键
     * @return 用户基础信息
     */
    @Override
    public UserBase selectUserBaseByUserBaseId(Long userBaseId)
    {
        return userBaseMapper.selectUserBaseByUserBaseId(userBaseId);
    }

    /**
     * 查询用户基础信息列表
     * 
     * @param userBase 用户基础信息
     * @return 用户基础信息
     */
    @Override
    public List<UserBase> selectUserBaseList(UserBase userBase)
    {
        return userBaseMapper.selectUserBaseList(userBase);
    }

    /**
     * 新增用户基础信息
     * 
     * @param userBase 用户基础信息
     * @return 结果
     */
    @Override
    public int insertUserBase(UserBase userBase)
    {
        userBase.setCreateTime(DateUtils.getNowDate());
        return userBaseMapper.insertUserBase(userBase);
    }

    /**
     * 修改用户基础信息
     * 
     * @param userBase 用户基础信息
     * @return 结果
     */
    @Override
    public int updateUserBase(UserBase userBase)
    {
        userBase.setUpdateTime(DateUtils.getNowDate());
        return userBaseMapper.updateUserBase(userBase);
    }

    /**
     * 批量删除用户基础信息
     * 
     * @param userBaseIds 需要删除的用户基础信息主键
     * @return 结果
     */
    @Override
    public int deleteUserBaseByUserBaseIds(Long[] userBaseIds)
    {
        return userBaseMapper.deleteUserBaseByUserBaseIds(userBaseIds);
    }

    /**
     * 删除用户基础信息信息
     * 
     * @param userBaseId 用户基础信息主键
     * @return 结果
     */
    @Override
    public int deleteUserBaseByUserBaseId(Long userBaseId)
    {
        return userBaseMapper.deleteUserBaseByUserBaseId(userBaseId);
    }

    @Override
    public Long selectUserBaseIdByPhone(String phone)
    {
        return userBaseMapper.selectUserIdByPhone(phone);
    }

    @Override
    public String updateAvatar(MultipartFile file, Long userBaseId){
        try{
            // 1. 查询用户当前信息
            UserBase userBase = userBaseMapper.selectUserBaseByUserBaseId(userBaseId);
            if (userBase == null) {
                return "error";
            }

            // 2. 保存旧头像URL(用于后续删除)
            String oldAvatar = userBase.getAvatar();

            // 3. 上传新头像到MinIO
            String newAvatarUrl = minioFileUtils.upload(file, "user", userBaseId);

            // 4. 更新数据库中的头像字段
            userBase.setAvatar(newAvatarUrl);
            int rows = userBaseMapper.updateUserBase(userBase);

            // 5. 如果更新成功且存在旧头像，删除MinIO中的旧头像文件
            if (rows > 0 && oldAvatar != null && !oldAvatar.isEmpty()) {
                // 使用安全删除，即使删除失败也不影响主流程
                minioFileUtils. safeDeleteByUrl(oldAvatar);
            }

            return newAvatarUrl;

        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 根据用户ID获取昵称
     *
     * @param userBaseId 用户ID
     * @return 昵称
     */
    @Override
    public String getNicknameById(Long userBaseId) {
        if (userBaseId == null) {
            return null;
        }
        UserBase userBase = userBaseMapper.selectUserBaseByUserBaseId(userBaseId);
        return userBase != null ? userBase.getNickname() : null;
    }

    /**
     * 根据手机号获取昵称
     *
     * @param phone 手机号
     * @return 昵称
     */
    @Override
    public String getNicknameByPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return null;
        }
        UserBase userBase = userBaseMapper.selectUserBaseByPhone(phone);
        return userBase != null ? userBase.getNickname() : null;
    }


    /**
     * 验证支付密码
     */
    @Override
    public boolean verifyPayPassword(Long userBaseId, String payPassword) {
        UserBase userBase = userBaseMapper.selectUserBaseByUserBaseId(userBaseId);
        if (userBase == null) {
            throw new ServiceException("用户不存在");
        }

        if (userBase.getPayPassword() == null || userBase.getPayPassword().isEmpty()) {
            throw new ServiceException("请先设置支付密码");
        }

        return SecurityUtils.matchesPassword(payPassword, userBase.getPayPassword());
    }

}
