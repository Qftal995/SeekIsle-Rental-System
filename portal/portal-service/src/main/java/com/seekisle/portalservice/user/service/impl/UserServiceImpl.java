package com.seekisle.portalservice.user.service.impl;

import com.seekisle.adminapi.appuser.domain.dto.*;
import com.seekisle.adminapi.appuser.domain.vo.AppUserVO;
import com.seekisle.adminapi.appuser.feign.AppUserFeignClient;
import com.seekisle.commoncore.utils.VerifyUtil;
import com.seekisle.commondomain.domain.ResultCode;
import com.seekisle.commondomain.domain.R;
import com.seekisle.commondomain.exception.ServiceException;
import com.seekisle.commonmessage.service.CaptchaService;
import com.seekisle.commonsecurity.domain.dto.LoginUserDTO;
import com.seekisle.commonsecurity.domain.dto.TokenDTO;
import com.seekisle.commonsecurity.service.TokenService;
import com.seekisle.commonsecurity.utils.JwtUtil;
import com.seekisle.commonsecurity.utils.SecurityUtil;
import com.seekisle.portalservice.user.entity.dto.CodeLoginDTO;
import com.seekisle.portalservice.user.entity.dto.LoginDTO;
import com.seekisle.portalservice.user.entity.dto.UserDTO;
import com.seekisle.portalservice.user.entity.dto.WechatLoginDTO;
import com.seekisle.portalservice.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 小程序端用户服务实现
 */
@Component
@Slf4j
public class UserServiceImpl implements IUserService {

    /**
     * C端用户远程调用声明
     */
    @Autowired
    private AppUserFeignClient appUserFeignClient;

    /**
     * token服务
     */
    @Autowired
    private TokenService tokenService;

    /**
     * 验证码服务
     */
    @Autowired
    private CaptchaService captchaService;

    /**
     * 发送短信验证码
     *
     * @param phone 用户手机号
     * @return 验证码
     */
    @Override
    public String sendCode(String phone) {
        if (!VerifyUtil.checkPhone(phone)) {
            throw new ServiceException("手机号格式错误", ResultCode.INVALID_PARA.getCode());
        }
        return captchaService.sendCode(phone);
    }

    /**
     * 登录
     *
     * @param loginDTO 登录DTO
     * @return token信息
     */
    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        // LoginUserDTO 结构为了维护登录用户生命周期
        // 因此登录完成会设置用户信息、token、生命周期等属性
        LoginUserDTO loginUserDTO = new LoginUserDTO();

        // 类型检查和类型转换，Java 14及以上版本中的模式匹配特性
        if (loginDTO instanceof CodeLoginDTO codeLoginDTO) {
            loginByCode(codeLoginDTO, loginUserDTO);
        } else if (loginDTO instanceof WechatLoginDTO wechatLoginDTO) {
            loginByWechat(wechatLoginDTO, loginUserDTO);
        } else {
            throw new ServiceException("无效的登录方式！", ResultCode.INVALID_PARA.getCode());
        }

