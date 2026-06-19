package com.bitejiuyeke.biteadminapi.appuser.feign;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.*;
import com.bitejiuyeke.biteadminapi.appuser.domain.vo.AppUserVO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * C端用户远程调用声明
 */
@FeignClient(contextId = "appUserFeignClient", value = "bite-admin", path = "/app_user")
public interface AppUserFeignClient {

    /**
     * 编辑用户
     *
     * @param userEditReqDTO 用户编辑DTO
     * @return void类型
     */
    @PostMapping("/edit")
    R<Void> edit(@Validated @RequestBody UserEditReqDTO userEditReqDTO);

    /**
     * 根据userId查询用户信息
     *
     * @param userId 用户ID
     * @return C端用户VO
     */
    @GetMapping("/id_find")
    R<AppUserVO> findById(@RequestParam Long userId);

    /**
     * 根据phone查询用户信息
     *
     * @param phoneNumber 用户手机号
     * @return C端用户VO
     */
    @GetMapping("/phone_find")
    R<AppUserVO> findByPhone(@RequestParam String phoneNumber);

    /**
     * 根据openId查询用户信息
     *
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @GetMapping("/open_id_find")
    R<AppUserVO> findByOpenId(@RequestParam String openId);

    /**
     * 批量查询用户列表
     *
     * @param userIds 用户ID列表
     * @return C端用户VO列表
     */
    @PostMapping("/list")
    R<List<AppUserVO>> list(@RequestBody List<Long> userIds);

    /**
     * 手机号注册
     *
     * @param phoneNumber 用户手机号
     * @return C端用户VO
     */
    @GetMapping("/register/phone")
    R<AppUserVO> registerByPhone(@RequestParam String phoneNumber);

    /**
     * 微信注册
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @GetMapping("/register/openid")
    R<AppUserVO> registerByOpenId(@RequestParam String openId);
}
