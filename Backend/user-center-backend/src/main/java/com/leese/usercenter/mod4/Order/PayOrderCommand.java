package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.service.OrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayOrderCommand extends AbstractOrderCommand {

    @Autowired
    public PayOrderCommand(OrderCommandService orderCommandService) {
        super(orderCommandService);
    }

    @Override
    protected boolean canExecute(Integer currentStatus, Integer newStatus) {
        // 支付后从待付款(1)转为待接单(2)
        return currentStatus == 1 && newStatus == 2;
    }

    @Override
    protected void executeSpecificLogic(Long orderId) {
        log.info("💰 订单 {} 已支付，执行支付逻辑", orderId);
        // 这里可以添加支付相关逻辑
        // 注意：如果需要调用其他Service，可以在这里注入
    }
}