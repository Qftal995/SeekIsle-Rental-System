package com.bitejiuyeke.biteportalservice.user.service;

import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;
import com.bitejiuyeke.biteportalservice.user.entity.dto.LoginDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.dto.UserEditReqDTO;
import com.bitejiuyeke.biteportalservice.user.entity.dto.UserDTO;

/**
 * 小程序端用户服务
 */
public interface IUserService {

    /**
     * 发送短信验证码
     *
     * @param phone 用户手机号
     * @return 验证码
     */
    String sendCode(String phone);

    /**
     * 登录
     *
     * @param loginDTO 登录DTO
     * @return token信息
     */
    TokenDTO login(LoginDTO loginDTO);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 修改用户信息
     *
     * @param userEditReqDTO 用户编辑DTO
     */
    void edit(UserEditReqDTO userEditReqDTO);

    /**
     * 获取用户登录信息
     *
     * @return 用户信息
     */
    UserDTO getLoginUser();
}
