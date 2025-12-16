package com.leese.usercenter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leese.usercenter.model.entity.Dish;
import com.leese.usercenter.model.entity.DishFlavor;
import com.leese.usercenter.model.dto.DishDTO;
import com.leese.usercenter.model.vo.DishVO;
import com.leese.usercenter.service.DishFlavorService;
import com.leese.usercenter.service.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
//@Transactional // 自动回滚，避免污染数据库
public class DishServiceTest {
    private static final Logger log = LoggerFactory.getLogger(DishServiceTest.class);
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    private Dish originalDish;
    @BeforeEach
    void setUp(){
        // 插入一个原始菜品（带口味）
        originalDish = new Dish();
        originalDish.setDishName("老版宫保鸡丁");
        originalDish.setPrice(new BigDecimal("35.00"));
        originalDish.setCategoryId(1);
        originalDish.setIsAvailable(1);
        originalDish.setIsDelete(0);
        dishService.save(originalDish);

        // 插入旧口味
        DishFlavor oldFlavor1 = new DishFlavor();
        oldFlavor1.setTag("辣度");
        oldFlavor1.setList("微辣");
        oldFlavor1.setDishId(originalDish.getId());
        dishFlavorService.save(oldFlavor1);

        DishFlavor oldFlavor2 = new DishFlavor();
        oldFlavor2.setTag("甜度");
        oldFlavor2.setList("正常");
        oldFlavor2.setDishId(originalDish.getId());
        dishFlavorService.save(oldFlavor2);
    }
    @Test
    public void testAddDish(){
        DishDTO dish = new DishDTO();
        dish.setDishName("Leese");
        dish.setPrice(new BigDecimal("58.00"));
        dish.setCategoryId(2);
        dish.setDescription("新菜品");
        dish.setImgUrl(null); // 测试中可为 null

        DishFlavor flavor1 = new DishFlavor();
        flavor1.setTag("甜度");
        flavor1.setList("正常,少糖,多糖");
        DishFlavor flavor2 = new DishFlavor();
        flavor2.setTag("辣度");
        flavor2.setList("不辣,微辣,中辣,重辣");

        dish.setFlavors(Arrays.asList(flavor1,flavor2));
        dishService.addDish(dish);
    }
    @Test
    public void testGetDish(){
        List<DishVO> result = dishService.getDishByName("更新");
        log.info("共查询到 {} 个菜品", result.size());
        for(DishVO dish : result){
            log.info("ID: {}, 菜名: {}, 价格: {}, 分类ID: {}, 上架状态: {},",
                    dish.getId(),
                    dish.getDishName(),
                    dish.getPrice(),
                    dish.getCategoryId(),
                    dish.getIsAvailable());

        }
//        List<DishVO> result1 = dishService.getAllDish();
//        log.info("共查询到 {} 个菜品", result.size());
//        for(DishVO dish : result1){
//            log.info("ID: {}, 菜名: {}, 价格: {}, 分类ID: {}, 上架状态: {}",
//                    dish.getId(),
//                    dish.getDishName(),
//                    dish.getPrice(),
//                    dish.getCategoryId(),
//                    dish.getIsAvailable());

//        }
    }
    @Test
    public void testUpdateDish(){
        DishDTO up = getDishDTO();

        // Act
        dishService.updateDish(up);
        Dish updated = dishService.getById(originalDish.getId());
        assertNotNull(updated);
        assertEquals("新版宫保鸡丁", updated.getDishName());
        assertEquals(new BigDecimal("38.00"), updated.getPrice());
        assertEquals(2, updated.getCategoryId());
        assertEquals("升级版，更香", updated.getDescription());

        List<DishFlavor> flavors = dishFlavorService.list(
                new QueryWrapper<DishFlavor>().eq("dishId",originalDish.getId()));
        assertEquals(2,flavors.size());
    }

    private DishDTO getDishDTO() {
        DishDTO up = new DishDTO();
        up.setId(originalDish.getId());
        up.setDishName("新版宫保鸡丁");
        up.setPrice(new BigDecimal("38.00"));
        up.setCategoryId(2);
        up.setDescription("升级版，更香");

        // 新口味
        DishFlavor newFlavor1 = new DishFlavor();
        newFlavor1.setTag("辣度");
        newFlavor1.setList("中辣,重辣");

        DishFlavor newFlavor2 = new DishFlavor();
        newFlavor2.setTag("温度");
        newFlavor2.setList("热,去冰");

        up.setFlavors(Arrays.asList(newFlavor1, newFlavor2));
        return up;
    }
}
