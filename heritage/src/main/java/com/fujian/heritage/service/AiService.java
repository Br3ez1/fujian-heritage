package com.fujian.heritage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fujian.heritage.entity.AiChatLog;

import java.util.List;

public interface AiService extends IService<AiChatLog> {

    /**
     * AI 对话
     * @param question 用户提问
     * @param userId 用户ID
     * @param type 对话模式：sales-商城导购, general-通用科普
     * @return AI回答
     */
    String chat(String question, Long userId, String type);

    List<AiChatLog> getAllLogs();
}