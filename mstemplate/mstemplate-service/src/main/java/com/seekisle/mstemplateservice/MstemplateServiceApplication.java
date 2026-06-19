package com.seekisle.mstemplateservice;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 */
@Slf4j
@MapperScan("com.seekisle.**.mapper")
@SpringBootApplication
public class MstemplateServiceApplication {

    /**
     * 主函数
     * @param args 入参
     */
    public static void main(String[] args) {
        SpringApplication.run(MstemplateServiceApplication.class, args);
    }
}
