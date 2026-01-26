package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.service.OrderService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AcceptOrderCommand extends AbstractOrderCommand {
    @Autowired
    public AcceptOrderCommand(OrderService orderService) {
        super(orderService);
    }

    @Override
    protected Integer getStatus() {
        return 3; // 已接單
    }
}
