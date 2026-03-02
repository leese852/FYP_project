package com.leese.usercenter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web 配置类
 * 负责静态资源映射
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取当前项目根目录下的 uploads 文件夹路径
        // System.getProperty("user.dir") 获取的是项目启动时的根目录
        String uploadPath = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

        // 将 /common/download/** 路径映射到本地的 uploads 文件夹
        // file: 前缀表示这是一个本地文件系统路径
        registry.addResourceHandler("/common/download/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
