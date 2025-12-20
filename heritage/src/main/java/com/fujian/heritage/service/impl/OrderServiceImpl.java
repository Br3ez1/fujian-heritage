package com.fujian.heritage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fujian.heritage.entity.Order;
import com.fujian.heritage.mapper.OrderMapper;
import com.fujian.heritage.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrder(Order order) {
        String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = new Random().nextInt(900) + 100;
        order.setOrderNo("OR" + timeStr + random);
        order.setStatus(1); // 默认待发货
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return baseMapper.insert(order) > 0;
    }

    @Override
    public List<Order> getMyOrders(Long userId) {
        QueryWrapper<Order> query = new QueryWrapper<>();
        query.eq("user_id", userId).orderByDesc("create_time");
        return baseMapper.selectList(query);
    }

    @Override
    public boolean shipOrder(Long orderId) {
        Order order = baseMapper.selectById(orderId);
        if (order != null && order.getStatus() == 1) {
            order.setStatus(2); // 2:已发货
            order.setUpdateTime(LocalDateTime.now());
            return baseMapper.updateById(order) > 0;
        }
        return false;
    }

    @Override
    public boolean completeOrder(Long orderId) {
        Order order = baseMapper.selectById(orderId);
        if (order != null) {
            order.setStatus(3); // 3:已完成
            order.setUpdateTime(LocalDateTime.now());
            return baseMapper.updateById(order) > 0;
        }
        return false;
    }

    // 实现取消订单逻辑
    @Override
    public boolean cancelOrder(Long orderId) {
        Order order = baseMapper.selectById(orderId);
        // 只有 "待支付(0)" 或 "待发货(1)" 的订单可以取消
        if (order != null && (order.getStatus() == 0 || order.getStatus() == 1)) {
            order.setStatus(-1); // -1: 已取消
            order.setUpdateTime(LocalDateTime.now());
            // TODO: 如果有库存系统，这里应该回滚库存
            return baseMapper.updateById(order) > 0;
        }
        return false;
    }
}