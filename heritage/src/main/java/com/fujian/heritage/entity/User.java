package com.fujian.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_user") // 指定对应的数据库表名
public class User {
    @TableId(type = IdType.AUTO) // 指定主键自增
    private Long userId;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private Integer role; // 0:普通用户 1:管理员
    private LocalDateTime createTime;
}
