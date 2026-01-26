package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class CancelOrderCommand extends AbstractOrderCommand {
    @Autowired
    public CancelOrderCommand(OrderService orderService) {
        super(orderService);
    }

    @Override
    protected Integer getStatus() {
        return 6; // 已取消
    }
}
