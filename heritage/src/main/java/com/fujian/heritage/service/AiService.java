package com.fujian.heritage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fujian.heritage.entity.AiChatLog;
import java.util.List;

public interface AiService extends IService<AiChatLog> {
    /**
     * AI 智能问答
     * @param question 用户问题
     * @param userId 用户ID (可选)
     * @return AI 的回答
     */
    String chat(String question, Long userId);

    /**
     * 获取所有聊天日志
     * @return 日志列表
     */
    List<AiChatLog> getAllLogs();
}