package com.gzu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gzu.domain.UserBase;
import com.gzu.mapper.UserMapper;
import com.gzu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserBase getUserById(long userBaseId) {

        return userMapper.selectById(userBaseId);
    }

    @Override
    public List<UserBase> getUserList() {
        QueryWrapper<UserBase> queryWrapper = new QueryWrapper<>();
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteUserById(long userBaseId) {
        userMapper.deleteById(userBaseId);
    }

    @Override
    public void InsertUser(UserBase user) {
        userMapper.insert(user);
    }
}
