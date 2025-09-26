package com.gzu.service;

import com.gzu.domain.UserAddress;

import java.util.List;

public interface UserAddressService {
    /**
     * 查询地址
     *
     * @param user_address_id
     * @return
     */
    UserAddress getAddressById(long user_address_id);

    /**
     * 查询所有地址
     *
     * @return
     */
    List<UserAddress> getAddressList();

    /**
     * 根据ID删除地址
     * @param user_address_id
     */
    void deleteAddressById(long user_address_id);

    /**
     * 增加用户地址
     * @param address
     */
    void insertAddress(UserAddress address);
}
