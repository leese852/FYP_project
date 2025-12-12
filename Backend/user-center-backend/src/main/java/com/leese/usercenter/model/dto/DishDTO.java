package com.leese.usercenter.model.dto;

import com.leese.usercenter.model.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor //无参构造
@AllArgsConstructor //有参构造
@Data
public class DishDTO implements Serializable {
    private Integer id;
    private String dishName;
    private BigDecimal price;
    private Integer categoryId;
    private String description;
    private byte[] imgUrl;

    // 多种口味，包括温度，忌口等(每种口味又对应一个列表)，且数据在口味表中而不是在Dish里，口味表有外键关联Dish
    private List<DishFlavor> flavors = new ArrayList<>();
}
