package com.gzu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gzu.domain.UserAddress;
import com.gzu.mapper.UserAddressMapper;
import com.gzu.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public UserAddress getAddressById(long user_address_id) {
        return userAddressMapper.selectById(user_address_id);

    }

    @Override
    public List<UserAddress> getAddressList() {
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
        return userAddressMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteAddressById(long user_address_id) {
        userAddressMapper.deleteById(user_address_id);

    }

    @Override
    public void insertAddress(UserAddress address) {
        userAddressMapper.insert(address);
    }


}
