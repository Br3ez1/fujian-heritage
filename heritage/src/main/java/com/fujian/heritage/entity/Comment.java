package com.fujian.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("biz_comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long commentId;
    private Long postId;
    private Long userId;
    private String content;
    private LocalDateTime createTime;

    // 下面这两个字段数据库没有，是用来查询时存用户信息的 (需要联表查或者单独设值)
    @TableField(exist = false)
    private String nickname;
    @TableField(exist = false)
    private String avatar;
}