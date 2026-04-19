package com.leese.usercenter.controller.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leese.usercenter.common.BaseResponse;
import com.leese.usercenter.common.ResultUtils;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.mapper.CartMapper;
import com.leese.usercenter.model.dto.PlaceOrderDTO;
import com.leese.usercenter.model.entity.Cart;
import com.leese.usercenter.model.entity.OrderEntity;
import com.leese.usercenter.model.vo.OrderVO;
import com.leese.usercenter.model.entity.User;
import com.leese.usercenter.service.OrderService;
import com.leese.usercenter.utils.AuthUtil;
import com.leese.usercenter.mapper.OrderItemMapper;
import com.leese.usercenter.model.entity.OrderItemEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @PostMapping("/place")
    public BaseResponse<OrderEntity> placeOrder(@RequestBody PlaceOrderDTO dto, HttpServletRequest request) {
        User user = AuthUtil.checkUserLogin(request);
        OrderEntity order = orderService.createOrderFromCart(dto, user.getId());
        return ResultUtils.success(order);
    }

    /**
     * 獲取所有訂單（包含菜品詳情）- 用於後台管理
     */
    @GetMapping("/all")
    public BaseResponse<List<OrderVO>> getAllOrdersWithDetails() {
        log.info("📋 獲取所有訂單列表（包含詳情）");
        try {
            List<OrderEntity> orders = orderService.findAll();
            List<OrderVO> orderVOs = orders.stream()
                    .map(order -> orderService.getOrderDetails(order.getId()))
                    .collect(Collectors.toList());
            return ResultUtils.success(orderVOs);
        } catch (Exception e) {
            log.error("❌ 獲取所有訂單失敗", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系統異常");
        }
    }

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
     * 獲取訂單詳情（用主鍵 id 查詢，包含菜品列表）
     */
    @GetMapping("/{id}")
    public BaseResponse<OrderVO> getOrderDetails(@PathVariable Long id) {
        log.info("📦 查詢訂單詳情，id = {}", id);
        OrderVO order = orderService.getOrderDetails(id);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND, "訂單不存在");
        }
        return ResultUtils.success(order);
    }

    /**
     * 更新訂單狀態
     * 🔥 顾客取消订单 → 状态8（待退款）
     * 🔥 管理员取消订单 → 状态7（已取消）
     */
    @PutMapping("/{id}/status")
    public BaseResponse<Void> updateOrderStatus(@PathVariable Long id,
                                                @RequestParam Integer status,
                                                HttpServletRequest request) {
        log.info("✏️ 更新訂單狀態，id = {}, status = {}", id, status);

        // 获取当前用户角色
        User currentUser = AuthUtil.checkUserLogin(request);
        boolean isAdmin = currentUser.getUserRole() == 1;

        Integer finalStatus = status;

        // 如果是取消操作 (status 7)
        if (status == 7) {
            if (!isAdmin) {
                // 普通用户取消：转为状态8（待退款）
                finalStatus = 8;
                log.info("普通用户取消订单，转为待退款状态(8)");
            }
        }

        orderService.updateOrderStatus(id, finalStatus);
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