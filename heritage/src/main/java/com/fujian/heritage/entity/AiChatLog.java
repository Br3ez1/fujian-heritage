package com.fujian.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("log_ai_chat")
public class AiChatLog {
    @TableId(type = IdType.AUTO)
    private Long logId;
    private Long userId;      // 提问用户ID (游客可为null)
    private String question;  // 问题
    private String answer;    // AI回答
    private LocalDateTime createTime;
}