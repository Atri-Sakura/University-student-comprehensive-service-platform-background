package com.gzu.controller;

import com.gzu.domain.R;
import com.gzu.domain.UserBase;
import com.gzu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{userBaseId}")
    public R<?> getUserById(@PathVariable Long userBaseId) {
        return R.ok(userService.getUserById(userBaseId));
    }

    @GetMapping("/allUsers")
    public R<?> getAllUsers() {

        return R.ok(userService.getUserList());
    }

    @PostMapping("/addUser")
    public R<?> addUser(@RequestBody UserBase userBase) {
        userService.InsertUser(userBase);
        return R.ok();
    }

    @DeleteMapping("/deleteUser/{userBaseId}")
    public R<?> deleteUser(@PathVariable long userBaseId) {
        userService.deleteUserById(userBaseId);
        return R.ok();
    }

}
