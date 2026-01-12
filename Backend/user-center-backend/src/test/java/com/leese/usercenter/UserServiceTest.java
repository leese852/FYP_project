package com.leese.usercenter;

import com.leese.usercenter.model.entity.User;
import com.leese.usercenter.service.DishService;
import com.leese.usercenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.leese.usercenter.constant.UserConstant.USER_LOGIN_STATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

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

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Test
    void getCurrentUser(){
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("testUser");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(USER_LOGIN_STATE)).thenReturn(mockUser);

        User result = userService.getCurUser(request);
        // Assert（断言）
        System.out.println( result.getUsername());
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("testUser", result.getUsername());

    }

}
