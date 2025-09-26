package com.gzu.service;


import com.gzu.domain.UserBase;

import java.util.List;

public interface UserService {

    UserBase getUserById(long id);

    List<UserBase> getUserList();

    void deleteUserById(long id);

    void InsertUser(UserBase user);
}
