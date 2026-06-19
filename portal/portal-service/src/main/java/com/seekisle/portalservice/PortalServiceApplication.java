package com.seekisle.portalservice;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 
 */
@Slf4j
@MapperScan("com.seekisle.**.mapper")
@EnableFeignClients(basePackages = {"com.seekisle.**.feign"})
@SpringBootApplication
public class PortalServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortalServiceApplication.class, args);
        log.info("门户服务启动成功");
    }
}