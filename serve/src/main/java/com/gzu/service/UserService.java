package com.gzu.service;

import com.gzu.dto.UserLoginDTO;
import com.gzu.dto.UserUpdateDTO;
import com.gzu.entity.User;
import com.gzu.vo.UserVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    List<User> list();

    /**
     * 登录
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * 更新密码
     * @param password
     */
    void updatePassword(String password);

    /**
     * 获取用户信息
     * @param id
     */
    UserVo getUserInfo(Integer id);

    /**
     * 更新用户信息
     * @param userUpdateDTO
     */
    void updateUserInfo(UserUpdateDTO userUpdateDTO);
}
