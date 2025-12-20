package com.fujian.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("biz_post")
public class Post {
    @TableId(type = IdType.AUTO)
    private Long postId;
    private Long userId;
    private String title;
    private String content;
    private String tags;
    private Integer likes;
    private LocalDateTime createTime;

    // 以下字段数据库表中没有，仅用于前端展示，需要 Service 层填充
    @TableField(exist = false)
    private String author;
    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private Integer views = 0;
    @TableField(exist = false)
    private Integer comments = 0;
}