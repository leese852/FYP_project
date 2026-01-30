package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.service.OrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MakingOrderCommand extends AbstractOrderCommand {

    @Autowired
    public MakingOrderCommand(OrderCommandService orderCommandService) {
        super(orderCommandService);
    }

    @Override
    protected boolean canExecute(Integer currentStatus, Integer newStatus) {
        // 制作中(4)可以从已接单(3)转换
        return currentStatus == 3 && newStatus == 4;
    }

    @Override
    protected void executeSpecificLogic(Long orderId) {
        log.info("👨‍🍳 订单 {} 开始制作", orderId);
    }
}