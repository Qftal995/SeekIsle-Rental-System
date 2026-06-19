package com.bitejiuyeke.biteadminservice.user.controller;

import com.bitejiuyeke.biteadminapi.appuser.domain.dto.UserEditReqDTO;
import com.bitejiuyeke.biteadminapi.appuser.feign.AppUserFeignClient;
import com.bitejiuyeke.biteadminapi.appuser.domain.dto.AppUserDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.dto.AppUserListReqDTO;
import com.bitejiuyeke.biteadminapi.appuser.domain.vo.AppUserVO;
import com.bitejiuyeke.biteadminservice.user.service.IAppUserService;
import com.bitejiuyeke.bitecommoncore.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * C端用户相关接口
 */
@RestController
@RequestMapping("/app_user")
public class AppUserController implements AppUserFeignClient {

    /**
     * C端用户相关服务对象
     */
    @Autowired
    private IAppUserService appUserService;

    /**
     * 查询C端用户
     * @param appUserListReqDTO 查询C端用户DTO
     * @return C端用户分页结果
     */
    @PostMapping("/list/search")
    public R<BasePageVO<AppUserVO>> list(@RequestBody AppUserListReqDTO appUserListReqDTO) {
        BasePageDTO<AppUserDTO> appUserDTOS = appUserService.getUserList(appUserListReqDTO);
        BasePageVO<AppUserVO> result = new BasePageVO<>();
        BeanUtils.copyProperties(appUserDTOS, result);
        return R.ok(result);
    }

    /**
     * 编辑用户
     *
     * @param userEditReqDTO 用户编辑DTO
     * @return void类型
     */
    @Override
    public R<Void> edit(@Validated @RequestBody UserEditReqDTO userEditReqDTO) {
        appUserService.edit(userEditReqDTO);
        return R.ok();
    }

    /**
     * 根据userId查询用户信息
     *
     * @param userId 用户ID
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> findById(@RequestParam Long userId) {
        AppUserDTO appUserDTO = appUserService.findById(userId);
        return null == appUserDTO ? R.ok() : R.ok(appUserDTO.convertToVO());
    }

    /**
     * 根据phone查询用户信息
     *
     * @param phoneNumber 用户手机号
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> findByPhone(@RequestParam String phoneNumber) {
        AppUserDTO appUserDTO = appUserService.findByPhone(phoneNumber);
        return null == appUserDTO ? R.ok() : R.ok(appUserDTO.convertToVO());
    }

    /**
     * 根据openId查询用户信息
     *
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> findByOpenId(@RequestParam String openId) {
        AppUserDTO appUserDTO = appUserService.findByOpenId(openId);
        return null == appUserDTO ? R.ok() : R.ok(appUserDTO.convertToVO());
    }

    /**
     * 批量查询用户列表
     *
     * @param userIds 用户ID列表
     * @return C端用户VO列表
     */
    @Override
    public R<List<AppUserVO>> list(@RequestBody List<Long> userIds) {
        List<AppUserDTO> appUserDTOS = appUserService.getUserList(userIds);
        return R.ok(appUserDTOS.stream()
                .filter(Objects::nonNull)
                .map(AppUserDTO::convertToVO)
                .collect(Collectors.toList()));
    }

    /**
     * 手机号注册
     *
     * @param phoneNumber 用户手机号
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> registerByPhone(@RequestParam String phoneNumber) {
        AppUserDTO appUserDTO = appUserService.registerByPhone(phoneNumber);
        if (null == appUserDTO) {
            throw new ServiceException("注册失败");
        }
        return R.ok(appUserDTO.convertToVO());
    }

    /**
     * 微信注册
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> registerByOpenId(@RequestParam String openId) {
        AppUserDTO appUserDTO = appUserService.registerByOpenId(openId);
        if (null == appUserDTO) {
            throw new ServiceException("注册失败");
        }
        return R.ok(appUserDTO.convertToVO());
    }

}
