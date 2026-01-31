package com.leese.usercenter.mod4.Order;

import com.leese.usercenter.command.Command;

import com.leese.usercenter.service.impl.OrderCommandService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractOrderCommand implements Command {

    protected final OrderCommandService orderCommandService;

    @Setter
    protected Long orderId;

    @Setter
    protected Integer currentStatus; // 当前状态

    @Setter
    protected Integer newStatus;     // 目标状态

    public AbstractOrderCommand(OrderCommandService orderCommandService) {
        this.orderCommandService = orderCommandService;
    }

    @Override
    public void execute() {
        validate();

        if (!canExecute(currentStatus, newStatus)) {
            throw new IllegalArgumentException(
                    String.format("❌ 无法从状态 %s 转换为状态 %s",
                            getStatusText(currentStatus), getStatusText(newStatus))
            );
        }

        log.info("🔄 执行状态转换: 订单 {} 从 {} 转为 {}",
                orderId, getStatusText(currentStatus), getStatusText(newStatus));

        // 更新订单状态 - 使用 OrderCommandService
        orderCommandService.updateOrderStatusOnly(orderId, newStatus);

        // 执行特定业务逻辑
        executeSpecificLogic(orderId);
    }

    /**
     * 验证状态转换是否允许
     */
    protected abstract boolean canExecute(Integer currentStatus, Integer newStatus);

    /**
     * 执行特定状态转换的业务逻辑
     */
    protected abstract void executeSpecificLogic(Long orderId);

    /**
     * 获取状态文本
     */
    protected String getStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 1: return "待付款";
            case 2: return "待接单";
            case 3: return "已接单";
            case 4: return "制作中";
            case 5: return "派送中";
            case 6: return "已完成";
            case 7: return "已取消";
            case 8: return "退款";
            default: return "未知";
        }
    }

    private void validate() {
        if (orderId == null) {
            throw new IllegalArgumentException("订单ID不能为空");
        }
        if (currentStatus == null || newStatus == null) {
            throw new IllegalArgumentException("状态不能为空");
        }
        if (!orderCommandService.existsOrder(orderId)) {
            throw new IllegalArgumentException("订单不存在: " + orderId);
        }
    }
}