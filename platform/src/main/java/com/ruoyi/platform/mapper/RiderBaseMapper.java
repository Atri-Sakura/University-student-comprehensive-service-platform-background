package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.RiderBase;
import com.ruoyi.platform.domain.vo.RiderBaseInfoVO;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 骑手基础信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface RiderBaseMapper
{

    /**
     * 更新骑手支付密码
     */
    int updateRiderPayPassword(@Param("riderBaseId") Long riderBaseId,
                               @Param("payPassword") String payPassword);
    /**
     * 获取骑手基础信息
     *
     */
    RiderBase selectRiderBaseById(@Param("riderBaseId") Long riderBaseId);


    /**
     * 只更新密码（更安全，避免误改其他字段）
     */
    int updateRiderBasePassword(RiderBase riderBase);
    /**
     * 更新骑手基础信息（昵称、电话）
     */
    int updateRiderBaseInfo(
            @Param("riderBaseId") Long riderBaseId,
            @Param("nickname") String nickname,
            @Param("phone") String phone
    );

    /**
     * 异步更新骑手头像字段
     */
    int updateRiderAvatarOnly(
            @Param("riderBaseId") Long riderBaseId,
            @Param("avatarUrl") String avatarUrl
    );
    /**
     * 根据骑手ID查询骑手基础信息
     *
     */
    public RiderBaseInfoVO selectRiderBaseInfoById(@Param("riderBaseId")Long riderBaseId);
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

    /**
     * 删除骑手基础信息
     * 
     * @param riderBaseId 骑手基础信息主键
     * @return 结果
     */
    public int deleteRiderBaseByRiderBaseId(Long riderBaseId);

    /**
     * 批量删除骑手基础信息
     * 
     * @param riderBaseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRiderBaseByRiderBaseIds(Long[] riderBaseIds);

    /**
     * 修改骑手工作状态
     *
     */
    int updateRiderWorkStatus(RiderBase riderBase);

    /**
     * 更新骑手授权信息
     */
    int updateRiderAuthInfo(RiderBase rider);


    /**
     * 通过手机号查询骑手
     *
     * @param phone 手机号
     * @return 骑手基础信息
     */
    public RiderBase selectRiderBaseByPhone(String phone);

    int updateRiderBaseBasicInfo(RiderBase riderBase);

    /**
     * 根据用户ID查询骑手基础信息
     *
     * @param userId 用户ID
     * @return 骑手基础信息
     */
    RiderBase selectRiderBaseByUserId(@Param("userId") Long userId);

    @Update("update sys_user set password = #{encrypted} where user_id = #{userId}")
    int updateSysUserPassword(Long userId, String encrypted);
}
