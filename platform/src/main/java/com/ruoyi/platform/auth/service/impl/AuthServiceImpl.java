package com.ruoyi.platform.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.platform.auth.service.IAuthService;
import com.ruoyi.platform.domain.MerchantBase;
import com.ruoyi.platform.domain.RiderBase;
import com.ruoyi.platform.domain.UserBase;
import com.ruoyi.platform.service.IMerchantBaseService;
import com.ruoyi.platform.service.IRiderBaseService;
import com.ruoyi.platform.service.IUserBaseService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 三端认证服务实现
 *
 * @author ruoyi
 */
@Service
public class AuthServiceImpl implements IAuthService
{
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IUserBaseService userBaseService;

    @Autowired
    private IRiderBaseService riderBaseService;

    @Autowired
    private IMerchantBaseService merchantBaseService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysLoginService loginService;

    /**
     * 用户端注册
     *
     * @param registerBody 注册信息
     * @return 结果消息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String registerUser(RegisterBody registerBody)
    {
        // 1. 校验验证码
        validateCaptcha(registerBody.getPhone(), registerBody.getCode(), registerBody.getUuid());

        // 2. 校验必填字段
        if (StringUtils.isEmpty(registerBody.getNickname()))
        {
            throw new ServiceException("用户昵称不能为空");
        }
        if (StringUtils.isEmpty(registerBody.getStudentId()))
        {
            throw new ServiceException("学号不能为空");
        }
        if (StringUtils.isEmpty(registerBody.getCollege()))
        {
            throw new ServiceException("所属学院不能为空");
        }
        if (StringUtils.isEmpty(registerBody.getMajor()))
        {
            throw new ServiceException("所属专业不能为空");
        }
        if (StringUtils.isEmpty(registerBody.getGrade()))
        {
            throw new ServiceException("年级不能为空");
        }

        String phone = registerBody.getPhone();
        String password = registerBody.getPassword();

        // 3. 校验手机号是否已注册
        SysUser existUser = sysUserService.selectUserByPhonenumber(phone);
        if (StringUtils.isNotNull(existUser))
        {
            throw new ServiceException("手机号已被注册");
        }

        // 4. 创建 sys_user 记录（角色：1-用户）
        SysUser sysUser = new SysUser();
        sysUser.setUserName(phone); // 用户账号设置为手机号
        sysUser.setNickName(registerBody.getNickname());
        sysUser.setPhonenumber(phone);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setStatus("0"); // 正常状态
        sysUser.setRole(1); // 1-用户

        int result = sysUserService.insertUser(sysUser);
        if (result == 0)
        {
            throw new ServiceException("注册失败，请联系管理员");
        }

        // 5. 创建 user_base 记录
        UserBase userBase = new UserBase();
        // 使用 UUID 转换为 Long 类型作为ID
        userBase.setUserBaseId(generateLongId());
        userBase.setUserId(sysUser.getUserId()); // 关联 sys_user 的 user_id
        userBase.setUsername(phone);
        userBase.setPassword(SecurityUtils.encryptPassword(password));
        userBase.setNickname(registerBody.getNickname());
        userBase.setStudentId(registerBody.getStudentId());
        userBase.setCollege(registerBody.getCollege());
        userBase.setMajor(registerBody.getMajor());
        userBase.setGrade(registerBody.getGrade());
        userBase.setPhone(phone);
        userBase.setCreditScore(600L); // 默认信用分
        userBase.setAccountStatus(1L); // 正常状态

        userBaseService.insertUserBase(userBase);

        // 6. 记录日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phone, Constants.REGISTER, "用户注册成功"));

        return "注册成功";
    }

    /**
     * 骑手端注册
     *
     * @param registerBody 注册信息
     * @return 结果消息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String registerRider(RegisterBody registerBody)
    {
        // 1. 校验验证码
        validateCaptcha(registerBody.getPhone(), registerBody.getCode(), registerBody.getUuid());

        // 2. 校验必填字段
        if (StringUtils.isEmpty(registerBody.getRiderNickname()))
        {
            throw new ServiceException("骑手昵称不能为空");
        }
        if (StringUtils.isEmpty(registerBody.getRealName()))
        {
            throw new ServiceException("真实姓名不能为空");
        }
        if (StringUtils.isEmpty(registerBody.getIdCard()))
        {
            throw new ServiceException("身份证号不能为空");
        }

        String phone = registerBody.getPhone();
        String password = registerBody.getPassword();

        // 3. 校验手机号是否已注册
        SysUser existUser = sysUserService.selectUserByPhonenumber(phone);
        if (StringUtils.isNotNull(existUser))
        {
            throw new ServiceException("手机号已被注册");
        }

        // 4. 创建 sys_user 记录（角色：2-骑手）
        SysUser sysUser = new SysUser();
        sysUser.setUserName(phone); // 用户账号设置为手机号
        sysUser.setNickName(registerBody.getRiderNickname());
        sysUser.setPhonenumber(phone);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setStatus("0"); // 正常状态
        sysUser.setRole(2); // 2-骑手

        int result = sysUserService.insertUser(sysUser);
        if (result == 0)
        {
            throw new ServiceException("注册失败，请联系管理员");
        }

        // 5. 创建 rider_base 记录
        RiderBase riderBase = new RiderBase();
        // 使用 UUID 转换为 Long 类型作为ID
        riderBase.setRiderBaseId(generateLongId());
        riderBase.setUserId(sysUser.getUserId()); // 关联 sys_user 的 user_id
        riderBase.setUsername(phone);
        riderBase.setPassword(SecurityUtils.encryptPassword(password));
        riderBase.setNickname(registerBody.getRiderNickname());
        riderBase.setRealName(registerBody.getRealName());
        riderBase.setIdCard(registerBody.getIdCard()); // 实际项目中需要 AES 加密
        riderBase.setPhone(phone);
        riderBase.setAuditStatus(0L); // 待审核
        riderBase.setWorkStatus(0L); // 下线
        riderBase.setCreditScore(600L); // 默认信用分
        riderBase.setAccountStatus(1L); // 正常状态

        riderBaseService.insertRiderBase(riderBase);

        // 6. 记录日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phone, Constants.REGISTER, "骑手注册成功"));

        return "注册成功，请等待审核";
    }

    /**
     * 商家端注册
     *
     * @param registerBody 注册信息
     * @return 结果消息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String registerMerchant(RegisterBody registerBody)
    {
        // 1. 校验验证码
        validateCaptcha(registerBody.getPhone(), registerBody.getCode(), registerBody.getUuid());

        // 2. 校验必填字段
        if (StringUtils.isEmpty(registerBody.getMerchantName()))
        {
            throw new ServiceException("商家名称不能为空");
        }
        if (StringUtils.isEmpty(registerBody.getBusinessScope()))
        {
            throw new ServiceException("经营范围不能为空");
        }

        String phone = registerBody.getPhone();
        String password = registerBody.getPassword();

        // 3. 校验手机号是否已注册
        SysUser existUser = sysUserService.selectUserByPhonenumber(phone);
        if (StringUtils.isNotNull(existUser))
        {
            throw new ServiceException("手机号已被注册");
        }

        // 4. 创建 sys_user 记录（角色：3-商家）
        SysUser sysUser = new SysUser();
        sysUser.setUserName(phone); // 用户账号设置为手机号
        sysUser.setNickName(registerBody.getMerchantName());
        sysUser.setPhonenumber(phone);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setStatus("0"); // 正常状态
        sysUser.setRole(3); // 3-商家

        int result = sysUserService.insertUser(sysUser);
        if (result == 0)
        {
            throw new ServiceException("注册失败，请联系管理员");
        }

        // 5. 创建 merchant_base 记录（基础信息，审核后完善）
        MerchantBase merchantBase = new MerchantBase();
        // 使用 UUID 转换为 Long 类型作为ID
        merchantBase.setMerchantBaseId(generateLongId());
        merchantBase.setUserId(sysUser.getUserId()); // 关联 sys_user 的 user_id
        merchantBase.setUsername(phone);
        merchantBase.setPhone(phone);
        merchantBase.setPassword(SecurityUtils.encryptPassword(password));
        merchantBase.setMerchantName(registerBody.getMerchantName());
        merchantBase.setBusinessScope(registerBody.getBusinessScope());
        merchantBase.setAuditStatus(0L); // 待审核
        merchantBase.setBusinessStatus(0L); // 停业（审核通过后营业）

        // 以下字段设置默认值（后续完善）
        merchantBase.setMerchantAddressId(0L);
        merchantBase.setBusinessHours("9:00-21:00");
        merchantBase.setDeliveryRange(java.math.BigDecimal.valueOf(5.0));
        merchantBase.setMinOrderAmount(java.math.BigDecimal.valueOf(10.0));
        merchantBase.setDeliveryFee(java.math.BigDecimal.valueOf(3.0));
        merchantBase.setLicenseImg(""); // 待上传

        merchantBaseService.insertMerchantBase(merchantBase);

        // 6. 记录日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phone, Constants.REGISTER, "商家注册成功"));

        return "注册成功，请等待审核并完善商家信息";
    }

    /**
     * 用户端登录
     *
     * @param phone 手机号
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return token
     */
    @Override
    public String loginUser(String phone, String password, String code, String uuid)
    {
        // 校验用户角色是否为用户（role=1）
        SysUser user = sysUserService.selectUserByPhonenumber(phone);
        if (StringUtils.isNull(user))
        {
            throw new ServiceException("用户不存在");
        }
        if (user.getRole() == null || user.getRole() != 1)
        {
            throw new ServiceException("您不是用户，请使用正确的端登录");
        }

        // 调用若依登录服务
        return loginService.login(phone, password, code, uuid);
    }

