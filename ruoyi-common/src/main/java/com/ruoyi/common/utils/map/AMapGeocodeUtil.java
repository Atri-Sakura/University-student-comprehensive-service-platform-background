package com.ruoyi.common.utils.map;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * 高德地图地理编码工具类（地址 → 经纬度）
 *
 * @author ruoyi
 */
@Component
public class AMapGeocodeUtil {

    private static final Logger log = LoggerFactory.getLogger(AMapGeocodeUtil.class);

    /** 高德地图地理编码API */
    private static final String GEOCODE_API_URL = "https://restapi.amap.com/v3/geocode/geo";

    @Value("${amap.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 地址转经纬度
     *
     * @param address 详细地址
     * @param city 城市名称（可选，提高精度）
     * @return 经纬度数组 [经度, 纬度]，失败返回 null
     */
    public BigDecimal[] geocode(String address, String city) {
        if (address == null || address.trim().isEmpty()) {
            log.warn("地址为空，无法进行地理编码");
            return null;
        }

        try {
            // 构建请求URL
            String url = String.format("%s?key=%s&address=%s",
                    GEOCODE_API_URL, apiKey, address);

            if (city != null && !city.trim().isEmpty()) {
                url += "&city=" + city;
            }

            log.info("调用高德地图API，地址：{}，城市：{}", address, city);

            // 发起HTTP GET请求
            String response = restTemplate.getForObject(url, String.class);

            if (response == null) {
                log.error("高德地图API返回为空");
                return null;
            }

            // 解析JSON响应
            JSONObject jsonObject = JSONObject.parseObject(response);
            String status = jsonObject.getString("status");

            // status=1 表示成功
            if (!"1".equals(status)) {
                String info = jsonObject.getString("info");
                log.error("高德地图API调用失败，status：{}，info：{}", status, info);
                return null;
            }

            // 获取地理编码结果
            JSONArray geocodes = jsonObject.getJSONArray("geocodes");
            if (geocodes == null || geocodes. isEmpty()) {
                log.warn("未查询到地址的经纬度，地址：{}", address);
                return null;
            }

            // 获取第一个结果的经纬度（格式："经度,纬度"）
            JSONObject firstResult = geocodes.getJSONObject(0);
            String location = firstResult.getString("location");

            if (location == null || location.trim().isEmpty()) {
                log.warn("经纬度为空，地址：{}", address);
                return null;
            }

            // 解析经纬度（格式："106.123456,26.123456"）
            String[] parts = location.split(",");
            if (parts.length != 2) {
                log. error("经纬度格式错误：{}", location);
                return null;
            }

            BigDecimal longitude = new BigDecimal(parts[0]); // 经度
            BigDecimal latitude = new BigDecimal(parts[1]);  // 纬度

            log.info("地理编码成功，地址：{}，经度：{}，纬度：{}", address, longitude, latitude);

            return new BigDecimal[]{longitude, latitude};

        } catch (Exception e) {
            log.error("地理编码失败，地址：{}", address, e);
            return null;
        }
    }

    /**
     * 地址转经纬度（不指定城市）
     *
     * @param address 详细地址
     * @return 经纬度数组 [经度, 纬度]
     */
    public BigDecimal[] geocode(String address) {
        return geocode(address, null);
    }
}