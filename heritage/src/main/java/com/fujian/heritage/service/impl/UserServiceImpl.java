package com.fujian.heritage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fujian.heritage.dto.LoginRequest;
import com.fujian.heritage.dto.RegisterRequest;
import com.fujian.heritage.entity.User;
import com.fujian.heritage.mapper.UserMapper;
import com.fujian.heritage.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(LoginRequest request) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", request.getUsername());
        // 实际项目中密码应该加密（如 BCrypt），这里演示明文对比
        query.eq("password", request.getPassword());

        User user = baseMapper.selectOne(query);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public User register(RegisterRequest request) {
        // 1. 检查用户名是否重复
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", request.getUsername());
        if (baseMapper.selectCount(query) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // 同样，实际应加密
        user.setNickname(request.getNickname());
        user.setRole(0); // 默认为普通用户
        user.setCreateTime(LocalDateTime.now());

        baseMapper.insert(user);
        return user;
    }
}