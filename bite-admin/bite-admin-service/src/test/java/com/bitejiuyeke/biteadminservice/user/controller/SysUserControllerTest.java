package com.bitejiuyeke.biteadminservice.user.controller;

import com.bitejiuyeke.biteadminservice.BiteAdminServiceApplication;
import com.bitejiuyeke.biteadminservice.user.domain.dto.PasswordLoginDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserListReqDTO;
import com.bitejiuyeke.biteadminservice.user.service.ISysUserService;
import com.bitejiuyeke.bitecommoncore.utils.AESUtil;
import com.bitejiuyeke.bitecommonsecurity.service.TokenService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = BiteAdminServiceApplication.class)
class SysUserControllerTest {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private TokenService tokenService;

    @Test
    @Transactional
    void addOrEditUser() {
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setNickName("bite_hahaha");
        sysUserDTO.setIdentity("platform_admin");
        sysUserDTO.setPhoneNumber("12345678901");
        sysUserDTO.setPassword("123456");
        sysUserDTO.setStatus("enable");
        Long userId = sysUserService.addOrEditUser(sysUserDTO);
        Assertions.assertTrue(userId > 0L);
        sysUserDTO.setUserId(userId);
        sysUserDTO.setNickName("bite_hahahaPlus");
        Assertions.assertTrue( sysUserService.addOrEditUser(sysUserDTO) > 0L);
    }

    @Test
    @Transactional
    void login() {
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setNickName("bite_hahaha");
        sysUserDTO.setIdentity("platform_admin");
        sysUserDTO.setPhoneNumber("12345678901");
        sysUserDTO.setPassword("123456");
        sysUserDTO.setStatus("enable");
        sysUserService.addOrEditUser(sysUserDTO);
        PasswordLoginDTO passwordLoginDTO = new PasswordLoginDTO();
        passwordLoginDTO.setPhone(sysUserDTO.getPhoneNumber());
        passwordLoginDTO.setPassword(AESUtil.encryptHex(sysUserDTO.getPassword()));
        Assertions.assertTrue( sysUserService.login(passwordLoginDTO) != null);
    }

    @Test
    @Transactional
    void getUserList() {
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setNickName("bite_hahaha");
        sysUserDTO.setIdentity("platform_admin");
        sysUserDTO.setPhoneNumber("12345678901");
        sysUserDTO.setPassword("123456");
        sysUserDTO.setStatus("enable");
        sysUserService.addOrEditUser(sysUserDTO);
        SysUserListReqDTO sysUserListReqDTO = new SysUserListReqDTO();
        sysUserListReqDTO.setPhoneNumber("12345678901");
        Assertions.assertTrue( !sysUserService.getUserList(sysUserListReqDTO).isEmpty());
    }

    @Test
    @Transactional
    void getLoginUser() {
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setNickName("bite_hahaha");
        sysUserDTO.setIdentity("platform_admin");
        sysUserDTO.setPhoneNumber("12345678901");
        sysUserDTO.setPassword("123456");
        sysUserDTO.setStatus("enable");
        sysUserService.addOrEditUser(sysUserDTO);
        PasswordLoginDTO passwordLoginDTO = new PasswordLoginDTO();
        passwordLoginDTO.setPhone(sysUserDTO.getPhoneNumber());
        passwordLoginDTO.setPassword(AESUtil.encryptHex(sysUserDTO.getPassword()));
        String token = sysUserService.login(passwordLoginDTO).getAccessToken();
        Assertions.assertTrue( tokenService.getLoginUser(token) != null);
    }
}