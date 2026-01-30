package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.service.OrderCommandService;
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
        // 退款(8)可以从已取消(7)转换
        return currentStatus == 7 && newStatus == 8;
    }

    @Override
    protected void executeSpecificLogic(Long orderId) {
        log.info("💸 订单 {} 退款处理", orderId);
    }
}