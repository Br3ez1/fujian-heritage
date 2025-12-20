package com.fujian.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("biz_activity")
public class Activity {
    @TableId(type = IdType.AUTO)
    private Long activityId;
    private String title;
    private String activityTime; // e.g. "本周六 14:00"
    private String location;
    private Integer maxPeople;
}