package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*") // 【关键】允许所有来源跨域访问，解决前端连不上后端的问题
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
        Long userId = 0L;
        String answer = aiService.chat(question, userId, type);
        return Result.success(answer);
    }
}