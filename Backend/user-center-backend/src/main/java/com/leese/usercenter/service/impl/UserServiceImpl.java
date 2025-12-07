package com.leese.usercenter.service.impl;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.model.User;
import com.leese.usercenter.service.UserService;
import com.leese.usercenter.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.leese.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* 用戶服務實現類
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private UserMapper userMapper;


    @Autowired
    //@Bean  是由 Spring IoC（控制反转）容器管理的对象，
    // 通过依赖注入的方式将这些对象提供给需要它们的其他组件。
    private BCryptPasswordEncoder encryptPassword;


    @Override 
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校驗
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            // todo 修改為自定義異常
            throw new BusinessException(ErrorCode.PARAM_ERROR,"輸入參數爲空");
        }
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAM_ERROR,"賬號長度過短");
        }
        if(userPassword.length() < 6 || checkPassword.length() < 6){
            throw new BusinessException(ErrorCode.PARAM_ERROR,"密碼長度過短");
        }

        //校驗特殊字符
        final String USERNAME_REGEX = "^[a-zA-Z0-9]{3,20}$";
        if(!userAccount.matches(USERNAME_REGEX)){
            throw new BusinessException(ErrorCode.PARAM_ERROR,"賬號格式錯誤");
        }

        //賬戶重複
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = this.count(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAM_ERROR,"賬號重複");
        }
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAM_ERROR,"兩次密碼不一致");
        }

        //2.加密
        String encodePassword = encryptPassword.encode(userPassword);

        //3 插入數據
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encodePassword);
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"");
        }
        return user.getId();
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //檢查輸入是否為空
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        if(userAccount.length() < 4){
            return null;
        }
        if(userPassword.length() < 6){
            return null;
        }
        //用戶名校驗
        final String USERNAME_REGEX = "^[a-zA-Z0-9]{3,20}$";
        if(!userAccount.matches(USERNAME_REGEX)){
            return null;
        }
        //查詢用戶是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
//        User user = this.getOne(queryWrapper);
        User user = userMapper.selectOne(queryWrapper);
        //用戶不存在
        String curAccount = user.getUserAccount();
        if(curAccount == null){
            log.debug("User login failed");
            return null;
        }
        String curPassword = user.getUserPassword();
        //用戶存在，判斷密碼是否一致
        boolean isMatch = encryptPassword.matches(userPassword,curPassword);
        if(!isMatch){
            return null;
        }

        //記錄用戶的登錄態
        User safetyUser = getSafetyUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User originUser){
        //用户脱敏
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setTel(originUser.getTel());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreaetTime(originUser.getCreaetTime());
        safetyUser.setUpdateTime(new Date());
        safetyUser.setUserRole(originUser.getUserRole());
        return safetyUser;
    }

    @Override
    public List<User> searchUsers(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank( username)){
             queryWrapper.like("username", username);
        }
        List<User> userList = this.list(queryWrapper);
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());

    }
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}
