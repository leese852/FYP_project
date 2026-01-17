package com.leese.usercenter.service.impl;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.common.ResultUtils;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.model.dto.UserFixPwdDTO;
import com.leese.usercenter.model.dto.UserUpdateDTO;
import com.leese.usercenter.model.entity.User;
import com.leese.usercenter.service.UserService;
import com.leese.usercenter.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.leese.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* 用戶服務實現類
*/
@Slf4j
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
        user.setUsername(userAccount);
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

        //記錄用戶的登錄態 !!!
        User safetyUser = getSafetyUser(user);
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE, safetyUser);

        // 4. 添加调试日志
        log.info("=== 用户登录成功 ===");
        log.info("Session ID: {}", session.getId());
        log.info("用户ID: {}", safetyUser.getId());
        log.info("用户账号: {}", safetyUser.getUserAccount());
        log.info("存储的属性名: USER_LOGIN_STATE");
        log.info("属性值类型: {}", safetyUser.getClass().getName());
        log.info("=== 登录结束 ===");
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
        safetyUser.setCreateTime(originUser.getCreateTime());
//        safetyUser.setUpdateTime(new Date());
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
    public void userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }

    @Override
    public User updateUser(UserUpdateDTO userUpdateDTO, HttpServletRequest request) {
        if(request == null && userUpdateDTO == null){
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        int userId = getCurUser(request).getId();
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",userId);
        if(StringUtils.isNotBlank(userUpdateDTO.getUsername())){
            updateWrapper.set("username",userUpdateDTO.getUsername());
        }
        Byte gender = userUpdateDTO.getGender();
        if (gender != null && (gender == 0 || gender == 1)) { // 根据你的业务范围调整
            updateWrapper.set("gender", gender);
        }
        if (StringUtils.isNotBlank(userUpdateDTO.getEmail())) {
            updateWrapper.set("email", userUpdateDTO.getEmail());
        }
        if (StringUtils.isNotBlank(userUpdateDTO.getTel())) {
            updateWrapper.set("tel", userUpdateDTO.getTel());
        }
        if (StringUtils.isNotBlank(userUpdateDTO.getAvatar())) {
            updateWrapper.set("avatar_url", userUpdateDTO.getAvatar());
        }
        //设置更新时间
        updateWrapper.set("update_Time",new Date());

        boolean updated = this.update(updateWrapper);
        if (!updated) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        User safetyUser = getSafetyUser(this.getById(userId));
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public User getCurUser(HttpServletRequest request){
        Object user = request.getSession(false).getAttribute(USER_LOGIN_STATE);
        return (User) user;
    }

    @Override
    public Boolean changePassword(UserFixPwdDTO dto,HttpServletRequest request){
        if(StringUtils.isAnyBlank(dto.getNewPwd(),dto.getOldPwd())){
            throw new BusinessException(ErrorCode.PARAM_ERROR,"参数不能为空");
        };
        int userId = getCurUser(request).getId();
        //因为session存入的是脱敏后的数据，所以需要重新获取
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        User user = this.getOne(queryWrapper);

        boolean isMatch = encryptPassword.matches(dto.getOldPwd(),user.getUserPassword());
        if(!isMatch){
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR,"密码错误");
        }

        String newEncryPwd = encryptPassword.encode(dto.getNewPwd());
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setUserPassword(newEncryPwd);
        updateUser.setUpdateTime(new Date());
        boolean isSuccess = this.updateById(updateUser);
        if (!isSuccess) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "密码更新失败");
        }
        return true;
    }
}
