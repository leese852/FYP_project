package com.leese.usercenter.mod4.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderCommandFactory {

    private final Map<Integer, AbstractOrderCommand> commandMap;

    @Autowired
    public OrderCommandFactory(
            PayOrderCommand payOrderCommand,
            AcceptOrderCommand acceptOrderCommand,
            CancelOrderCommand cancelOrderCommand,
            CompleteOrderCommand completeOrderCommand,
            DeliverOrderCommand deliverOrderCommand,
            MakingOrderCommand makingOrderCommand
            // RefundOrderCommand refundOrderCommand // 可选添加
    ) {
        this.commandMap = Map.of(
                2, payOrderCommand,         // 待接单（支付后）
                3, acceptOrderCommand,      // 已接单
                4, makingOrderCommand,      // 制作中
                5, deliverOrderCommand,     // 派送中
                6, completeOrderCommand,    // 已完成
                7, cancelOrderCommand       // 已取消
                // 8, refundOrderCommand     // 退款（可选）
        );
    }

    public AbstractOrderCommand getCommand(Integer status) {
        AbstractOrderCommand command = commandMap.get(status);
        if (command == null) {
            throw new IllegalArgumentException("❌ 不支援的狀態碼: " + status);
        }
        return command;
    }
}