package com.leese.usercenter.controller.common;

import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.leese.usercenter.config.AlipayConfig;
import com.leese.usercenter.mapper.OrderMapper;
import com.leese.usercenter.model.entity.OrderEntity;
import com.leese.usercenter.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
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

    /**
     * 发起支付宝电脑网站支付
     * 浏览器直接访问此接口，会输出支付宝 HTML 表单并自动跳转支付页面
     */
    @GetMapping("/pay")
    public void pay(@RequestParam("orderId") String orderId,
                    @RequestParam("amount") Double amount,
                    HttpServletResponse response) throws Exception {
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
        alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());

        String bizContent = "{" +
                "\"out_trade_no\":\"" + orderId + "\"," +
                "\"total_amount\":\"" + amount + "\"," +
                "\"subject\":\"外卖订单付款\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"" +
                "}";
        alipayRequest.setBizContent(bizContent);

        String form = alipayClient.pageExecute(alipayRequest).getBody();

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(form);
        out.flush();
        out.close();
    }

    /**
     * 支付宝异步回调通知
     * 验签通过后更新订单支付状态
     */
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
                String outTradeNo = params.get("out_trade_no"); // 订单 orderId 字段值

                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    log.info("订单支付成功！订单号: {}", outTradeNo);

                    // 通过 orderId 字符串查出对应数据库记录
                    QueryWrapper<OrderEntity> query = new QueryWrapper<>();
                    query.eq("orderId", outTradeNo);
                    OrderEntity order = orderMapper.selectOne(query);

                    if (order != null) {
                        // payStatus=1(已支付), status=2(待接单)
                        orderService.updatePaymentStatus(order.getId(), 1, 2);
                        log.info("订单状态更新成功，数据库ID: {}", order.getId());
                    } else {
                        log.warn("未找到订单，orderId: {}", outTradeNo);
                    }
                }
                return "success"; // 告诉支付宝处理完了
            } else {
                log.warn("支付宝回调验签失败");
            }
        } catch (Exception e) {
            log.error("支付宝回调出错", e);
        }
        return "failure";
    }
}

