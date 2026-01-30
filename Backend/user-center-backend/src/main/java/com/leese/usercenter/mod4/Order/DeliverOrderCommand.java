package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.service.OrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeliverOrderCommand extends AbstractOrderCommand {

    @Autowired
    public DeliverOrderCommand(OrderCommandService orderCommandService) {
        super(orderCommandService);
    }

    @Override
    protected boolean canExecute(Integer currentStatus, Integer newStatus) {
        // 派送中(5)可以从制作中(4)转换
        return currentStatus == 4 && newStatus == 5;
    }

    @Override
    protected void executeSpecificLogic(Long orderId) {
        log.info("🛵 订单 {} 开始派送", orderId);
    }
}