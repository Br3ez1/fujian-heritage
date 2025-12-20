package com.fujian.heritage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fujian.heritage.dto.LoginRequest;
import com.fujian.heritage.dto.RegisterRequest;
import com.fujian.heritage.entity.User;

public interface UserService extends IService<User> {
    User login(LoginRequest request);
    User register(RegisterRequest request);
}