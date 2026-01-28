// Backend/usercenter/src/main/java/com/leese/usercenter/mod5/controller/FeedbackController.java
package com.leese.usercenter.mod5.controller;

import com.leese.usercenter.mod5.model.dto.FeedbackRequest;
import com.leese.usercenter.mod5.model.dto.FeedbackResponse;
import com.leese.usercenter.mod5.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // 增
    @PostMapping("/create")
    public FeedbackResponse createFeedback(@RequestBody FeedbackRequest request) {
        return feedbackService.createFeedback(request);
    }

    // 删
    @DeleteMapping("/delete/{id}")
    public boolean deleteFeedback(@PathVariable Long id) {
        return feedbackService.deleteFeedback(id);
    }

    // 改
    @PutMapping("/update/{id}")
    public FeedbackResponse updateFeedback(
            @PathVariable Long id,
            @RequestBody FeedbackRequest request) {
        return feedbackService.updateFeedback(id, request);
    }

    // 查 - 所有
    @GetMapping("/list")
    public List<FeedbackResponse> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    // 查 - 单个
    @GetMapping("/{id}")
    public FeedbackResponse getFeedbackById(@PathVariable Long id) {
        return feedbackService.getFeedbackById(id);
    }

    // 查 - 按用户
    @GetMapping("/user/{userId}")
    public List<FeedbackResponse> getFeedbacksByUserId(@PathVariable Long userId) {
        return feedbackService.getFeedbacksByUserId(userId);
    }
}