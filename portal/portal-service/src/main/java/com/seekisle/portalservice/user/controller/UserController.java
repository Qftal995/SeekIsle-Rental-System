package com.seekisle.portalservice.user.controller;

import com.seekisle.commonsecurity.domain.dto.TokenDTO;
import com.seekisle.portalservice.user.entity.dto.CodeLoginDTO;
import com.seekisle.adminapi.appuser.domain.dto.UserEditReqDTO;
import com.seekisle.portalservice.user.entity.dto.WechatLoginDTO;
import com.seekisle.commondomain.domain.R;
import com.seekisle.commondomain.domain.vo.TokenVO;
import com.seekisle.portalservice.user.entity.vo.UserVO;
import com.seekisle.portalservice.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序用户接口
 */
@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    /**
     * 小程序端用户服务类对象
     */
    @Autowired
    private IUserService userService;

    /**
     * 发送短信验证码
     *
     * @param phone 用户手机号
     * @return 验证码
     */
    @GetMapping("/send_code")
    public R<String> sendCode(String phone) {
        return R.ok(userService.sendCode(phone));
    }

    /**
     * 短信登录
     *
     * @param codeLoginDTO 短信登录信息
     * @return token信息
     */
    @PostMapping("/login/code")
    public R<TokenVO> login(@Validated @RequestBody CodeLoginDTO codeLoginDTO) {
        // 用户登录，获取登录token
        TokenDTO tokenDTO = userService.login(codeLoginDTO);
        return R.ok(tokenDTO.convertToVO());
    }

    /**
     * 微信登录
     *
     * @param wechatLoginDTO 微信登录信息
     * @return token信息
     */
    @PostMapping("/login/wechat")
    public R<TokenVO> login(@Validated @RequestBody WechatLoginDTO wechatLoginDTO) {
        TokenDTO tokenDTO = userService.login(wechatLoginDTO);
        return R.ok(tokenDTO.convertToVO());
    }

    /**
     * 退出登录
     * @return
     */
    @DeleteMapping("/logout")
    public R<?> logout() {
        userService.logout();
        return R.ok();
    }

    /**
     * 修改用户信息
     * @param userEditReqDTO 用户编辑DTO
     * @return void
     */
    @PostMapping("/edit")
    public R<Void> edit(@Validated @RequestBody UserEditReqDTO userEditReqDTO) {
        userService.edit(userEditReqDTO);
        return R.ok();
    }

    /**
     * 获取用户登录信息
     *
     * @return 用户信息
     */
    @GetMapping("/login_info/get")
    public R<UserVO> getLoginUser() {
        return R.ok(userService.getLoginUser().convertToVO());
    }
}
