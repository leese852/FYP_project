package com.leese.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication //@ComponentScan 默认扫描 com.leese.usercenter 及其子包（包括 controller, service 等）
@MapperScan("com.leese.usercenter.mapper")
//自动扫描指定包下的 Mapper 接口，并将它们注册为 Spring 容器中的 Bean。
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
