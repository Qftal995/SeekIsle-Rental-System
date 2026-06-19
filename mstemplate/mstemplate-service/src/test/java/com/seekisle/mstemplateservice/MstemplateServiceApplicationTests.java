package com.seekisle.mstemplateservice;

import com.seekisle.commoncore.domain.entity.BaseDO;
import com.seekisle.commonredis.service.RedisService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MstemplateServiceApplicationTests {

    @Autowired
    RedisService redisService;

    @Test
    void contextLoads() {
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class SysRegion extends BaseDO {

        /**
         * 区划名称
         */
        private String name;

        /**
         * 区划全称
         */
        private String fullName;

        /**
         * 区域编码
         */
        private String code;

        /**
         * 区域父id
         */
        private Long parentId;

        /**
         * 区域父编码
         */
        private String parentCode;

        /**
         * 拼音
         */
        private String pinyin;

        /**
         * 级别
         */
        private Integer level;

        /**
         * 经度
         */
        private Double longitude;

        /**
         * 纬度
         */
        private Double latitude;


    }

    //公共类测试
    @Test
    void redisTest(){
        SysRegion sysRegion = new SysRegion();
        sysRegion.setFullName("北京市");
        sysRegion.setName("北京");
        sysRegion.setCode("110000");
        sysRegion.setId(1L);

        redisService.setCacheObject("test:redis",sysRegion);
    }
}
