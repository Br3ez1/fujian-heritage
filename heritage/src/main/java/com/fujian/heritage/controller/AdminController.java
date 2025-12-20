package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.mapper.ActivityMapper;
import com.fujian.heritage.mapper.PostMapper;
import com.fujian.heritage.mapper.ProductMapper;
import com.fujian.heritage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 获取后台首页统计数据
     */
    @GetMapping("/stats")
    public Result<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();

        // 使用 MyBatis-Plus 的 selectCount 方法
        stats.put("userCount", userMapper.selectCount(null));
        stats.put("productCount", productMapper.selectCount(null));
        stats.put("postCount", postMapper.selectCount(null));
        stats.put("activityCount", activityMapper.selectCount(null));

        return Result.success(stats);
    }
}