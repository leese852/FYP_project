package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.service.impl.OrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CancelOrderCommand extends AbstractOrderCommand {

    @Autowired
    public CancelOrderCommand(OrderCommandService orderCommandService) {
        super(orderCommandService);
    }

    @Override
    protected boolean canExecute(Integer currentStatus, Integer newStatus) {
        // 已取消(7)可以从多个状态转换
        // 待付款(1)、待接单(2)、已接单(3)、制作中(4)都可以取消
        return (currentStatus >= 1 && currentStatus <= 4) && newStatus == 7;
    }

    @Override
    protected void executeSpecificLogic(Long orderId) {
        log.info("❌ 订单 {} 已取消", orderId);
    }
}