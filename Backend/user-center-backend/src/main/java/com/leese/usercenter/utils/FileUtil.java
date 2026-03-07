package com.leese.usercenter.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件工具类 - 负责将图片保存到本地磁盘
 * 可被任意 Service 或 Controller 调用（User、Dish 等模块通用）
 */
public class FileUtil {

    // 文件存储的根目录（项目根目录下的 uploads 文件夹）
    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    /**
     * 保存文件到磁盘
     *
     * @param file 上传的文件（MultipartFile）
     * @return 保存后的新文件名（如 uuid-xxxx.jpg），不包含完整 URL
     * @throws IOException 保存失败时抛出，让调用方决定如何处理（例如事务回滚）
     */
    public static String saveFile(MultipartFile file) throws IOException {
         // 1. 获取原始文件名，截取后缀（如 .jpg）并暂存
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 2. 用 UUID 生成不重复的新文件名（防止同名文件互相覆盖）
        String newFileName = UUID.randomUUID().toString() + suffix;

        // 3. 确保目录存在（不存在就自动创建）
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 4. 保存文件到磁盘
        file.transferTo(new File(UPLOAD_DIR + newFileName));

        // 5. 只返回文件名，不返回完整 URL（URL 拼接交给调用方）
        return newFileName;
    }
}
