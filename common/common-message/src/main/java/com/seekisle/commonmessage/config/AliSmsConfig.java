package com.seekisle.commonmessage.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云短信配置参数
 * @author bite
 */
@Configuration
@RefreshScope
public class AliSmsConfig {

    /**
     * ak
     */
    @Value("${sms.aliyun.accessKeyId:}")
    private String accessKeyId;

    /**
     * sk
     */
    @Value("${sms.aliyun.accessKeySecret:}")
    private String accessKeySecret;

    /**
     * 服务器地址
     */
    @Value("${sms.aliyun.endpoint:}")
    private String endpoint;

    /**
     * 客户端注册
     * @return 短信客户端
     * @throws Exception 异常
     */
    @Bean("aliClient")
    public Client client() throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint(endpoint);
        return new Client(config);
    }
}
