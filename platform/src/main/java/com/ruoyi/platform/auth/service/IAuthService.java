package com.ruoyi.platform.auth.service;

import com.ruoyi.common.core.domain.model.RegisterBody;

/**
 * 三端认证服务接口
 *
 * @author ruoyi
 */
public interface IAuthService
{
    /**
     * 用户端注册
     *
     * @param registerBody 注册信息
     * @return 结果
     */
    String registerUser(RegisterBody registerBody);

    /**
     * 骑手端注册
     *
     * @param registerBody 注册信息
     * @return 结果
     */
    String registerRider(RegisterBody registerBody);

    /**
     * 商家端注册
     *
     * @param registerBody 注册信息
     * @return 结果
     */
    String registerMerchant(RegisterBody registerBody);

    /**
     * 用户端登录
     *
     * @param phone 手机号
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return token
     */
    String loginUser(String phone, String password, String code, String uuid);

    /**
     * 骑手端登录
     *
     * @param phone 手机号
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return token
     */
    String loginRider(String phone, String password, String code, String uuid);

    /**
     * 商家端登录
     *
     * @param phone 手机号
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return token
     */
    String loginMerchant(String phone, String password, String code, String uuid);
}