package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.command.Command;
import com.leese.usercenter.service.OrderService;

public abstract class AbstractOrderCommand extends Command {
    protected final OrderService orderService;
    protected Long orderId;

    public AbstractOrderCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    // ✅ 改成 Integer
    protected abstract Integer getStatus();

    @Override
    public void execute() {
        orderService.updateOrderStatus(orderId, getStatus());
    }
}
