package com.ruoyi.platform.utils;

import com.ruoyi.common.utils.StringUtils;

/**
 * 脱敏工具类
 *
 */
public class MaskUtils {

    /**
     * 脱敏身份证号，只显示前后4位
     */
    public static String maskIdCard(String idCard){
        if(idCard.length() < 8 || StringUtils.isBlank(idCard)) {
            return "********";
        }
        int length = idCard.length();

        return idCard.substring(0,4) + StringUtils.repeat("*", length - 8) + idCard.substring(length - 4);
    }

    /**
     * 脱敏手机号，显示前3后4位
     */
    public static String maskPhone(String phone){
        if(phone.length() < 7 || StringUtils.isBlank(phone)){
            return "*******";
        }
        return phone.substring(0, 3)
                + StringUtils.repeat('*', phone.length() - 7)
                + phone.substring(phone.length() - 4);
    }
}
