package com.leese.usercenter;

import com.leese.usercenter.model.User;
import com.leese.usercenter.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

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


}
