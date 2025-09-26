package com.gzu.service;


import com.gzu.domain.UserBase;

import java.util.List;

public interface UserService {
    /**
     * 通过ID查询用户id
     * @param id
     * @return
     */
    UserBase getUserById(long id);

    /**
     * 查询所有用户
     * @return
     */
    List<UserBase> getUserList();

    /**
     * 通过ID查询用户
     * @param id
     */
    void deleteUserById(long id);

    /**
     * 增加用户
     * @param user
     */
    void insertUser(UserBase user);
}
