package com.fujian.heritage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fujian.heritage.entity.Order;
import java.util.List;

public interface OrderService extends IService<Order> {
    // 创建订单
    boolean createOrder(Order order);

    // 查询我的订单
    List<Order> getMyOrders(Long userId);

    // 调度：发货
    boolean shipOrder(Long orderId);

    // 调度：完成订单
    boolean completeOrder(Long orderId);

    // 新增：取消订单
    boolean cancelOrder(Long orderId);
}