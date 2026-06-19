package com.seekisle.portalservice.user.controller;

import com.seekisle.adminapi.appuser.domain.dto.UserEditReqDTO;
import com.seekisle.commonsecurity.domain.dto.TokenDTO;
import com.seekisle.commonsecurity.utils.JwtUtil;
import com.seekisle.portalservice.PortalServiceApplication;
import com.seekisle.portalservice.user.entity.dto.CodeLoginDTO;
import com.seekisle.portalservice.user.entity.dto.WechatLoginDTO;
import com.seekisle.portalservice.user.service.IUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = PortalServiceApplication.class)
class UserControllerTest {

    @Autowired
    private IUserService userService;

    @Test
    void sendCode() {
        Assertions.assertTrue( userService.sendCode("12532385964") != null);
    }

    @Test
    void login() {
        CodeLoginDTO codeLoginDTO = new CodeLoginDTO();
        String code = userService.sendCode("12532385964");
        codeLoginDTO.setPhone("12532385964");
        codeLoginDTO.setCode(code);
        Assertions.assertTrue( userService.login(codeLoginDTO) != null);
        WechatLoginDTO wechatLoginDTO = new WechatLoginDTO();
        wechatLoginDTO.setOpenId("o7pPd4isCAMne7v30Sai0KQudXyE");
        Assertions.assertTrue(userService.login(wechatLoginDTO) != null);
    }

    @Test
    void edit() {
        CodeLoginDTO codeLoginDTO = new CodeLoginDTO();
        String code = userService.sendCode("12532385964");
        codeLoginDTO.setPhone("12532385964");
        codeLoginDTO.setCode(code);
        TokenDTO tokenDTO = userService.login(codeLoginDTO);
        UserEditReqDTO editReqDTO = new UserEditReqDTO();
        editReqDTO.setUserId(Long.parseLong(JwtUtil.getUserId(tokenDTO.getAccessToken())));
        editReqDTO.setNickName("bite_hahaha");
        userService.edit(editReqDTO);
        Assertions.assertTrue(true, "The condition should be true after the method execution");
    }
}