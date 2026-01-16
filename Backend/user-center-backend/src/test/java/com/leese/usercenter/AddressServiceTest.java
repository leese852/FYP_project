package com.leese.usercenter.service.impl;

import com.leese.usercenter.model.dto.AddressDTO;
import com.leese.usercenter.model.entity.User;
import com.leese.usercenter.service.UserService;
import com.leese.usercenter.utils.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddressServiceTest {

    @Autowired
    private AddressServiceImpl addressService;  // 直接注入，让 Spring 管理

    @Autowired
    private UserService userService;

    private MockHttpServletRequest request;
    private User testUser;

    @BeforeAll
    void setUp() {
        request = new MockHttpServletRequest();

        // 创建测试用户
        testUser = new User();
        testUser.setUsername("test_" + System.currentTimeMillis());
        testUser.setUserPassword("123456");
        testUser.setTel("13800138000");
        userService.save(testUser);


    }

    @Test
    void testAddAddress() {

        // 1. 准备数据
        AddressDTO dto = new AddressDTO();
        dto.setContactName("张二");
        dto.setContactPhone("53000849");
        dto.setAddress("测试地址1");
        dto.setIsDefault(1);

        // 添加第二个地址（非默认）
        AddressDTO address2 = new AddressDTO();
        address2.setContactName("李四");
        address2.setContactPhone("53000850");
        address2.setAddress("地址2-非默认");
        address2.setIsDefault(0);


        // 2. Mock静态方法（用户登录验证）
        try (MockedStatic<AuthUtil> authUtilMock = org.mockito.Mockito.mockStatic(AuthUtil.class)) {
            // 设置用户到 request 中（根据你的 AuthUtil 实现调整）
            request.setAttribute("userId", testUser.getId());

            // Mock AuthUtil.checkUserLogin
            authUtilMock.when(() -> AuthUtil.checkUserLogin(request))
                    .thenReturn(testUser);

            // 3. 执行测试
            assertDoesNotThrow(() -> {
                addressService.addAddress(dto, request);
                addressService.addAddress(address2, request);
            });

            System.out.println("✅ 添加地址成功");
        }
    }
    @Test
    void updateAddress(){
        // 1. 准备数据
        AddressDTO dto = new AddressDTO();
        dto.setId(3);
        dto.setUserId(17);
        dto.setContactName("张4");
        dto.setContactPhone("53000849");
        dto.setAddress("修改后地址");
        dto.setIsDefault(1);

        try (MockedStatic<AuthUtil> authUtilMock = org.mockito.Mockito.mockStatic(AuthUtil.class)){
            request.setAttribute("userId", testUser.getId());

            // Mock AuthUtil.checkUserLogin
            authUtilMock.when(() -> AuthUtil.checkUserLogin(request))
                    .thenReturn(testUser);
        }

        // 3. 执行测试
        assertDoesNotThrow(() -> {
            addressService.updateAddress(dto, request);
        });
    }
}