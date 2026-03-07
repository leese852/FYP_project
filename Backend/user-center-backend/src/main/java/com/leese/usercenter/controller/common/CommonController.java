package com.leese.usercenter.controller.common;

import com.leese.usercenter.common.BaseResponse;
import com.leese.usercenter.common.ErrorCode;
import com.leese.usercenter.common.ResultUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    /**
     * 文件上传
     *
     * @param file    上传的文件
     * @param request HTTP 请求对象
     * @return 文件的访问 URL
     */
    @PostMapping("/upload")
    public BaseResponse<String> upload(MultipartFile file, HttpServletRequest request) {
        if (file == null || file.isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "文件不能为空", "文件不能为空");
        }

        // 1. 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR, "文件名异常", "文件名异常");
        }

        // 2. 截取后缀名 (例如 .jpg, .png)
        String suffix = "";
        if (originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 3. 生成新文件名 (使用 UUID 防止重名)
        String fileName = UUID.randomUUID().toString() + suffix;

        // 4. 获取文件保存目录 (项目根目录/uploads/)
        String uploadPath = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

        File dir = new File(uploadPath);
        // 如果目录不存在，创建目录
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            // 5. 将文件保存到指定位置
            file.transferTo(new File(uploadPath + fileName));

            // 6. 返回文件的访问 URL
            // 拼接格式：http://localhost:8080/common/download/文件名
            // 根据 request 动态拼接 URL
            String scheme = request.getScheme(); // http
            String serverName = request.getServerName(); // localhost
            int serverPort = request.getServerPort(); // 8080
            String contextPath = request.getContextPath(); // 如果有配置 context-path

            // 构造完整的 URL
            // 注意：WebMvcConfig 中配置了 /common/download/** 映射到 uploads 目录
            String fileUrl = scheme + "://" + serverName + ":" + serverPort + contextPath + "/common/download/" + fileName;

            return ResultUtils.success(fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "文件上传失败", e.getMessage());
        }
    }
}
