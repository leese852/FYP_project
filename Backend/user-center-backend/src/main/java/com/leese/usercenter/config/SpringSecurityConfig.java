package com.leese.usercenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.security.Security;

@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 所有请求都允许访问，无需登录
                )
                .csrf(csrf -> csrf.disable())


                .httpBasic(httpBasic -> httpBasic.disable())


                .formLogin(formLogin -> formLogin.disable());

        return http.build();
    }
}
