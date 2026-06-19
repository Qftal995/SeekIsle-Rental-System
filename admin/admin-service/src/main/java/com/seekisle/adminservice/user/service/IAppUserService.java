package com.seekisle.adminservice.user.service;

import com.seekisle.adminapi.appuser.domain.dto.UserEditReqDTO;
import com.seekisle.adminapi.appuser.domain.dto.AppUserDTO;
import com.seekisle.adminapi.appuser.domain.dto.AppUserListReqDTO;
import com.seekisle.commoncore.domain.dto.BasePageDTO;


import java.util.List;

/**
 * C端用户相关服务
 */
public interface IAppUserService {


    /**
     * 搜索获取人员列表
     *
     * @param appUserListReqDTO 人员列表请求DTO
     * @return C端用户分页结果
     */
    BasePageDTO<AppUserDTO> getUserList(AppUserListReqDTO appUserListReqDTO);

    /**
     * 根据ids获取人员列表
     *
     * @param userIds 用户ID列表
     * @return C端用户DTO列表
     */
    List<AppUserDTO> getUserList(List<Long> userIds);

    /**
     * 编辑用户
     *
     * @param userEditReqDTO 用户编辑DTO
     */
    void edit(UserEditReqDTO userEditReqDTO);

    /**
     * 手机号注册
     *
     * @param phoneNumber 用户手机号
     * @return C端用户DTO
     */
    AppUserDTO registerByPhone(String phoneNumber);

    /**
     *微信注册
     *
     * @param openId 用户微信ID
     * @return C端用户DTO
     */
    AppUserDTO registerByOpenId(String openId);

    /**
     * 根据用户id查询
     * @param userId 用户ID
     * @return C端用户DTO
     */
    AppUserDTO findById(Long userId);

    /**
     * 根据用户手机号查询
     * @param phoneNumber 用户手机号
     * @return C端用户DTO
     */
    AppUserDTO findByPhone(String phoneNumber);

    /**
     * 根据用户微信id查询
     * @param openId 用户微信ID
     * @return C端用户DTO
     */
    AppUserDTO findByOpenId(String openId);
}
