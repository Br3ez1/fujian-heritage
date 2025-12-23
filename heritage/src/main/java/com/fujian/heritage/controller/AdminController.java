package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.mapper.*;
import com.fujian.heritage.entity.AiChatLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

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

    @Autowired
    private AiChatLogMapper aiChatLogMapper; // 必须注入这个 Mapper 才能读取对话记录

    /**
     * 获取后台首页统计数据
     */
    @GetMapping("/stats")
    public Result<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("userCount", userMapper.selectCount(null));
        stats.put("productCount", productMapper.selectCount(null));
        stats.put("postCount", postMapper.selectCount(null));
        stats.put("activityCount", activityMapper.selectCount(null));
        return Result.success(stats);
    }

    /**
     * 获取所有用户列表 (用于用户管理板块)
     */
    @GetMapping("/users")
    public Result getUsers() {
        return Result.success(userMapper.selectList(null));
    }

    /**
     * 核心修复：获取所有 AI 对话日志 (用于 AI 监控板块)
     */
    @GetMapping("/ai/logs")
    public Result getAiLogs() {
        // 查询所有 AI 对话记录，建议实际开发中按时间倒序
        List<AiChatLog> logs = aiChatLogMapper.selectList(null);
        return Result.success(logs);
    }

    /**
     * 删除指定的 AI 对话记录
     */
    @DeleteMapping("/ai/log/{id}")
    public Result deleteAiLog(@PathVariable Long id) {
        aiChatLogMapper.deleteById(id);
        return Result.success();
    }
}