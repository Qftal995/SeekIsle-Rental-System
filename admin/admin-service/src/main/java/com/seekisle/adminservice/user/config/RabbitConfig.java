package com.seekisle.adminservice.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 声明mq配置信息
 */
@Configuration
@Slf4j
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "edit_user_exchange";

    /**
     * 交换机
     *
     * @return
     */
    @Bean
    public FanoutExchange editUserExchange() {
        return new FanoutExchange(EXCHANGE_NAME,true,false);
    }

}
