package com.seekisle.adminservice.user.service.impl;


import cn.hutool.crypto.digest.DigestUtil;
import com.seekisle.adminapi.config.domain.dto.DictionaryDataDTO;
import com.seekisle.adminservice.config.service.ISysDictionaryService;
import com.seekisle.adminservice.user.domain.dto.PasswordLoginDTO;
import com.seekisle.adminservice.user.domain.dto.SysUserDTO;
import com.seekisle.adminservice.user.domain.dto.SysUserListReqDTO;
import com.seekisle.adminservice.user.domain.dto.SysUserLoginDTO;
import com.seekisle.adminservice.user.domain.entity.SysUser;
import com.seekisle.adminservice.user.mapper.SysUserMapper;
import com.seekisle.adminservice.user.service.ISysUserService;
import com.seekisle.commoncore.utils.AESUtil;
import com.seekisle.commoncore.utils.VerifyUtil;
import com.seekisle.commondomain.domain.ResultCode;
import com.seekisle.commondomain.exception.ServiceException;
import com.seekisle.commonsecurity.domain.dto.LoginUserDTO;
import com.seekisle.commonsecurity.domain.dto.TokenDTO;
import com.seekisle.commonsecurity.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * B端用户登录服务
 */
@Slf4j
@Service
public class SysUserServiceImpl implements ISysUserService {

    /**
     * token服务
     */
    @Autowired
    private TokenService tokenService;

    /**
     * B端用户相关
     */
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 字典类型表 服务类
     */
    @Autowired
    private ISysDictionaryService sysDictionaryService;

    /**
     * 管理员密码登录
     *
     * @param passwordLoginDTO 用户登录DTO
     * @return token 信息
     */
    @Override
    public TokenDTO login(PasswordLoginDTO passwordLoginDTO) {

        // LoginUserDTO 结构为了维护登录用户生命周期
        // 因此登录完成会设置用户信息、token、生命周期等属性
        LoginUserDTO loginUserDTO = new LoginUserDTO();

        // 1、校验手机号
        if (!VerifyUtil.checkPhone(passwordLoginDTO.getPhone())) {
            throw new ServiceException("账号密码错误，请确认后重新登录", ResultCode.INVALID_PARA.getCode());
        }

        SysUser sysUser = sysUserMapper.selectByPhoneNumber(
                AESUtil.encryptHex(passwordLoginDTO.getPhone()));
        if (null == sysUser) {
            throw new ServiceException("账号密码错误，请确认后重新登录", ResultCode.INVALID_PARA.getCode());
        }

        // 2、校验密码
        // 先解密，再加密（aes）：为了不让其明文传输。需要前后端约定密钥
        String password = AESUtil.decryptStr(passwordLoginDTO.getPassword());
        if (StringUtils.isEmpty(password)) {
            throw new ServiceException("密码加密错误，请确认后重新登录", ResultCode.INVALID_PARA.getCode());
        }
        String passwordEncrypt = DigestUtil.sha256Hex(password);
        if (!passwordEncrypt.equals(sysUser.getPassword())) {
            throw new ServiceException("账号密码错误，请确认后重新登录", ResultCode.INVALID_PARA.getCode());
        }

        // 3、校验用户状态
        if ("disable".equalsIgnoreCase(sysUser.getStatus())) {
            throw new ServiceException(ResultCode.USER_DISABLE);
        }

        // 4、设置登录信息
        loginUserDTO.setUserId(sysUser.getId());
        loginUserDTO.setUserName(sysUser.getNickName());
        loginUserDTO.setUserFrom("sys");
        // 该方法会设置用户token、生命周期，并返回 Token
        return tokenService.createToken(loginUserDTO);
    }

