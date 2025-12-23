package com.fujian.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("biz_activity")
public class Activity {
    @TableId(type = IdType.AUTO)
    private Long activityId;

    private String title;

    private String activityTime; // e.g. "本周六 14:00"

    private String location;

    private Integer maxPeople;

    // === 下面是之前缺失的字段，必须补上 ===

    @TableField("img_url")
    private String imgUrl;       // 封面图

    private String description;  // 详情介绍

    private BigDecimal price;    // 费用

    private Integer status;      // 1:上架 0:下架
}