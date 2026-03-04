package com.leese.usercenter.controller.common;

import com.leese.usercenter.common.BaseResponse;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.common.ResultUtils;
import com.leese.usercenter.utils.FileUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 通用接口 - 文件上传（可被 User、Dish 等所有模块的前端调用）
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    /**
     * 文件上传
     * 前端上传图片 → 后端保存到 uploads/ → 返回完整访问 URL
     *
     * @param file    上传的图片文件
     * @param request HTTP 请求对象（用于动态拼接 URL）
     * @return 图片的完整访问 URL，如 http://localhost:8080/common/download/uuid.jpg
     */
    @PostMapping("/upload")
    public BaseResponse<String> upload(MultipartFile file, HttpServletRequest request) {
        // 1. 基本校验
        if (file == null || file.isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "文件不能为空", "文件不能为空");
        }

        try {
            // 2. 委托 FileUtil 保存文件，拿到新文件名（如 uuid-xxxx.jpg）
            String fileName = FileUtil.saveFile(file);

            // 3. 动态拼接完整 URL（不写死 localhost，部署到服务器时也能正常使用）
            String scheme = request.getScheme();         // http 或 https
            String serverName = request.getServerName(); // localhost 或服务器 IP
            int serverPort = request.getServerPort();    // 8080
            String contextPath = request.getContextPath(); // 一般为空字符串

            // 格式：http://localhost:8080/common/download/uuid-xxxx.jpg
            // WebMvcConfig 中配置了 /common/download/** 映射到本地 uploads 目录
            String fileUrl = scheme + "://" + serverName + ":" + serverPort + contextPath
                    + "/common/download/" + fileName;

            return ResultUtils.success(fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "文件上传失败", e.getMessage());
        }
    }
}