    /**
     * 查询用户列表
     *
     * @param sysUserListReqDTO 用户查询DTO
     * @return B端用户信息
     */
    @Override
    public List<SysUserDTO> getUserList(SysUserListReqDTO sysUserListReqDTO) {

        SysUser searchSysUser = new SysUser();
        searchSysUser.setId(sysUserListReqDTO.getUserId());
        searchSysUser.setStatus(sysUserListReqDTO.getStatus());
        if (StringUtils.isNotEmpty(sysUserListReqDTO.getPhoneNumber())) {
            searchSysUser.setPhoneNumber(
                    AESUtil.encryptHex(sysUserListReqDTO.getPhoneNumber()));
        }
        List<SysUser> sysUserList = sysUserMapper.selectList(searchSysUser);

        return sysUserList.stream()
                .map(sysUser -> {
                    SysUserDTO sysUserDTO = new SysUserDTO();
                    sysUserDTO.setUserId(sysUser.getId());
                    sysUserDTO.setPhoneNumber(
                            AESUtil.decryptStr(sysUser.getPhoneNumber()));
                    sysUserDTO.setNickName(sysUser.getNickName());
                    sysUserDTO.setRemark(sysUser.getRemark());
                    sysUserDTO.setIdentity(sysUser.getIdentity());
                    sysUserDTO.setStatus(sysUser.getStatus());
                    return sysUserDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 新增或修改用户信息
     *      修改只能修改昵称、备注、状态，其他参数传了也不改，保证业务
     *      修改时，需带上昵称、备注、状态。逻辑为直接覆盖原有数据
     *
     * @param sysUserDTO 用户新增/编辑DTO
     * @return 用户id
     */
    @Override
    public Long addOrEditUser(SysUserDTO sysUserDTO) {

        SysUser sysUser = new SysUser();
        if (null == sysUserDTO.getUserId()) {
            // 新增用户
            if (StringUtils.isEmpty(sysUserDTO.getPassword())
                    || !sysUserDTO.checkPassword()) {
                throw new ServiceException("密码为空或校验失败", ResultCode.INVALID_PARA.getCode());
            }
            if (!VerifyUtil.checkPhone(sysUserDTO.getPhoneNumber())) {
                throw new ServiceException("手机号格式错误", ResultCode.INVALID_PARA.getCode());
            }
            SysUser existSysUser = sysUserMapper.selectByPhoneNumber(
                    AESUtil.encryptHex(sysUserDTO.getPhoneNumber()));
            if (null != existSysUser) {
                throw new ServiceException("手机号已注册", ResultCode.INVALID_PARA.getCode());
            }
            if (StringUtils.isEmpty(sysUserDTO.getIdentity())
                    || null == sysDictionaryService.selectDictDataByDataKey(sysUserDTO.getIdentity())) {
                throw new ServiceException("用户身份错误", ResultCode.INVALID_PARA.getCode());
            }

            // 新增用户才设置，修改用户传了也不改，保证业务
            sysUser.setPassword(
                    DigestUtil.sha256Hex(sysUserDTO.getPassword()));
            sysUser.setPhoneNumber(
                    AESUtil.encryptHex(sysUserDTO.getPhoneNumber()));
            sysUser.setIdentity(sysUserDTO.getIdentity());
        }

        sysUser.setId(sysUserDTO.getUserId());
        sysUser.setNickName(sysUserDTO.getNickName());
        if (null == sysDictionaryService.selectDictDataByDataKey(sysUserDTO.getStatus())) {
            // 要改，但是传错了
            throw new ServiceException("用户状态传参错误", ResultCode.INVALID_PARA.getCode());
        }
        sysUser.setStatus(sysUserDTO.getStatus());
        sysUser.setRemark(sysUserDTO.getRemark());
        sysUserMapper.insertOrUpdate(sysUser);

        // 踢人
        if (null != sysUserDTO.getUserId()
                && "disable".equalsIgnoreCase(sysUserDTO.getStatus())) {
            tokenService.delUser(sysUserDTO.getUserId(), "sys");
        }

        return sysUser.getId();
    }

    /**
     * 获取管理员登录信息
     *
     * @return 登录信息
     */
    @Override
    public SysUserLoginDTO getLoginUser() {
        // 获取用户登录信息
        LoginUserDTO loginUserDTO = tokenService.getLoginUser();
        if (null == loginUserDTO || null == loginUserDTO.getUserId()) {
            throw new ServiceException("用户token有误！", ResultCode.INVALID_PARA.getCode());
        }

        // 获取用户信息
        SysUser sysUser = sysUserMapper.selectById(loginUserDTO.getUserId());
        if (null == sysUser) {
            throw new ServiceException("获取用户信息失败！");
        }
        SysUserLoginDTO sysUserLoginDTO = new SysUserLoginDTO();
        sysUserLoginDTO.setNickName(sysUser.getNickName());
        sysUserLoginDTO.setIdentity(sysUser.getIdentity());
        sysUserLoginDTO.setStatus(sysUser.getStatus());
        sysUserLoginDTO.setToken(loginUserDTO.getToken());
        sysUserLoginDTO.setUserId(loginUserDTO.getUserId());
        sysUserLoginDTO.setUserName(loginUserDTO.getUserName());
        sysUserLoginDTO.setLoginTime(loginUserDTO.getLoginTime());
        sysUserLoginDTO.setExpireTime(loginUserDTO.getExpireTime());
        return sysUserLoginDTO;

    }

}
