package com.leese.usercenter.mod5.controller;

import com.leese.usercenter.mod5.model.dto.PaymentRequest;
import com.leese.usercenter.mod5.model.dto.PaymentResponse;
import com.leese.usercenter.mod5.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 支付控制器
 */
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 处理支付
     */
    @PostMapping("/process")
    public PaymentResponse processPayment(@RequestBody PaymentRequest paymentRequest) {
        // 临时设置用户ID（测试用）
        paymentRequest.setUserId(1L);

        // 手动验证
        if (paymentRequest.getOrderId() == null) {
            PaymentResponse error = new PaymentResponse();
            error.setSuccess(false);
            error.setMessage("订单ID不能为空");
            return error;
        }
        if (paymentRequest.getAmount() == null || paymentRequest.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            PaymentResponse error = new PaymentResponse();
            error.setSuccess(false);
            error.setMessage("支付金额必须大于0");
            return error;
        }
        if (paymentRequest.getPaymentMethod() == null || paymentRequest.getPaymentMethod().trim().isEmpty()) {
            PaymentResponse error = new PaymentResponse();
            error.setSuccess(false);
            error.setMessage("支付方式不能为空");
            return error;
        }

        return paymentService.processPayment(paymentRequest);
    }

    /**
     * 查询订单支付记录
     */
    @GetMapping("/order/{orderId}")
    public PaymentResponse getPaymentByOrderId(@PathVariable Long orderId) {
        return convertToResponse(paymentService.getPaymentByOrderId(orderId));
    }

    /**
     * 模拟支付回调（测试用）
     */
    @PostMapping("/callback/{orderId}")
    public Boolean simulateCallback(@PathVariable Long orderId,
                                    @RequestParam String status) {
        return paymentService.simulatePaymentCallback(orderId, status);
    }

    private PaymentResponse convertToResponse(com.leese.usercenter.mod5.model.entity.PaymentRecord record) {
        if (record == null) {
            return null;
        }

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(record.getId());
        response.setOrderId(record.getOrderId());
        response.setAmount(record.getAmount());
        response.setPaymentMethod(record.getPaymentMethod());
        response.setStatus(record.getStatus());
        response.setTransactionId(record.getTransactionId());
        response.setPaymentTime(record.getCreatedAt());
        response.setSuccess("SUCCESS".equals(record.getStatus()));
        response.setMessage("SUCCESS".equals(record.getStatus()) ? "支付成功" : "支付失败");

        return response;
    }
}