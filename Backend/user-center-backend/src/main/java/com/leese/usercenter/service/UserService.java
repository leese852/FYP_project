package com.leese.usercenter.service;

import com.leese.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author wuhao
* @description 针对表【user】的数据库操作Service
* @createDate 2025-07-29 14:57:46
 *
 * 用戶服務
*/

//他这里是怎么调用到UserServiceImpl的实例.它触及了Spring框架的核心机制——依赖注入
//UserService 是接口，定义了方法契约。
//UserServiceImpl 是实现类，提供了具体逻辑。
//Spring 容器在启动时创建 UserServiceImpl 的实例，并将其作为 UserService 的实现注册到容器中。
//当其他组件使用 @Autowired 注入 UserService 时，Spring 会自动将 UserServiceImpl 的实例注入。
//最终，调用的是 UserServiceImpl 中的方法。
public interface UserService extends IService<User> {


    /**
     *
     * @param userAccount 用戶賬戶
     * @param userPassword 用戶密碼
     * @param checkPassword 校驗密碼
     * @return 新用戶 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     *
     * @param userAccount
     * @param userPassword
     * @return 脫敏後的用戶信息
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User originUser);

    /**
     *
     * @param username
     * @return 用户信息
     */
    List<User> searchUsers(String username);

    /**
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
