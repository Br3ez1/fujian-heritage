package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.entity.User;
import com.fujian.heritage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*") // 允许跨域
public class UserController {

    // 关键修复：注入 UserService
    @Autowired
    private UserService userService;

    // 1. 获取用户列表
    @GetMapping("/list")
    public Result<List<User>> list() {
        // 调用 MyBatis-Plus 提供的 list() 方法
        return Result.success(userService.list());
    }

    // 2. 新增用户
    @PostMapping("/add")
    public Result<String> add(@RequestBody User user) {
        // 设置默认密码
        if (user.getPassword() == null) {
            user.setPassword("123456"); // 实际项目中应加密
        }
        user.setCreateTime(LocalDateTime.now());
        user.setRole(0); // 默认为普通用户

        boolean success = userService.save(user);
        return success ? Result.success("新增成功") : Result.error("新增失败");
    }

    // 3. 修改用户
    @PutMapping("/update")
    public Result<String> update(@RequestBody User user) {
        boolean success = userService.updateById(user);
        return success ? Result.success("修改成功") : Result.error("修改失败");
    }

    // 4. 删除用户
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = userService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}