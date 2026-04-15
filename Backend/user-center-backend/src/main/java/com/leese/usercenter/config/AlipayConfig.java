package com.leese.usercenter.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayConfig {

    // 从 application.yml 中自动读取
    private String appId;
    private String privateKey;
    private String alipayPublicKey;
    private String serverUrl;
    private String charset;
    private String signType;
    private String format;
    private String notifyUrl;
    private String returnUrl;

    /**
     * 实例化并注册 AlipayClient。
     * 只有写了这个 @Bean，在 Controller 里才能 @Autowired private AlipayClient alipayClient;
     */
    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(
                serverUrl,
                appId,
                privateKey,
                format,
                charset,
                alipayPublicKey,
                signType
        );
    }
}