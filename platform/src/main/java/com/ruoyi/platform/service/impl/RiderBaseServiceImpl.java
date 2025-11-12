package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.file.MinioFileUtils;
import com.ruoyi.platform.domain.vo.RiderBaseInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.RiderBaseMapper;
import com.ruoyi.platform.domain.RiderBase;
import com.ruoyi.platform.service.IRiderBaseService;
import com.ruoyi.platform.utils.MaskUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.ruoyi.framework.datasource.DynamicDataSourceContextHolder.log;

/**
 * 骑手基础信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class RiderBaseServiceImpl implements IRiderBaseService 
{
    @Autowired
    private RiderBaseMapper riderBaseMapper;

    @Autowired
    private MinioFileUtils minioFileUtils;

//    @Qualifier("riderUploadExecutor")
//    @Autowired
//    private ThreadPoolExecutor riderUploadExecutor;
    /**
     * 修改骑手基础信息
     */
    @Override
    @Transactional
    public boolean updateRiderBaseInfo(
            Long riderBaseId,
            String nickname,
            String phone,
            MultipartFile avatar
    ){
        if (phone != null && !phone.isEmpty()) {
            String phonePattern = "^1[3-9]\\d{9}$";
            if (!phone.matches(phonePattern)) {
                throw new IllegalArgumentException("手机号格式不正确");
            }
        }
        // 上传头像（同步执行）
        String avatarUrl = null;
        if (avatar != null && !avatar.isEmpty()) {
            try {
                avatarUrl = minioFileUtils.upload(avatar, "user", riderBaseId);
                log.info("[同步上传] 骑手ID={} 头像上传成功: {}", riderBaseId, avatarUrl);
            } catch (Exception e) {
                log.error("[同步上传] 骑手ID={} 上传失败: {}", riderBaseId, e.getMessage(), e);
                throw new RuntimeException("头像上传失败，请稍后重试");
            }
        }
        int updated = riderBaseMapper.updateRiderBaseInfo(riderBaseId, nickname, phone);

        //若头像上传成功，额外更新 avatar 字段
        if (avatarUrl != null) {
            riderBaseMapper.updateRiderAvatarOnly(riderBaseId, avatarUrl);
        }

//        if(avatar != null && !avatar.isEmpty()){
//            CompletableFuture.runAsync(() -> {
//                try {
//                    String url = minioFileUtils.upload(avatar, "user", riderBaseId);
//                    log.info("[异步上传] 骑手ID={} 头像上传成功: {}", riderBaseId, url);
//                    // 异步更新头像字段（单独事务）
//                    riderBaseMapper.updateRiderAvatarOnly(riderBaseId, url);
//                }catch (Exception e){
//                    log.error("[异步上传] 骑手ID={} 上传失败: {}", riderBaseId, e.getMessage());
//                }
//            },riderUploadExecutor);
//        }
        return updated > 0;
    }
    /**
     * 查询骑手脱敏基础信息
     * @param riderId
     * @return 骑手基础信息
     */
    @Override
    public RiderBaseInfoVO getRiderBaseInfo(Long riderId) {
        RiderBaseInfoVO vo = riderBaseMapper.selectRiderBaseInfoById(riderId);
        if(vo == null){
            return null;
        }

        // ⚙️ 调用脱敏工具类处理
        vo.setIdCard(MaskUtils.maskIdCard(vo.getIdCard()));
        vo.setPhone(MaskUtils.maskPhone(vo.getPhone()));
        return vo;
    }

    /**
     * 查询骑手基础信息
     * 
     * @param riderBaseId 骑手基础信息主键
     * @return 骑手基础信息
     */
    @Override
    public RiderBase selectRiderBaseByRiderBaseId(Long riderBaseId)
    {
        return riderBaseMapper.selectRiderBaseByRiderBaseId(riderBaseId);
    }

    /**
     * 查询骑手基础信息列表
     * 
     * @param riderBase 骑手基础信息
     * @return 骑手基础信息
     */
    @Override
    public List<RiderBase> selectRiderBaseList(RiderBase riderBase)
    {
        return riderBaseMapper.selectRiderBaseList(riderBase);
    }

    /**
     * 新增骑手基础信息
     * 
     * @param riderBase 骑手基础信息
     * @return 结果
     */
    @Override
    public int insertRiderBase(RiderBase riderBase)
    {
        riderBase.setCreateTime(DateUtils.getNowDate());
        return riderBaseMapper.insertRiderBase(riderBase);
    }

    /**
     * 修改骑手基础信息
     * 
     * @param riderBase 骑手基础信息
     * @return 结果
     */
    @Override
    public int updateRiderBase(RiderBase riderBase)
    {
        riderBase.setUpdateTime(DateUtils.getNowDate());
        return riderBaseMapper.updateRiderBase(riderBase);
    }

    @Override
    public int updateRiderBaseBasicInfo(RiderBase riderBase) {
        return riderBaseMapper.updateRiderBaseBasicInfo(riderBase);
    }


    /**
     * 批量删除骑手基础信息
     * 
     * @param riderBaseIds 需要删除的骑手基础信息主键
     * @return 结果
     */
    @Override
    public int deleteRiderBaseByRiderBaseIds(Long[] riderBaseIds)
    {
        return riderBaseMapper.deleteRiderBaseByRiderBaseIds(riderBaseIds);
    }

    /**
     * 删除骑手基础信息信息
     * 
     * @param riderBaseId 骑手基础信息主键
     * @return 结果
     */
    @Override
    public int deleteRiderBaseByRiderBaseId(Long riderBaseId)
    {
        return riderBaseMapper.deleteRiderBaseByRiderBaseId(riderBaseId);
    }

    /**
     * 修改骑手工作状态
     *
     */
    @Override
    public int updateRiderWorkStatus(RiderBase riderBase) {
        RiderBase current = riderBaseMapper.selectRiderBaseByRiderBaseId(riderBase.getRiderBaseId());
        if (current != null && current.getWorkStatus().equals(riderBase.getWorkStatus())) {
            // 状态一致，不更新
            return 0;
        }
        return riderBaseMapper.updateRiderWorkStatus(riderBase);
    }

    /**
     * 更新骑手授权信息
     */
    @Override
    public int updateRiderAuthInfo(RiderBase rider) {
        return riderBaseMapper.updateRiderAuthInfo(rider);
    }

}
