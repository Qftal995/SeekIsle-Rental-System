package com.seekisle.adminservice.user.controller;

import com.seekisle.adminservice.user.domain.dto.PasswordLoginDTO;
import com.seekisle.adminservice.user.domain.dto.SysUserDTO;
import com.seekisle.adminservice.user.domain.dto.SysUserListReqDTO;
import com.seekisle.adminservice.user.domain.vo.SysUserLoginVO;
import com.seekisle.adminservice.user.domain.vo.SysUserVO;
import com.seekisle.adminservice.user.service.ISysUserService;
import com.seekisle.commondomain.domain.R;
import com.seekisle.commondomain.domain.vo.TokenVO;
import com.seekisle.commonsecurity.domain.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * B端用户服务接口
 */
@RestController
@RequestMapping("/sys_user")
public class SysUserController {

    /**
     * B端用户相关服务
     */
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 管理员密码登录
     *
     * @param loginDTO 用户登录DTO
     * @return token 信息
     */
    @PostMapping("/login/password")
    public R<TokenVO> login(@Validated @RequestBody PasswordLoginDTO loginDTO) {
        // 用户登录，获取登录token
        TokenDTO tokenDTO = sysUserService.login(loginDTO);
        return R.ok(tokenDTO.convertToVO());
    }

    /**
     * 查询用户列表
     *
     * @param sysUserListReqDTO 用户查询DTO
     * @return B端用户信息
     */
    @PostMapping("/list")
    public R<List<SysUserVO>> getUserList(@RequestBody SysUserListReqDTO sysUserListReqDTO) {
        List<SysUserDTO> sysUserDTOS = sysUserService.getUserList(sysUserListReqDTO);
        return R.ok(sysUserDTOS.stream()
                        .map(SysUserDTO::convertToVO)
                        .collect(Collectors.toList()));
    }

    /**
     * 新增/编辑用户
     * @param sysUserDTO B端用户信息
     * @return  用户ID
     */
    @PostMapping("/add_edit")
    public R<Long> addOrEditUser(@RequestBody SysUserDTO sysUserDTO) {
        return R.ok(sysUserService.addOrEditUser(sysUserDTO));
    }

    /**
     * 获取管理员登录信息
     * @return 管理员登录信息
     */
    @GetMapping("/login_info/get")
    public R<SysUserLoginVO> getLoginUser() {
        return R.ok(sysUserService.getLoginUser().convertToVO());
    }

}
