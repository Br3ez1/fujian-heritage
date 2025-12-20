package com.fujian.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("biz_heritage")
public class Heritage {
    @TableId(type = IdType.AUTO)
    private Long heritageId;

    private String name;
    private String category;
    private String level;
    private String region;
    private String description;
    private String content;
    private String imgUrl;
    private String videoUrl;
    private String inheritor;
    private String batch;

    // 0: 待审核, 1: 已发布
    private Integer status;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}