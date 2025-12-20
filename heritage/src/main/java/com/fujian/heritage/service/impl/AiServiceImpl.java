package com.fujian.heritage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fujian.heritage.entity.AiChatLog;
import com.fujian.heritage.mapper.AiChatLogMapper;
import com.fujian.heritage.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiServiceImpl extends ServiceImpl<AiChatLogMapper, AiChatLog> implements AiService {

    @Autowired
    private AiChatLogMapper aiChatLogMapper;

    // DeepSeek API 配置
    private static final String API_URL = "https://api.deepseek.com/chat/completions";
    // 您的 Key
    private static final String API_KEY = "sk-fd588a7fba864b89bfb91ccf8dd7aa6e";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String chat(String question, Long userId) {
        String answer;
        try {
            // 尝试调用真实 API
            answer = callAiApi(question);
        } catch (Exception e) {
            e.printStackTrace();
            answer = "API_ERROR: " + e.getMessage();
        }

        // 记录日志
        try {
            AiChatLog log = new AiChatLog();
            log.setUserId(userId);
            log.setQuestion(question);
            log.setAnswer(answer.startsWith("API_ERROR") ? "服务暂时不可用" : answer);
            log.setCreateTime(LocalDateTime.now());
            aiChatLogMapper.insert(log);
        } catch (Exception e) {
            System.err.println("日志入库失败: " + e.getMessage());
        }

        return answer;
    }

    @Override
    public List<AiChatLog> getAllLogs() {
        QueryWrapper<AiChatLog> query = new QueryWrapper<>();
        query.orderByDesc("create_time");
        return aiChatLogMapper.selectList(query);
    }

    private String callAiApi(String question) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setDoOutput(true);
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(60000);

        // 构造请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("stream", false);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> sysMsg = new HashMap<>();
        sysMsg.put("role", "system");
        sysMsg.put("content", "你是一个精通福建非物质文化遗产的智能助手。");
        messages.add(sysMsg);

        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", question);
        messages.add(userMsg);

        requestBody.put("messages", messages);

        String jsonInputString = objectMapper.writeValueAsString(requestBody);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            // 读取错误流
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) errorResponse.append(line);
                throw new RuntimeException("API Error " + responseCode + ": " + errorResponse.toString());
            }
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) response.append(line.trim());
        }

        JsonNode rootNode = objectMapper.readTree(response.toString());
        if (rootNode.has("choices") && rootNode.get("choices").size() > 0) {
            return rootNode.get("choices").get(0).get("message").get("content").asText();
        } else {
            return "无法解析AI返回的数据";
        }
    }
}