    /**
     * 骑手端登录
     *
     * @param phone 手机号
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return token
     */
    @Override
    public String loginRider(String phone, String password, String code, String uuid)
    {
        // 校验用户角色是否为骑手（role=2）
        SysUser user = sysUserService.selectUserByPhonenumber(phone);
        if (StringUtils.isNull(user))
        {
            throw new ServiceException("用户不存在");
        }
        if (user.getRole() == null || user.getRole() != 2)
        {
            throw new ServiceException("您不是骑手，请使用正确的端登录");
        }

        // 调用若依登录服务
        return loginService.login(phone, password, code, uuid);
    }

    /**
     * 商家端登录
     *
     * @param phone 手机号
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return token
     */
    @Override
    public String loginMerchant(String phone, String password, String code, String uuid)
    {
        // 校验用户角色是否为商家（role=3）
        SysUser user = sysUserService.selectUserByPhonenumber(phone);
        if (StringUtils.isNull(user))
        {
            throw new ServiceException("用户不存在");
        }
        if (user.getRole() == null || user.getRole() != 3)
        {
            throw new ServiceException("您不是商家，请使用正确的端登录");
        }

        // 调用若依登录服务
        return loginService.login(phone, password, code, uuid);
    }

    /**
     * 校验验证码
     *
     * @param phone 手机号
     * @param code 验证码
     * @param uuid 唯一标识
     */
    private void validateCaptcha(String phone, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        if (captcha == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(phone, Constants.REGISTER, MessageUtils.message("user.jcaptcha.expire")));
            throw new ServiceException("验证码已过期");
        }
        redisCache.deleteObject(verifyKey);
        if (!code.equalsIgnoreCase(captcha))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(phone, Constants.REGISTER, MessageUtils.message("user.jcaptcha.error")));
            throw new ServiceException("验证码错误");
        }
    }

    /**
     * 生成Long类型的唯一ID
     * 使用 IdUtils.fastSimpleUUID() 生成UUID字符串，然后转换为Long
     *
     * @return Long类型的唯一ID
     */
    /**
     * 生成Long类型的唯一ID
     * 使用 IdUtils.fastSimpleUUID() 生成UUID字符串，然后转换为Long
     *
     * @return Long类型的唯一ID
     */
    private Long generateLongId()
    {
        // 获取UUID字符串（32位，去掉横线）
        String uuid = IdUtils.fastSimpleUUID();

        // 取UUID的前15位字符（而不是16位），避免超出Long.MAX_VALUE
        // Long.MAX_VALUE = 9223372036854775807 (19位十进制数)
        // 15位16进制 = 最多60位二进制，在Long范围内
        String hexString = uuid.substring(0, 15);

        try {
            // 将16进制字符串转换为Long
            return Long.parseLong(hexString, 16);
        } catch (NumberFormatException e) {
            // 如果转换失败，使用时间戳 + 随机数作为备用方案
            return System.currentTimeMillis() * 1000 + (long)(Math.random() * 1000);
        }
    }
}