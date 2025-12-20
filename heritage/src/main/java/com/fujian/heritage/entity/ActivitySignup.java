package com.fujian.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("biz_activity_signup")
public class ActivitySignup {
    @TableId(type = IdType.AUTO)
    private Long signupId;
    private Long activityId;
    private Long userId;
    private Integer status; // 0:已提交
    private LocalDateTime createTime;
}