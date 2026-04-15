package com.leese.usercenter.controller.common;

import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leese.usercenter.config.AlipayConfig;
import com.leese.usercenter.mapper.OrderMapper;
import com.leese.usercenter.model.entity.OrderEntity;
import com.leese.usercenter.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/alipay")
public class AlipayController {
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/pay")
    public String pay(@RequestParam("orderId") String orderId, @RequestParam("amount") Double amount)throws Exception{
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        //配置写在了yml文件里
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
        alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());

        String bizContent = "{" +
                "\"out_trade_no\":\"" + orderId + "\"," + // 直接用你的 orderId
                "\"total_amount\":\"" + amount + "\"," +
                "\"subject\":\"外卖订单付款\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"" +
                "}";

        alipayRequest.setBizContent(bizContent);

        // 返回一段 HTML Form 代码，前端拿到渲染即可跳支付宝
        return alipayClient.pageExecute(alipayRequest).getBody();
    }

    @PostMapping("/notify")
    public String notifyCall(HttpServletRequest request) {
        log.info("--- 收到支付宝回调 ---");
        try {
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, requestParams.get(name)[0]);
            }

            // 验签
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params, alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getCharset(), alipayConfig.getSignType()
            );

            if (signVerified) {
                String tradeStatus = params.get("trade_status");
                String outTradeNo = params.get("out_trade_no"); // 这就是 orderId (比如 ORD123456)

                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    log.info("订单支付成功！订单号: {}", outTradeNo);

                    // 1. 通过字符串 orderId 查出对应的数据库实体
                    com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.leese.usercenter.model.entity.OrderEntity> query =
                            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
                    query.eq("orderId", outTradeNo);

                    com.leese.usercenter.model.entity.OrderEntity order = orderMapper.selectOne(query);
                    orderService.updatePaymentStatus(order.getId(),1,2);

                }
                return "success"; // 告诉支付宝处理完了
            }
        } catch (Exception e) {
            log.error("支付宝回调出错", e);
        }
        return "failure";
    }
}