        // 该方法会设置用户token、生命周期，并返回 Token
        loginUserDTO.setUserFrom("app");
        return tokenService.createToken(loginUserDTO);
    }

    /**
     * 退出登录
     */
    @Override
    public void logout() {
        String token = SecurityUtil.getToken();
        if (StringUtils.isEmpty(token)) {
            return;
        }
        String username = JwtUtil.getUserName(token);
        String userId = JwtUtil.getUserId(token);
        log.info("{}退出了，用户id:{}", username, userId);
        // 删除用户缓存记录
        tokenService.delLoginUser(token);
    }

    /**
     * 修改用户信息
     *
     * @param userEditReqDTO 用户编辑DTO
     */
    @Override
    public void edit(UserEditReqDTO userEditReqDTO) {
        R<Void> result = appUserFeignClient.edit(userEditReqDTO);
        if (null == result || result.getCode() != ResultCode.SUCCESS.getCode()) {
            throw new ServiceException("修改用户失败！", ResultCode.INVALID_PARA.getCode());
        }
    }

    /**
     * 获取用户登录信息
     *
     * @return 用户信息
     */
    @Override
    public UserDTO getLoginUser() {

        // 获取用户登录信息
        LoginUserDTO loginUserDTO = tokenService.getLoginUser();
        if (null == loginUserDTO) {
            throw new ServiceException("用户token有误！", ResultCode.INVALID_PARA.getCode());
        }

        // 获取用户信息
        R<AppUserVO> result = appUserFeignClient.findById(loginUserDTO.getUserId());
        if (null == result
                || result.getCode() != ResultCode.SUCCESS.getCode()
                || null == result.getData()) {
            throw new ServiceException("查询用户失败！", ResultCode.ERROR.getCode());
        }
        AppUserVO appUserVO = result.getData();
        UserDTO userDTO = new UserDTO();
        userDTO.setAvatar(appUserVO.getAvatar());
        userDTO.setToken(loginUserDTO.getToken());
        userDTO.setUserId(loginUserDTO.getUserId());
        userDTO.setUserName(loginUserDTO.getUserName());
        userDTO.setLoginTime(loginUserDTO.getLoginTime());
        userDTO.setExpireTime(loginUserDTO.getExpireTime());
        return userDTO;
    }

    /**
     * 微信小程序一键登录
     *
     * @param wechatLoginDTO
     * @param loginUserDTO
     */
    private void loginByWechat(WechatLoginDTO wechatLoginDTO, LoginUserDTO loginUserDTO) {

        AppUserVO appUserVO;
        R<AppUserVO> result =  appUserFeignClient.findByOpenId(wechatLoginDTO.getOpenId());
        if (null == result
                || result.getCode() != ResultCode.SUCCESS.getCode()
                || null == result.getData()) {
            // 1.1、查无此人，去注册
            appUserVO = register(2, wechatLoginDTO.getOpenId());
            if (null == appUserVO) {
                throw new ServiceException("注册用户失败！");
            }
        } else {
            appUserVO = result.getData();
        }

        // 设置登录信息
        loginUserDTO.setUserId(appUserVO.getUserId());
        loginUserDTO.setUserName(appUserVO.getNickName());
    }

    /**
     * 验证码登录
     *
     * @param codeLoginDTO
     * @param loginUserDTO
     */
    public void loginByCode(CodeLoginDTO codeLoginDTO, LoginUserDTO loginUserDTO) {

        // 1、校验手机号
        if (!VerifyUtil.checkPhone(codeLoginDTO.getPhone())) {
            throw new ServiceException("手机号格式错误", ResultCode.INVALID_PARA.getCode());
        }

        AppUserVO appUserVO;
        R<AppUserVO> result =  appUserFeignClient.findByPhone(codeLoginDTO.getPhone());
        if (null == result
                || result.getCode() != ResultCode.SUCCESS.getCode()
                || null == result.getData()) {
            // 1.1、查无此人，去注册
            appUserVO = register(1, codeLoginDTO.getPhone());
            if (null == appUserVO) {
                throw new ServiceException("注册用户失败！");
            }
        } else {
            appUserVO = result.getData();
        }

        // 2、校验验证码
        String cacheValue = captchaService.getCode(codeLoginDTO.getPhone());
        if (StringUtils.isEmpty(cacheValue)) {
            throw new ServiceException("输入的验证码无效", ResultCode.INVALID_PARA.getCode());
        }
        if (!cacheValue.equals(codeLoginDTO.getCode())) {
            throw new ServiceException("输入的验证码错误", ResultCode.INVALID_PARA.getCode());
        }

        // 3、校验通过，删除缓存验证码
        captchaService.deleteCode(codeLoginDTO.getPhone());

        // 4、设置登录信息
        loginUserDTO.setUserId(appUserVO.getUserId());
        loginUserDTO.setUserName(appUserVO.getNickName());
    }

    private AppUserVO register(int op, String info) {
        R<AppUserVO> appUserVOR;

        if (1 == op) {
            appUserVOR = appUserFeignClient.registerByPhone(info);
        } else if (2 == op) {
            appUserVOR = appUserFeignClient.registerByOpenId(info);
        } else {
            return null;
        }

        if (null == appUserVOR
                || appUserVOR.getCode() != ResultCode.SUCCESS.getCode()
                || null == appUserVOR.getData()) {
            // 注册失败
            log.error("用户注册失败！{}:{}", op, info);
            return null;
        }
        return appUserVOR.getData();
    }

}
