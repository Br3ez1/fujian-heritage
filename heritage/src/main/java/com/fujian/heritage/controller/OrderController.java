package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.entity.Order;
import com.fujian.heritage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result<String> create(@RequestBody Order order) {
        try {
            boolean success = orderService.createOrder(order);
            return success ? Result.success("下单成功") : Result.error("下单失败");
        } catch (Exception e) {
            return Result.error("下单异常：" + e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result<List<Order>> myOrders(@RequestParam Long userId) {
        return Result.success(orderService.getMyOrders(userId));
    }

    @GetMapping("/all")
    public Result<List<Order>> allOrders() {
        return Result.success(orderService.list());
    }

    @PostMapping("/ship/{id}")
    public Result<String> ship(@PathVariable Long id) {
        boolean success = orderService.shipOrder(id);
        return success ? Result.success("发货成功") : Result.error("状态不正确");
    }

    @PostMapping("/complete/{id}")
    public Result<String> complete(@PathVariable Long id) {
        boolean success = orderService.completeOrder(id);
        return success ? Result.success("订单已完成") : Result.error("操作失败");
    }

    // 取消订单接口
    @PostMapping("/cancel/{id}")
    public Result<String> cancel(@PathVariable Long id) {
        boolean success = orderService.cancelOrder(id);
        return success ? Result.success("订单已取消") : Result.error("取消失败，订单可能已发货");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = orderService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}