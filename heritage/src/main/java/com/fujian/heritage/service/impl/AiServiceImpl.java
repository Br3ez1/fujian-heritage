package com.fujian.heritage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fujian.heritage.entity.AiChatLog;
import com.fujian.heritage.entity.Product;
import com.fujian.heritage.mapper.AiChatLogMapper;
import com.fujian.heritage.mapper.ProductMapper;
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

    @Autowired
    private ProductMapper productMapper;

    // DeepSeek API 配置
    private static final String API_URL = "https://api.deepseek.com/chat/completions";
    // 请替换为您自己的真实 Key
    private static final String API_KEY = "sk-fd588a7fba864b89bfb91ccf8dd7aa6e";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String chat(String question, Long userId, String type) {
        String answer;
        try {
            answer = callAiApi(question, type);
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
            // 如果数据库表里有 type 字段可以保存，没有则忽略
            // log.setType(type);
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

    private String callAiApi(String question, String type) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setDoOutput(true);
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(60000);

        String systemPrompt;

        // 核心：根据 type 切换人设
        if ("sales".equals(type)) {
            // === 模式一：金牌销售 (带商品库) ===
            List<Product> products = productMapper.selectList(null);
            StringBuilder productContext = new StringBuilder();
            productContext.append("这是商城目前在售的商品列表（请基于此列表进行推荐）：\n");
            if (products != null) {
                for (Product p : products) {
                    // Removed p.getDescription() as the method does not exist in the Product entity
                    productContext.append(String.format("- 商品名：%s | 价格：%.2f元 | 分类：%s\n",
                            p.getName(), p.getPrice(), p.getCategory()));
                }
            }
            systemPrompt = "你是一个【福建非遗文创商城的金牌销售员】。你不仅精通福建非物质文化遗产知识，还非常熟悉店内的商品。" +
                    "你的任务是：\n" +
                    "1. 热情地回答客户关于商品的问题，结合非遗文化背景进行介绍（例如介绍德化白瓷时，提到其'中国白'的艺术地位）。\n" +
                    "2. 如果用户让推荐礼物，请从上面的【在售商品列表】中挑选合适的进行推荐，不要虚构商品。\n" +
                    "3. 语气要亲切、专业，富有感染力，适当使用表情符号。\n" +
                    "4. 如果商品不在列表中，礼貌告知暂时缺货并推荐类似商品。\n\n" +
                    productContext.toString();
        } else {
            // === 模式二：文化科普专家 (纯净模式 - 用于首页、非遗名录等) ===
            systemPrompt = "你是一个【福建非物质文化遗产科普专家】。" +
                    "你的任务是为用户科普福建地区的非遗文化（如南音、漆艺、寿山石雕、茶文化等）。" +
                    "请注意：\n" +
                    "1. 回答要引经据典，具有文化深度，语言优美。\n" +
                    "2. 专注于历史、工艺、艺术价值的讲解，不要进行商业推销。\n" +
                    "3. 即使提到相关器物，也侧重于其艺术鉴赏价值而非价格。";
        }

        // 构造请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("stream", false);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> sysMsg = new HashMap<>();
        sysMsg.put("role", "system");
        sysMsg.put("content", systemPrompt);
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