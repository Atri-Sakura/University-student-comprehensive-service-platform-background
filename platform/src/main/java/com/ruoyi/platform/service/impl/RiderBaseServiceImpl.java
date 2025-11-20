package com.ruoyi.platform.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
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
     * 修改支付密码
     *
     * @param riderBaseId      当前登录用户 sys_user.id
     * @param oldPayPassword 原支付密码（明文）
     * @param newPayPassword 新支付密码（明文）
     */
    @Override
    public void changePayPassword(Long riderBaseId, String oldPayPassword, String newPayPassword) {
        // 1. 查当前登录用户对应的骑手
        RiderBase rider = riderBaseMapper.selectRiderBaseById(riderBaseId);
        if (rider == null) {
            throw new ServiceException("当前账号未绑定骑手信息");
        }

        // 2. 必须已经设置过支付密码才能修改
        if (rider.getPayPassword() == null || "".equals(rider.getPayPassword())) {
            throw new ServiceException("尚未设置支付密码，请先设置支付密码");
        }

        // 3. 校验原支付密码是否正确
        if (!SecurityUtils.matchesPassword(oldPayPassword, rider.getPayPassword())) {
            throw new ServiceException("原支付密码不正确");
        }

        // 4. 校验新支付密码格式（6 位数字）
        validatePayPassword(newPayPassword);

        // 5. 新支付密码不能和旧的一样
        if (SecurityUtils.matchesPassword(newPayPassword, rider.getPayPassword())) {
            throw new ServiceException("新支付密码不能与原支付密码相同");
        }

        // TODO: 6. 以后可以在这里增加短信验证码校验（再提高一层安全）
        String encoded = SecurityUtils.encryptPassword(newPayPassword);
        int rows = riderBaseMapper.updateRiderPayPassword(rider.getRiderBaseId(), encoded);
        if (rows <= 0) {
            throw new ServiceException("支付密码修改失败，请稍后重试");
        }

    }


    /**
     * 骑手首次设置支付密码
     *
     * @param riderBaseId   当前登录用户在 sys_user 表中的 ID
     * @param payPassword 明文支付密码
     */
    public void setPayPassword(Long riderBaseId, String payPassword){
        //检测用户是否存在
        RiderBase rider = riderBaseMapper.selectRiderBaseById(riderBaseId);
        if (rider == null) {
            throw new ServiceException("当前账号未绑定骑手信息");
        }

        //检验是否已有密码
        if(rider.getPayPassword()!= null && !"".equals(rider.getPayPassword())){
            throw new ServiceException("已设置支付密码，请使用修改支付密码功能");
        }

        // 3. 校验支付密码规则（6 位数字）
        validatePayPassword(payPassword);

        // TODO: 4. 以后在这里加：短信验证码校验（比如 checkSmsCode(sysUserId, smsCode)）

        // 5. 加密支付密码（BCrypt）
        String encoded = SecurityUtils.encryptPassword(payPassword);

        // 6. 更新骑手的支付密码
        int rows = riderBaseMapper.updateRiderPayPassword(rider.getRiderBaseId(), encoded);
        if (rows <= 0) {
            throw new ServiceException("支付密码设置失败，请稍后重试");
        }
    }

    /**
     * 支付密码规则：
     *  - 必须是 6 位数字
     *  - 你也可以拓展：不能为 000000 / 123456 之类的弱口令
     */
    public void validatePayPassword(String payPassword){
        if (payPassword == null) {
            throw new ServiceException("支付密码不能为空");
        }

        if (!payPassword.matches("^[0-9]{6}$")) {
            throw new ServiceException("支付密码必须为 6 位数字");
        }
    }


    /**
     * 骑手修改密码
     *
     * @param riderBaseId     当前骑手 ID（从登录信息中拿）
     * @param oldPassword 原密码（明文）
     * @param newPassword 新密码（明文）
     */
    @Override
    public void changePassword(Long riderBaseId, String oldPassword, String newPassword){
        // 1. 查出当前骑手信息
        RiderBase rider = riderBaseMapper.selectRiderBaseById(riderBaseId);
        if (rider == null) {
            throw new ServiceException("骑手不存在");
        }

        // 2. 校验原密码是否正确（BCrypt）这里是内置的原理是oldPassword 明文加密后对比还是？
        if (!SecurityUtils.matchesPassword(oldPassword, rider.getPassword())) {
            throw new ServiceException("原密码不正确");
        }
        // 3. 校验新密码规则（长度、空格、字母+数字、不能等于旧密码）
        validateNewPassword(newPassword, rider.getPassword());

        // 4. 加密新密码并更新数据库
        String encrypted = SecurityUtils.encryptPassword(newPassword);
        rider.setPassword(encrypted);

        int rows = riderBaseMapper.updateRiderBasePassword(rider);
        if (rows <= 0) {
            throw new ServiceException("修改密码失败，请稍后重试");
        }
    }

    /**
     * 校验新密码规则：
     *  - 6-20 位
     *  - 不能包含空格
     *  - 必须同时包含字母和数字
     *  - 不能和旧密码相同（旧密码是加密串，用 matches 判断）
     */
    private void validateNewPassword(String newPassword, String oldEncryptedPassword){
        if (newPassword == null) {
            throw new ServiceException("新密码不能为空");
        }

        // ① 长度 6-20
        int len = newPassword.length();
        if (len < 6 || len > 20) {
            throw new ServiceException("新密码长度必须为 6-20 位");
        }

        // ② 不包含空格
        if (newPassword.contains(" ")) {
            throw new ServiceException("新密码不能包含空格");
        }

        // ③ 必须包含字母和数字（你也可以拆成多个 if 写）
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)\\S{6,20}$";
        if (!newPassword.matches(regex)) {
            throw new ServiceException("新密码必须同时包含字母和数字，且不能包含空格");
        }

        // ④ 不能和旧密码相同（注意旧密码是加密的）
        if (SecurityUtils.matchesPassword(newPassword, oldEncryptedPassword)) {
            throw new ServiceException("新密码不能与旧密码相同");
        }
    }
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
        //确保不为空才处理
        if(vo.getIdCard() != null ){
        // ⚙️ 调用脱敏工具类处理
            vo.setIdCard(MaskUtils.maskIdCard(vo.getIdCard()));
        }

        if(vo.getPhone() != null){
            vo.setPhone(MaskUtils.maskPhone(vo.getPhone()));
        }

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
