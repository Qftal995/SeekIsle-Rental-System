package com.bitejiuyeke.biteadminservice;

import com.bitejiuyeke.biteadminapi.config.feign.DictionaryFeignClient;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class BiteAdminServiceApplicationTests {

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
