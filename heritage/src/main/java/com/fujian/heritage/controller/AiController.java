package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.entity.AiChatLog; // [新增] 导入实体类
import com.fujian.heritage.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List; // [新增] 导入List

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*") // 允许跨域
public class AiController {

    @Autowired
    private AiService aiService;

    /**
     * AI 对话接口
     * @param question 问题内容
     * @param type 场景类型 (sales / general)
     * @return 回答
     */
    @GetMapping("/chat")
    public Result<String> chat(@RequestParam String question,
                               @RequestParam(required = false, defaultValue = "general") String type) {
        // 这里暂时用 0L 代表匿名/默认用户，后续可对接真实用户ID
        Long userId = 0L;
        String answer = aiService.chat(question, userId, type);
        return Result.success(answer);
    }

    /**
     * [新增] 获取 AI 对话日志列表
     * 对应前端请求: GET /ai/logs
     */
    @GetMapping("/logs")
    public Result<List<AiChatLog>> getLogs() {
        // 调用您已经在 AiServiceImpl 中写好的 getAllLogs 方法
        List<AiChatLog> list = aiService.getAllLogs();
        return Result.success(list);
    }
}