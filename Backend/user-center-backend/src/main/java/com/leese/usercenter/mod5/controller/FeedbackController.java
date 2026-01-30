package com.leese.usercenter.mod5.controller;

import com.leese.usercenter.mod5.model.dto.FeedbackRequest;
import com.leese.usercenter.mod5.model.dto.FeedbackResponse;
import com.leese.usercenter.mod5.service.FeedbackService;
import com.leese.usercenter.utils.AuthUtil;
import com.leese.usercenter.model.entity.User;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true") // 添加跨域
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // 增 - 需要登录
    @PostMapping("/create")
    public FeedbackResponse createFeedback(@RequestBody FeedbackRequest request,
                                           HttpServletRequest httpRequest) {
        User currentUser = AuthUtil.checkUserLogin(httpRequest);
        // 从session获取用户ID，而不是从请求体
        request.setUserId(currentUser.getId().longValue());
        return feedbackService.createFeedback(request);
    }

    // 删 - 需要管理员权限
    @DeleteMapping("/delete/{id}")
    public boolean deleteFeedback(@PathVariable Long id, HttpServletRequest request) {
        AuthUtil.checkAdmin(request); // 只有管理员能删除
        return feedbackService.deleteFeedback(id);
    }

    // 改 - 需要管理员权限
    @PutMapping("/update/{id}")
    public FeedbackResponse updateFeedback(
            @PathVariable Long id,
            @RequestBody FeedbackRequest request,
            HttpServletRequest httpRequest) {
        AuthUtil.checkAdmin(httpRequest); // 只有管理员能修改
        return feedbackService.updateFeedback(id, request);
    }



    // 查 - 所有（需要管理员权限）
    @GetMapping("/list")
    public List<FeedbackResponse> getAllFeedbacks(HttpServletRequest request) {
        AuthUtil.checkAdmin(request); // 只有管理员能查看所有
        return feedbackService.getAllFeedbacks();
    }

    // 查 - 单个（管理员或自己的反馈）
    @GetMapping("/{id}")
    public FeedbackResponse getFeedbackById(@PathVariable Long id,
                                            HttpServletRequest request) {
        FeedbackResponse feedback = feedbackService.getFeedbackById(id);
        if (feedback == null) {
            return null;
        }

        // 检查权限：管理员或反馈所有者
        User currentUser = AuthUtil.checkUserLogin(request);
        boolean isAdmin = currentUser.getUserRole() == 1; // ADMIN_ROLE
        boolean isOwner = feedback.getUserId().equals(currentUser.getId().longValue());

        if (!isAdmin && !isOwner) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权查看此反馈");
        }

        return feedback;
    }

    // 查 - 按用户（需要管理员权限或查看自己的）
    @GetMapping("/user/{userId}")
    public List<FeedbackResponse> getFeedbacksByUserId(@PathVariable Long userId,
                                                       HttpServletRequest request) {
        User currentUser = AuthUtil.checkUserLogin(request);

        // 检查权限：只能查看自己的反馈，除非是管理员
        if (!currentUser.getId().equals(userId.intValue()) && currentUser.getUserRole() != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只能查看自己的反馈");
        }

        return feedbackService.getFeedbacksByUserId(userId);
    }

    // 新加：获取当前用户的反馈
    @GetMapping("/my")
    public List<FeedbackResponse> getMyFeedbacks(HttpServletRequest request) {
        User currentUser = AuthUtil.checkUserLogin(request);
        return feedbackService.getFeedbacksByUserId(currentUser.getId().longValue());
    }
}