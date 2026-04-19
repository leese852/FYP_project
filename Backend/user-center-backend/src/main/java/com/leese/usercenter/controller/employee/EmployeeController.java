package com.leese.usercenter.controller.employee;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leese.usercenter.common.BaseResponse;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.common.ResultUtils;
import com.leese.usercenter.exception.BusinessException;
import com.leese.usercenter.mapper.EmployeeMapper;
import com.leese.usercenter.model.dto.EmployeeLoginDTO;
import com.leese.usercenter.model.entity.Employee;
import com.leese.usercenter.constant.EmployeeConstant;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeMapper employeeMapper;

    @PostMapping("/login")
    public BaseResponse<Employee> employeeLogin(@RequestBody EmployeeLoginDTO loginDTO, HttpServletRequest request) {
        if (loginDTO == null || StringUtils.isAnyBlank(loginDTO.getAccount(), loginDTO.getPassword())) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账号或密码为空");
        }

        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", loginDTO.getAccount());
        queryWrapper.eq("password", loginDTO.getPassword()); // 直接明码比较

        Employee employee = employeeMapper.selectOne(queryWrapper);
        if (employee == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账号或密码错误");
        }

        if (employee.getStatus() != null && employee.getStatus() == 1) { // 假设 1 为禁用
            throw new BusinessException(ErrorCode.NO_AUTH, "账号已被禁用");
        }

        // 回退点：不再伪装成 User，直接将脱敏的 Employee 信息存入独立的 Session Key 中
        employee.setPassword(null); // 脱敏处理
        request.getSession().setAttribute(EmployeeConstant.EMPLOYEE_LOGIN_STATE, employee);

        return ResultUtils.success(employee);
    }

    @GetMapping("/current")
    public BaseResponse<Employee> getCurrentEmployee(HttpServletRequest request) {
        Object employeeObj = request.getSession().getAttribute(EmployeeConstant.EMPLOYEE_LOGIN_STATE);
        Employee employee = (Employee) employeeObj;
        if (employee == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return ResultUtils.success(employee);
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> employeeLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(EmployeeConstant.EMPLOYEE_LOGIN_STATE);
        return ResultUtils.success(true);
    }
}
