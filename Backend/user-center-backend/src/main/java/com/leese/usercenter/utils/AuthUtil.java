package com.leese.usercenter.utils;

import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import static com.leese.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.leese.usercenter.constant.UserConstant.USER_LOGIN_STATE;

@Slf4j
public class    AuthUtil {
    public static User checkUserLogin(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.warn("⚠️ Session 不存在");
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Object userObj = session.getAttribute(USER_LOGIN_STATE);
        if (!(userObj instanceof User)) {
            log.warn("⚠️ Session 中沒有正確的 USER_LOGIN_STATE, value = {}", userObj);
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        User user = (User) userObj;
        log.info("✅ Session 中獲取到用戶: {}", user);
        return user;
    }

    public static User checkAdmin(HttpServletRequest request){
        User user = checkUserLogin(request);
        if(user.getUserRole() == null || user.getUserRole() != ADMIN_ROLE){
            log.warn("⚠️ 用戶 {} 嘗試訪問管理員接口，但角色不足", user.getId());
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return user;
    }
}
