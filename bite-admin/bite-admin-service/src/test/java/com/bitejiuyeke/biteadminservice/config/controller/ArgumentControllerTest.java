package com.bitejiuyeke.biteadminservice.config.controller;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentAddReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentEditReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentListReqDTO;
import com.bitejiuyeke.biteadminservice.BiteAdminServiceApplication;
import com.bitejiuyeke.biteadminservice.config.service.ISysArgumentService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BiteAdminServiceApplication.class)
class ArgumentControllerTest {

    @Resource
    private ISysArgumentService argumentService;

    @Test
    @Transactional
    void add() {
        ArgumentAddReqDTO argumentAddReqDTO = new ArgumentAddReqDTO();
        argumentAddReqDTO.setName("允许上传最多图片数");
        argumentAddReqDTO.setValue("9");
        argumentAddReqDTO.setConfigKey("picturesMax");
        Assertions.assertTrue(argumentService.add(argumentAddReqDTO) > 0L);
    }

    @Test
    @Transactional
    void edit() {
        ArgumentAddReqDTO argumentAddReqDTO = new ArgumentAddReqDTO();
        argumentAddReqDTO.setName("允许上传最多图片数");
        argumentAddReqDTO.setValue("9");
        argumentAddReqDTO.setConfigKey("picturesMax");
        argumentService.add(argumentAddReqDTO);
        ArgumentEditReqDTO argumentEditReqDTO = new ArgumentEditReqDTO();
        argumentEditReqDTO.setConfigKey("picturesMax");
        argumentEditReqDTO.setValue("10");
        Assertions.assertTrue(argumentService.edit(argumentEditReqDTO) > 0L);
    }

    @Test
    @Transactional
    void list() {
        ArgumentAddReqDTO argumentAddReqDTO = new ArgumentAddReqDTO();
        argumentAddReqDTO.setName("允许上传最多图片数");
        argumentAddReqDTO.setValue("9");
        argumentAddReqDTO.setConfigKey("picturesMax");
        argumentService.add(argumentAddReqDTO);
        ArgumentListReqDTO argumentListReqDTO = new ArgumentListReqDTO();
        argumentListReqDTO.setConfigKey("picturesMax");
        Assertions.assertTrue(!argumentService.list(argumentListReqDTO).getList().isEmpty());
        argumentListReqDTO.setConfigKey("picturesMaxPlus");
        Assertions.assertTrue(argumentService.list(argumentListReqDTO).getList().isEmpty());
    }

    @Test
    @Transactional
    void getByConfigKeys() {
        ArgumentAddReqDTO argumentAddReqDTO = new ArgumentAddReqDTO();
        argumentAddReqDTO.setName("允许上传最多图片数");
        argumentAddReqDTO.setValue("9");
        argumentAddReqDTO.setConfigKey("picturesMax");
        argumentService.add(argumentAddReqDTO);
        argumentAddReqDTO.setName("允许上传文件大小");
        argumentAddReqDTO.setValue("10");
        argumentAddReqDTO.setConfigKey("fileMax");
        argumentService.add(argumentAddReqDTO);
        Assertions.assertTrue(argumentService.getByConfigKeys(Arrays.asList("picturesMax", "fileMax")).size() == 2);
    }

    @Test
    @Transactional
    void getByConfigKey() {
        ArgumentAddReqDTO argumentAddReqDTO = new ArgumentAddReqDTO();
        argumentAddReqDTO.setName("允许上传最多图片数");
        argumentAddReqDTO.setValue("9");
        argumentAddReqDTO.setConfigKey("picturesMax");
        argumentService.add(argumentAddReqDTO);
        Assertions.assertTrue(argumentService.getByConfigKey("picturesMax").getValue().equals("9"));
    }
}