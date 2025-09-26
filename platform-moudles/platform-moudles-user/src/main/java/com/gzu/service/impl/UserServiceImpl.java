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

    /**
     * 通过用户ID查询用户
     * @param userBaseId
     * @return
     */
    @Override
    public UserBase getUserById(long userBaseId) {

        return userMapper.selectById(userBaseId);
    }

    /**
     * 查询所有用户
     * @return
     */
    @Override
    public List<UserBase> getUserList() {
        QueryWrapper<UserBase> queryWrapper = new QueryWrapper<>();
        return userMapper.selectList(queryWrapper);
    }

    /**
     * 通过ID删除用户
     * @param userBaseId
     */
    @Override
    public void deleteUserById(long userBaseId) {
        userMapper.deleteById(userBaseId);
    }

    /**
     * 增加用户
     * @param user
     */
    @Override
    public void insertUser(UserBase user) {
        userMapper.insert(user);
    }
}
