package com.leese.usercenter.mod5.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leese.usercenter.mod5.mapper.PaymentRecordMapper;
import com.leese.usercenter.mod5.model.dto.PaymentRequest;
import com.leese.usercenter.mod5.model.dto.PaymentResponse;
import com.leese.usercenter.mod5.model.entity.PaymentRecord;
import com.leese.usercenter.mod5.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 支付服务实现
 */
@Service
@Slf4j
public class PaymentServiceImpl extends ServiceImpl<PaymentRecordMapper, PaymentRecord>
        implements PaymentService {

    // 支付成功率配置（可配置化）
    private static final double SUCCESS_RATE = 0.85;
    private final Random random = new Random();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        // 1. 创建支付记录
        PaymentRecord paymentRecord = new PaymentRecord();
        BeanUtils.copyProperties(paymentRequest, paymentRecord);
        paymentRecord.setUserId(paymentRequest.getUserId());
        paymentRecord.setCreatedAt(new Date());

        // 2. 模拟支付处理
        boolean isSuccess = simulatePaymentProcessing(paymentRequest.getPaymentMethod());

        if (isSuccess) {
            paymentRecord.setStatus("SUCCESS");
            paymentRecord.setTransactionId("TXN_" + UUID.randomUUID().toString().substring(0, 8));
            log.info("支付成功，订单ID: {}, 交易ID: {}", paymentRequest.getOrderId(), paymentRecord.getTransactionId());
        } else {
            paymentRecord.setStatus("FAILED");
            paymentRecord.setTransactionId(null);
            log.warn("支付失败，订单ID: {}", paymentRequest.getOrderId());
        }

        // 3. 保存支付记录
        this.save(paymentRecord);

        // 4. 调用订单模块更新订单状态（TODO: 需要与模块三对接）
        // updateOrderStatus(paymentRequest.getOrderId(), isSuccess ? "PAID" : "PAYMENT_FAILED");

        // 5. 返回支付响应
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(paymentRecord.getId());
        response.setOrderId(paymentRecord.getOrderId());
        response.setAmount(paymentRecord.getAmount());
        response.setPaymentMethod(paymentRecord.getPaymentMethod());
        response.setStatus(paymentRecord.getStatus());
        response.setTransactionId(paymentRecord.getTransactionId());
        response.setPaymentTime(paymentRecord.getCreatedAt());
        response.setSuccess(isSuccess);
        response.setMessage(isSuccess ? "支付成功" : "支付失败，请重试或选择其他支付方式");

        return response;
    }

    /**
     * 模拟支付处理逻辑
     */
    private boolean simulatePaymentProcessing(String paymentMethod) {
        // 现金支付总是成功
        if ("CASH".equalsIgnoreCase(paymentMethod)) {
            return true;
        }

        // 其他支付方式按成功率随机决定
        return random.nextDouble() < SUCCESS_RATE;
    }

    @Override
    public PaymentRecord getPaymentByOrderId(Long orderId) {
        QueryWrapper<PaymentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId)
                .orderByDesc("created_at")
                .last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<PaymentRecord> getPaymentsByUserId(Long userId) {
        QueryWrapper<PaymentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("created_at");
        return this.list(queryWrapper);
    }

    @Override
    public Boolean simulatePaymentCallback(Long orderId, String status) {
        PaymentRecord paymentRecord = getPaymentByOrderId(orderId);
        if (paymentRecord == null) {
            return false;
        }

        paymentRecord.setStatus(status.toUpperCase());
        if ("SUCCESS".equals(status)) {
            paymentRecord.setTransactionId("CALLBACK_TXN_" + UUID.randomUUID().toString().substring(0, 8));
        }

        return this.updateById(paymentRecord);
    }
}