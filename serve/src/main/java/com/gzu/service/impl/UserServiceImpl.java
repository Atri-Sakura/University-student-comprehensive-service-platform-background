package com.gzu.service.impl;

import com.gzu.context.BaseContext;
import com.gzu.dto.UserLoginDTO;
import com.gzu.dto.UserUpdateDTO;
import com.gzu.entity.User;
import com.gzu.exception.UserLoginException;
import com.gzu.exception.UserSelectException;
import com.gzu.mapper.UserMapper;
import com.gzu.service.UserService;
import com.gzu.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> list() {
        return null;
    }

    /**
     * 登录
     * @param userLoginDTO
     * @return
     */
    public User login(UserLoginDTO userLoginDTO) {
         // 登录逻辑
        String userId = userLoginDTO.getStudentId();
        String password= userLoginDTO.getPassword();

        //根据学号查询学生
        User user = userMapper.getByUsertId(userId);
        if (user==null){
            throw new UserLoginException("账号不存在");
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())){
            throw new UserLoginException("密码错误");
        }

        return user;
    }

    @Override
    public void updatePassword(String password) {
        Integer id = BaseContext.getCurrentId();
        User user = new User();
        user.setId(BaseContext.getCurrentId());
        user.setUpdateTime(LocalDateTime.now());
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userMapper.updatePdById(user);

    }

    @Override
    public UserVo getUserInfo(Integer id) {
        Integer id1 = BaseContext.getCurrentId();
        if (id1!=id){
            throw new UserSelectException("权限不足");
        }
        User user = userMapper.getById(id);
        if (user==null){
            throw new UserSelectException("用户不存在");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }

    @Override
    public void updateUserInfo(UserUpdateDTO userUpdateDTO) {
        Integer id = BaseContext.getCurrentId();
        if (id != userUpdateDTO.getId()){
            throw new UserSelectException("权限不足");
        }
        User user = new User();
        user.setQQ(userUpdateDTO.getQqNumber());
        BeanUtils.copyProperties(userUpdateDTO,user);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updatePdById(user);


    }
}
