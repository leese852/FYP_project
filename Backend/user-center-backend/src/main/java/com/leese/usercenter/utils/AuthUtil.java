package com.leese.usercenter.utils;

import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.leese.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.leese.usercenter.constant.UserConstant.USER_LOGIN_STATE;

public class AuthUtil {
    public static User checkUserLogin(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Object userObj = session.getAttribute(USER_LOGIN_STATE);
        if (!(userObj instanceof User)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return (User) userObj;
    }

    public static User checkAdmin(HttpServletRequest request){
        User user = checkUserLogin(request);
        if(user.getUserRole() != ADMIN_ROLE){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return user;
    }
}
