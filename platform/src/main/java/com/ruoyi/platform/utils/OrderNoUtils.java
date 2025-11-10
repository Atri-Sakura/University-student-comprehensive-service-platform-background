package com.ruoyi.platform.utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单号生成工具
 * 规则：时间戳（到秒） + 用户ID后四位 + 5位随机数
 */
public class OrderNoUtils {
    private static final SecureRandom random = new SecureRandom();

    public static String generate(Long userId) {
        String timePart = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String userPart = String.format("%04d", userId % 10000); // 取后4位用户ID
        int randomPart = random.nextInt(90000) + 10000; // 10000 ~ 99999
        return timePart + userPart + randomPart;
    }
}
