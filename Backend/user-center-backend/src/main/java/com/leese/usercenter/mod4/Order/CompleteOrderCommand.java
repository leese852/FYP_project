package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.service.impl.OrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompleteOrderCommand extends AbstractOrderCommand {

    @Autowired
    public CompleteOrderCommand(OrderCommandService orderCommandService) {
        super(orderCommandService);
    }

    @Override
    protected boolean canExecute(Integer currentStatus, Integer newStatus) {
        // 已完成(6)只能从派送中(5)转换
        return currentStatus == 5 && newStatus == 6;
    }

    @Override
    protected void executeSpecificLogic(Long orderId) {
        log.info("✅ 订单 {} 已完成", orderId);
    }
}