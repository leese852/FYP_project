package com.leese.usercenter.controller.order;

import com.leese.usercenter.common.BaseResponse;
import com.leese.usercenter.common.ResultUtils;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.model.entity.OrderEntity;
import com.leese.usercenter.model.vo.OrderVO;
import com.leese.usercenter.model.entity.User;
import com.leese.usercenter.service.OrderService;
import com.leese.usercenter.utils.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 獲取當前用戶的訂單列表
     */
    @GetMapping("/user")
    public BaseResponse<List<OrderEntity>> getOrdersByCurrentUser(HttpServletRequest request) {
        User user = AuthUtil.checkUserLogin(request);
        if (user == null || user.getId() == null) {
            log.warn("⚠️ 當前 session 沒有正確的用戶信息");
            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登入或 session 無效");
        }
        log.info("🔍 查詢用戶訂單，用戶信息 = {}", user);
        List<OrderEntity> orders = orderService.findByUserId(user.getId());
        return ResultUtils.success(orders);
    }

    /**
     * 獲取訂單詳情
     */
    @GetMapping("/{orderId}")
    public BaseResponse<OrderVO> getOrderDetails(@PathVariable Long orderId) {
        log.info("📦 查詢訂單詳情，orderId = {}", orderId);
        OrderVO order = orderService.getOrderDetails(orderId);
        return ResultUtils.success(order);
    }

    /**
     * 更新訂單狀態
     */
    @PutMapping("/{orderId}/status")
    public BaseResponse<Void> updateOrderStatus(@PathVariable Long orderId, @RequestParam Integer status) {
        log.info("✏️ 更新訂單狀態，orderId = {}, status = {}", orderId, status);
        orderService.updateOrderStatus(orderId, status);
        return ResultUtils.success();
    }

    /**
     * 測試用：查詢所有訂單
     */
    @GetMapping("/test/all")
    public BaseResponse<List<OrderEntity>> getAllOrders() {
        log.info("🧪 測試查詢所有訂單");
        List<OrderEntity> orders = orderService.findAll();
        return ResultUtils.success(orders);
    }

    /**
     * 測試用：查看當前 session 用戶
     */
    @GetMapping("/test/session")
    public BaseResponse<User> getCurrentSessionUser(HttpServletRequest request) {
        User user = AuthUtil.checkUserLogin(request);
        log.info("🧪 當前 session 用戶信息 = {}", user);
        return ResultUtils.success(user);
    }
}
