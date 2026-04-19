package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.service.impl.OrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RefundOrderCommand extends AbstractOrderCommand {

    @Autowired
    public RefundOrderCommand(OrderCommandService orderCommandService) {
        super(orderCommandService);
    }

    @Override
    protected boolean canExecute(Integer currentStatus, Integer newStatus) {
        // 🔥 允许从状态 1-4 转到状态 8（待退款）
        // 顾客取消订单时，从待付款(1)、待接单(2)、已接单(3)、制作中(4)转到待退款(8)
        return (currentStatus >= 1 && currentStatus <= 4) && newStatus == 8;
    }

    @Override
    protected void executeSpecificLogic(Long orderId) {
        log.info("💸 订单 {} 进入待退款状态", orderId);
    }
}