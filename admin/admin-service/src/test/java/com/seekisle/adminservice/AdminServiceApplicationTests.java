package com.seekisle.adminservice;

import com.seekisle.adminapi.config.feign.DictionaryFeignClient;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class AdminServiceApplicationTests {

    @Resource
    private DictionaryFeignClient dictionaryFeignClient;
    @Test
    void contextLoads() {
        System.out.println(dictionaryFeignClient.selectDictDataByType("house_type"));
        System.out.println(dictionaryFeignClient.selectDictDataByTypes(new ArrayList<>(){{
            add("house_type");
            add("rent_type");
        }}));

        System.out.println(dictionaryFeignClient.getDicDataByKey("one"));
        System.out.println(dictionaryFeignClient.getDicDataByKey("two room"));
        System.out.println(dictionaryFeignClient.getDicDataByKeys(new ArrayList<String>(){{
            add("two room");
        }}));
    }

}
