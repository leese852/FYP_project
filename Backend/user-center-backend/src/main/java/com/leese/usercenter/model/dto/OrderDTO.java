package com.leese.usercenter.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
// OrderDTO.java
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private Timestamp orderTime;
    private String status;
    private Integer totalAmount;
    private String rideAddress;
    private String orderComment;
    private Long employeeId;
    private List<OrderItemDTO> items; // 订单项列表
}
