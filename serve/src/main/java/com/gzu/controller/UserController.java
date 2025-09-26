package com.gzu.controller;

import com.gzu.dto.UserLoginDTO;
import com.gzu.dto.UserUpdateDTO;
import com.gzu.entity.Result;
import com.gzu.entity.User;
import com.gzu.properties.JwtProperties;
import com.gzu.service.UserService;
import com.gzu.utils.JwtUtil;
import com.gzu.vo.UserLoginVO;
import com.gzu.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;
    @PostMapping ("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info(userLoginDTO.toString());
        // TODO 登录逻辑
        User user = userService.login(userLoginDTO);
        // 登录成功后返回token
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        // 这里返回token，实际项目中应该返回json数据，包含token信息
        userLoginVO.setToken(token);
        return Result.success(userLoginVO);
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(String password) {
        log.info("update password: " + password);

        userService.updatePassword(password);
        return Result.success();
    }

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/getUserInfo")
    public Result<UserVo> getUserInfo(Integer id) {
        // TODO 获取用户信息
        UserVo userVo = userService.getUserInfo(id);

        return Result.success(userVo);
    }

    @PutMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("update user info: " + userUpdateDTO.toString());
        // TODO 更新用户信息
        userService.updateUserInfo(userUpdateDTO);

        return Result.success();
    }
}
