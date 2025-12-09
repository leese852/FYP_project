package com.leese.usercenter.controller;

import com.leese.usercenter.common.BaseResponse;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.common.ResultUtils;
import com.leese.usercenter.constant.UserConstant;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.model.User;
import com.leese.usercenter.model.dto.userLoginRequest;
import com.leese.usercenter.model.dto.userRegisterRequest;
import com.leese.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author leese
 * @since 2022-05-09
 */
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@RestController
// @RestController 当 Spring MVC 发现方法上有 @ResponseBody（或类上有 @RestController），
// 它会启动一个叫做 HttpMessageConverter（HTTP 消息转换器）的机制
@RequestMapping("/user")
public class UserController implements UserConstant{
    @Resource
    private UserService userService;

    //PostMapping 服务器输入映射
    //@RequestBody 映射json数据到java对象
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody userRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null){
//            return ResultUtils.error(ErrorCode.PARAM_ERROR);
            throw new BusinessException(ErrorCode.PARAM_ERROR,"参数为空");
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }

        long result = userService.userRegister(userAccount, userPassword, checkPassword);
//        return new BaseResponse<>(0,result,"ok");
        return ResultUtils.success(result);
    }

    //HttpServletRequest 用于封装客户端发送给服务器的 HTTP 请求信息。
    // 每当客户端通过 HTTP 协议向服务器发起请求（如访问网页、提交表单等），
    // Servlet 容器就会创建一个 HttpServletRequest 对象来表示这个请求，
    // 并将其传递给相应的 Servlet 或控制器方法进行处理。
    @PostMapping("/login")
    public BaseResponse<User> doLogin (@RequestBody userLoginRequest userLoginRequest,HttpServletRequest request){
        if(userLoginRequest == null){
            return ResultUtils.error(ErrorCode.PARAM_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return ResultUtils.error(ErrorCode.PARAM_ERROR);
        }
        User user = userService.doLogin(userAccount,userPassword,request);

        return ResultUtils.success(user);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        List<User> list = userService.searchUsers(username);
        return ResultUtils.success(list);
    }

    @GetMapping("/current")
    public BaseResponse<User>  getCurrentUser(HttpServletRequest request){
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObject;
        if(currentUser == null){
            return null;
        }
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id,HttpServletRequest request){
        // TODO 完成false工具类分装
        if(!isAdmin(request)){
            return null;
        }
        if(id <= 0){
            return null;
        }
        //调用mybatis 的逻辑删除，将删除转变为更新
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if(request == null){
            return null;
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    private boolean isAdmin(HttpServletRequest request){
        //仅管理
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
