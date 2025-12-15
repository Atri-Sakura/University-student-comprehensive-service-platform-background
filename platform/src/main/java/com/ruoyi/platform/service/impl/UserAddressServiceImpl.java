package com.ruoyi.platform.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserAddressMapper;
import com.ruoyi.platform.domain.UserAddress;
import com.ruoyi.platform.service.IUserAddressService;
import com.ruoyi.common.utils.map.AMapGeocodeUtil;

/**
 * 用户地址Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class UserAddressServiceImpl implements IUserAddressService 
{

    private static final Logger log = LoggerFactory. getLogger(UserAddressServiceImpl.class);

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private AMapGeocodeUtil aMapGeocodeUtil;

    /**
     * 查询用户地址
     * 
     * @param userAddressId 用户地址主键
     * @return 用户地址
     */
    @Override
    public UserAddress selectUserAddressByUserAddressId(Long userAddressId)
    {
        return userAddressMapper.selectUserAddressByUserAddressId(userAddressId);
    }

    /**
     * 查询用户地址列表
     * 
     * @param userAddress 用户地址
     * @return 用户地址
     */
    @Override
    public List<UserAddress> selectUserAddressList(UserAddress userAddress)
    {
        return userAddressMapper.selectUserAddressList(userAddress);
    }

    /**
     * 新增用户地址
     *
     * @param userAddress 用户地址
     * @return 结果
     */
    @Override
    public int insertUserAddress(UserAddress userAddress)
    {
        userAddress.setCreateTime(DateUtils.getNowDate());

        // 自动转换地址为经纬度
        // 1. 拼接完整地址
        String fullAddress = buildFullAddress(userAddress);

        // 2. 调用高德地图API获取经纬度
        try {
            BigDecimal[] location = aMapGeocodeUtil. geocode(fullAddress, userAddress.getCity());

            if (location != null && location.length == 2) {
                userAddress.setLongitude(location[0]);
                userAddress.setLatitude(location[1]);
                log.info("用户地址转经纬度成功，地址：{}，经度：{}，纬度：{}",
                        fullAddress, location[0], location[1]);
            } else {
                log. warn("用户地址转经纬度失败，地址：{}，将使用空值", fullAddress);
                // 允许经纬度为空，不阻止地址添加
            }
        } catch (Exception e) {
            log.error("用户地址转经纬度异常，地址：{}", fullAddress, e);
            // 异常时也不阻止地址添加
        }

        // 查询该用户已经有多少收货地址
        UserAddress query = new UserAddress();
        query.setUserBaseId(userAddress.getUserBaseId());
        List<UserAddress> existList = userAddressMapper.selectUserAddressList(query);

        // 如果没有其他地址，自动设为默认
        if (existList == null || existList.isEmpty()) {
            userAddress.setIsDefault(1L);
        } else {
            if (userAddress.getIsDefault() == null) {
                userAddress.setIsDefault(0L);
            }
        }

        return userAddressMapper.insertUserAddress(userAddress);
    }

    /**
     * 修改用户地址
     *
     * @param userAddress 用户地址
     * @return 结果
     */
    @Override
    public int updateUserAddress(UserAddress userAddress)
    {
        userAddress.setUpdateTime(DateUtils.getNowDate());

        // 如果地址字段有变化，重新转换经纬度
        // 判断是否需要重新转换（如果省市区或详细地址有变化）
        if (needGeocodeUpdate(userAddress)) {
            String fullAddress = buildFullAddress(userAddress);

            try {
                BigDecimal[] location = aMapGeocodeUtil. geocode(fullAddress, userAddress.getCity());

                if (location != null && location. length == 2) {
                    userAddress.setLongitude(location[0]);
                    userAddress.setLatitude(location[1]);
                    log. info("用户地址更新后重新转经纬度成功，地址：{}，经度：{}，纬度：{}",
                            fullAddress, location[0], location[1]);
                } else {
                    log.warn("用户地址更新后转经纬度失败，地址：{}", fullAddress);
                }
            } catch (Exception e) {
                log.error("用户地址更新后转经纬度异常，地址：{}", fullAddress, e);
            }
        }

        return userAddressMapper.updateUserAddress(userAddress);
    }

    /**
     * 批量删除用户地址
     * 
     * @param userAddressIds 需要删除的用户地址主键
     * @return 结果
     */
    @Override
    public int deleteUserAddressByUserAddressIds(Long[] userAddressIds)
    {
        // 查询这些地址中有没有默认
        boolean hasDefault = false;
        Long userBaseId = null;
        for (Long id : userAddressIds) {
            UserAddress addr = userAddressMapper.selectUserAddressByUserAddressId(id);
            if (addr != null && addr.getIsDefault() != null && addr.getIsDefault() == 1L) {
                hasDefault = true;
                userBaseId = addr.getUserBaseId();
            }
        }

        int result = userAddressMapper.deleteUserAddressByUserAddressIds(userAddressIds);

        // 删除后，若无地址直接返回
        if (hasDefault && userBaseId != null) {
            // 查询剩余地址
            UserAddress query = new UserAddress();
            query.setUserBaseId(userBaseId);
            List<UserAddress> remain = userAddressMapper.selectUserAddressList(query);
            if (remain != null && !remain.isEmpty()) {
                // 找最新的地址（按 create_time 最大）
                UserAddress latest = remain.get(0);
                for (UserAddress a : remain) {
                    if (latest.getCreateTime().before(a.getCreateTime())) {
                        latest = a;
                    }
                }
                // 批量先全部设为非默认
                userAddressMapper.updateAllDefaultToZero(userBaseId);
                // 设置最新为默认
                UserAddress upd = new UserAddress();
                upd.setUserAddressId(latest.getUserAddressId());
                upd.setIsDefault(1L);
                userAddressMapper.updateUserAddress(upd);
            }
        }
        return result;
    }

    /**
     * 删除用户地址信息
     * 
     * @param userAddressId 用户地址主键
     * @return 结果
     */
    @Override
    public int deleteUserAddressByUserAddressId(Long userAddressId)
    {
        return userAddressMapper.deleteUserAddressByUserAddressId(userAddressId);
    }

    @Override
    public void updateAllDefaultToZero(Long userBaseId) {
        userAddressMapper.updateAllDefaultToZero(userBaseId);
    }

    /**
     * 拼接完整地址
     *
     * @param userAddress 用户地址对象
     * @return 完整地址字符串
     */
    private String buildFullAddress(UserAddress userAddress) {
        StringBuilder sb = new StringBuilder();

        if (userAddress. getProvince() != null && !userAddress.getProvince().isEmpty()) {
            sb.append(userAddress.getProvince());
        }
        if (userAddress.getCity() != null && !userAddress.getCity().isEmpty()) {
            sb.append(userAddress.getCity());
        }
        if (userAddress.getDistrict() != null && !userAddress.getDistrict().isEmpty()) {
            sb.append(userAddress. getDistrict());
        }
        if (userAddress.getDetailAddress() != null && !userAddress.getDetailAddress().isEmpty()) {
            sb.append(userAddress.getDetailAddress());
        }

        return sb.toString();
    }

    /**
     * 判断是否需要重新进行地理编码
     *
     * @param userAddress 更新的地址对象
     * @return true-需要重新转换，false-不需要
     */
    private boolean needGeocodeUpdate(UserAddress userAddress) {
        // 如果省市区或详细地址有值，说明可能修改了地址，需要重新转换
        return (userAddress.getProvince() != null && !userAddress.getProvince().isEmpty()) ||
                (userAddress.getCity() != null && !userAddress.getCity().isEmpty()) ||
                (userAddress.getDistrict() != null && !userAddress.getDistrict().isEmpty()) ||
                (userAddress.getDetailAddress() != null && !userAddress.getDetailAddress().isEmpty());
    }
}
