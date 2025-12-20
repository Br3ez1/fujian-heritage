package com.fujian.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("biz_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long orderId;

    private Long userId;
    private String orderNo;       // 订单编号 (如: OR20251219001)
    private BigDecimal totalAmount;

    // 状态: 0:待支付 1:待发货 2:已发货 3:已完成
    private Integer status;

    private String receiverName;  // 收货人
    private String address;       // 收货地址
    private String contact;       // 联系电话

    // 为了简化演示，将购物车商品拼接成字符串存储，如 "白瓷茶具x1, 铁观音x2"
    private String productSummary;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}