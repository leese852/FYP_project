package com.leese.usercenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
@Data
public class PlaceOrderDTO {
    private Integer addressId;
    private List<Integer> cartIds;
}
