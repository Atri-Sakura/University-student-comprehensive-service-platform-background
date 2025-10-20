package com.ruoyi.framework.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 注册校验方法
 *
 * @author ruoyi
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册 (修改为手机号注册)
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "";
        String phonenumber = registerBody.getPhonenumber();  // 获取手机号
        String password = registerBody.getPassword();
        String nickName = registerBody.getNickName();  // 获取昵称(可选)

        SysUser sysUser = new SysUser();
        sysUser.setPhonenumber(phonenumber);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            validateCaptcha(phonenumber, registerBody.getCode(), registerBody.getUuid());
        }

        // 手机号和密码校验
        if (StringUtils.isEmpty(phonenumber))
        {
            msg = "手机号不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (!phonenumber.matches("^1[3-9]\\d{9}$"))
        {
            msg = "手机号格式不正确";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (!userService.checkPhoneUnique(sysUser))
        {
            msg = "保存用户'" + phonenumber + "'失败，该手机号已被注册";
        }
        else
        {
            // 生成用户名 (使用 user_ + 手机号)
            sysUser.setUserName("user_" + phonenumber);

            // 设置昵称 (如果没有提供,使用手机号后4位)
            if (StringUtils.isEmpty(nickName))
            {
                sysUser.setNickName("用户" + phonenumber.substring(7));  // 取后4位
            }
            else
            {
                sysUser.setNickName(nickName);
            }

            sysUser.setPhonenumber(phonenumber);
            sysUser.setPwdUpdateDate(DateUtils.getNowDate());
            sysUser.setPassword(SecurityUtils.encryptPassword(password));

            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phonenumber, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     *
     * @param phonenumber 手机号
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String phonenumber, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}