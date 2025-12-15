package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.RiderBase;
import com.ruoyi.platform.domain.vo.RiderBaseInfoVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 骑手基础信息Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IRiderBaseService
{
    /**
     * 修改支付密码
     *
     * @param riderBaseId      当前登录用户 sys_user.id
     * @param oldPayPassword 原支付密码（明文）
     * @param newPayPassword 新支付密码（明文）
     */
    void changePayPassword(Long riderBaseId, String oldPayPassword, String newPayPassword);
    /**
     * 骑手首次设置支付密码
     *
     * @param riderBaseId   当前登录用户在 sys_user 表中的 ID
     * @param payPassword 明文支付密码
     */
    void setPayPassword(Long riderBaseId, String payPassword);
    /**
     * 密码修改
     */
    public void changePassword(Long riderBaseId, String oldPassword, String newPassword);
    /**
     * 修改骑手基础信息
     */
    public boolean updateRiderBaseInfo(
            Long riderBaseId,
            String nickname,
            String phone,
            MultipartFile avatar
    );
    /**
     * 查询骑手脱敏基础信息
     *
     * @param riderBaseId 骑手基础信息主键
     * @return 骑手基础信息
     */
    public RiderBaseInfoVO getRiderBaseInfo(Long riderBaseId);
    /**
     * 查询骑手基础信息
     * 
     * @param riderBaseId 骑手基础信息主键
     * @return 骑手基础信息
     */
    public RiderBase selectRiderBaseByRiderBaseId(Long riderBaseId);

    /**
     * 查询骑手基础信息列表
     * 
     * @param riderBase 骑手基础信息
     * @return 骑手基础信息集合
     */
    public List<RiderBase> selectRiderBaseList(RiderBase riderBase);

    /**
     * 新增骑手基础信息
     * 
     * @param riderBase 骑手基础信息
     * @return 结果
     */
    public int insertRiderBase(RiderBase riderBase);

    /**
     * 修改骑手基础信息
     * 
     * @param riderBase 骑手基础信息
     * @return 结果
     */
    public int updateRiderBase(RiderBase riderBase);


    int updateRiderBaseBasicInfo(RiderBase riderBase);
    /**
     * 批量删除骑手基础信息
     * 
     * @param riderBaseIds 需要删除的骑手基础信息主键集合
     * @return 结果
     */
    public int deleteRiderBaseByRiderBaseIds(Long[] riderBaseIds);

    /**
     * 删除骑手基础信息信息
     * 
     * @param riderBaseId 骑手基础信息主键
     * @return 结果
     */
    public int deleteRiderBaseByRiderBaseId(Long riderBaseId);

    /**
     * 切换骑手工作状态
     *
     */
    int updateRiderWorkStatus(RiderBase riderBase);

    /**
     * 跟新骑手授权信息
     */
    int updateRiderAuthInfo(RiderBase rider);

    /**
     * 根据骑手ID获取昵称
     *
     * @param riderBaseId 骑手ID
     * @return 昵称
     */
    String getNicknameById(Long riderBaseId);

    /**
     * 根据手机号获取昵称
     *
     * @param phone 手机号
     * @return 昵称
     */
    String getNicknameByPhone(String phone);

    /**
     * 根据用户ID查询骑手信息
     *
     * @param userId sys_user 表的用户ID
     * @return 骑手基础信息
     */
    RiderBase selectRiderBaseByUserId(Long userId);

    /**
     * 根据手机号获取骑手信息
     * @param phone
     * @return
     */
    Long selectRiderBaseIdByPhone(String phone);

}
