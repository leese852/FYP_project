package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.service.OrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AcceptOrderCommand extends AbstractOrderCommand {

    @Autowired
    public AcceptOrderCommand(OrderCommandService orderCommandService) {
        super(orderCommandService);
    }

    @Override
    protected boolean canExecute(Integer currentStatus, Integer newStatus) {
        // 已接单(3)只能从待接单(2)转换
        return currentStatus == 2 && newStatus == 3;
    }

    @Override
    protected void executeSpecificLogic(Long orderId) {
        log.info("📞 订单 {} 已被接单，通知厨房准备", orderId);
    }
}