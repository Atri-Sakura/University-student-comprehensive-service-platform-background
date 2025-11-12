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

}
