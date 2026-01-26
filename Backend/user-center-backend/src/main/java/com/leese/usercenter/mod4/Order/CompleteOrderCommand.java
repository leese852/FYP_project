package com.leese.usercenter.mod4.Order;


import com.leese.usercenter.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class CompleteOrderCommand extends AbstractOrderCommand {
    @Autowired
    public CompleteOrderCommand(OrderService orderService) {
        super(orderService);
    }

    @Override
    protected String getStatus() {
        return "Completed";
    }
}
