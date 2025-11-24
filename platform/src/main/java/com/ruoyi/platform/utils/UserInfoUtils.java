package com.ruoyi.platform.utils;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.service.IMerchantBaseService;
import com.ruoyi.platform.service.IRiderBaseService;
import com.ruoyi.platform.service.IUserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户信息统一工具类
 * @date 2025-11-21
 */
@Component
public class UserInfoUtils {

    private static IUserBaseService userBaseService;
    private static IRiderBaseService riderBaseService;
    private static IMerchantBaseService merchantBaseService;

    @Autowired
    public void setUserBaseService(IUserBaseService service) {
        UserInfoUtils.userBaseService = service;
    }

    @Autowired
    public void setRiderBaseService(IRiderBaseService service) {
        UserInfoUtils.riderBaseService = service;
    }

    @Autowired
    public void setMerchantBaseService(IMerchantBaseService service) {
        UserInfoUtils.merchantBaseService = service;
    }

    /**
     * 根据角色类型和ID获取昵称
     *
     * @param role 角色类型 (0:平台管理员 1:用户 2:骑手 3:商家)
     * @param id   对应角色的BaseId
     * @return 昵称/名称
     */
    public static String getNicknameByRoleAndId(Integer role, Long id) {
        if (id == null || role == null) {
            return "未知用户";
        }

        switch (role) {
            case 1: // 普通用户
                return userBaseService.getNicknameById(id);
            case 2: // 骑手
                return riderBaseService.getNicknameById(id);
            case 3: // 商家
                return merchantBaseService.getNameById(id);
            case 0: // 平台管理员
                return "管理员";
            default:
                return "未知用户";
        }
    }

    /**
     * 根据手机号和角色类型获取昵称
     *
     * @param phone 手机号
     * @param role  角色类型 (1:用户 2:骑手 3:商家)
     * @return 昵称/名称
     */
    public static String getNicknameByPhoneAndRole(String phone, Integer role) {
        if (phone == null || phone.isEmpty() || role == null) {
            return "未知用户";
        }

        switch (role) {
            case 1: // 普通用户
                return userBaseService.getNicknameByPhone(phone);
            case 2: // 骑手
                return riderBaseService.getNicknameByPhone(phone);
            case 3: // 商家
                return merchantBaseService.getNameByPhone(phone);
            default:
                return "未知用户";
        }
    }

    /**
     * 获取当前登录用户的昵称（从业务表获取）
     *
     * @return 昵称
     */
    public static String getCurrentUserNickname() {
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            SysUser sysUser = loginUser.getUser();
            Integer role = sysUser.getRole();

            switch (role) {
                case 1: // 普通用户
                    Long userBaseId = SecurityUtils.getUserBaseId();
                    return userBaseService.getNicknameById(userBaseId);
                case 2: // 骑手
                    Long riderBaseId = SecurityUtils.getRiderBaseId();
                    return riderBaseService.getNicknameById(riderBaseId);
                case 3: // 商家
                    Long merchantBaseId = SecurityUtils.getMerchantBaseId();
                    return merchantBaseService.getNameById(merchantBaseId);
                case 0: // 平台管理员
                    return sysUser.getNickName(); // 从 sys_user 获取
                default:
                    return "未知用户";
            }
        } catch (Exception e) {
            return "未知用户";
        }
    }

    /**
     * 获取当前登录用户的昵称（从 sys_user 表获取）
     *
     * @return 昵称
     */
    public static String getCurrentUserNicknameFast() {
        try {
            return SecurityUtils.getLoginUser().getUser().getNickName();
        } catch (Exception e) {
            return "未知用户";
        }
    }
}