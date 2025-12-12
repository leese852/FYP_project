package com.leese.usercenter;

import com.leese.usercenter.model.Dish;
import com.leese.usercenter.model.User;
import com.leese.usercenter.model.dto.DishDTO;
import com.leese.usercenter.service.DishService;
import com.leese.usercenter.service.UserService;
import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private DishService dishService;
    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("testLeese");
        user.setUserPassword("test123");
        user.setUserAccount("testleese");
        user.setAvatarUrl("https://baomidou.com/assets/asset.cIbiVTt_.svg");
        user.setGender(18);
        user.setEmail("leese@gmail.com");
        user.setTel("53000849");
        user.setPlantCode("1145");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void userRegister() {
        String userAccount = "leese145";
        String userPassword = "123456";
        String checkPassword = "123456";
        long result = userService.userRegister(userAccount,userPassword,checkPassword);
        System.out.println(result);
    }

    @Test
    void testDish(){
        DishDTO dish = new DishDTO();
        dish.setDishName("testDish");
        dish.setPrice(new BigDecimal("50.00"));
        dish.setCategoryId(1);



    }
    @Test
    public void testUpdateDishDoesNotLoseCreateTime() throws InterruptedException {
        // 1. 先创建一个测试菜品
        Dish testDish = new Dish();
        testDish.setDishName("测试菜品");
        testDish.setPrice(new BigDecimal("50.00"));
        testDish.setCategoryId(1);
        testDish.setIsDelete(0);
        testDish.setIsAvailable(1);
        testDish.setCreateTime(new Date());

        dishService.save(testDish);
        Integer dishId = testDish.getId();  // 获取自增ID

        System.out.println("创建的菜品ID: " + dishId);

        // 2. 查询确认
        Dish original = dishService.getById(dishId);  // ✅ 使用真实ID
        assertNotNull("菜品应该存在", original);
        Date originalCreateTime = original.getCreateTime();
        System.out.println("原始创建时间: " + originalCreateTime);

        Thread.sleep(3000);

        // 3. 执行更新
        DishDTO dto = new DishDTO();
        dto.setId(dishId);  // 使用真实ID
        dto.setDishName("更新测试");
        dto.setPrice(new BigDecimal("99.99"));

        dishService.updateDish(dto);

        // 4. 验证
        Dish updated = dishService.getById(dishId);
        System.out.println("更新后创建时间: " + updated.getUpdateTime());

        assertNotNull("创建时间不应该为null", updated.getUpdateTime());
        assertEquals("创建时间应该保持不变",
                originalCreateTime,
                updated.getCreateTime());
    }


}
