package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.dto.LoginRequest;
import com.fujian.heritage.dto.RegisterRequest;
import com.fujian.heritage.entity.User;
import com.fujian.heritage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request);
            // 登录成功，返回用户信息（实际项目中这里会返回 Token）
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}