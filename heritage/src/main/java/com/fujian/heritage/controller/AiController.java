package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.entity.AiChatLog;
import com.fujian.heritage.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class AiController {

    @Autowired
    private AiService aiService;

    // 问答接口
    @PostMapping("/ask") // 注意：这里为了兼容前端可能调用的地址，保留 /ask，或者您可以改为 /chat
    public Result<String> chat(@RequestBody Map<String, Object> params) {
        String question = (String) params.get("question");
        Long userId = null;
        if (params.get("userId") != null) {
            userId = Long.valueOf(params.get("userId").toString());
        }
        // 调用统一后的 chat 方法
        return Result.success(aiService.chat(question, userId));
    }

    // 获取日志接口
    @GetMapping("/logs")
    public Result<List<AiChatLog>> listLogs() {
        return Result.success(aiService.getAllLogs());
    }
}