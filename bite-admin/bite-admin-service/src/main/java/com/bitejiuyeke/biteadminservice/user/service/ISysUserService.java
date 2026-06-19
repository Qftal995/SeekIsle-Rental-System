package com.bitejiuyeke.biteadminservice.user.service;

import com.bitejiuyeke.biteadminservice.user.domain.dto.PasswordLoginDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserListReqDTO;
import com.bitejiuyeke.biteadminservice.user.domain.dto.SysUserLoginDTO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.TokenDTO;


import java.util.List;

/**
 * B端用户相关服务
 */
public interface ISysUserService {


    /**
     * 管理员密码登录
     *
     * @param loginDTO 用户登录DTO
     * @return token 信息
     */
    TokenDTO login(PasswordLoginDTO loginDTO);

    /**
     * 查询用户列表
     *
     * @param sysUserListReqDTO 用户查询DTO
     * @return B端用户信息
     */
    List<SysUserDTO> getUserList(SysUserListReqDTO sysUserListReqDTO);

    /**
     * 新增或修改用户信息
     *      修改只能修改昵称、备注、状态，其他参数传了也不改，保证业务
     *      修改时，需带上昵称、备注、状态。逻辑为直接覆盖原有数据
     *
     * @param sysUserDTO 用户新增/编辑DTO
     * @return 用户id
     */
    Long addOrEditUser(SysUserDTO sysUserDTO);

    /**
     * 获取管理员登录信息
     *
     * @return 登录信息
     */
    SysUserLoginDTO getLoginUser();

}
