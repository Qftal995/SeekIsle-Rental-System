package com.bitejiuyeke.biteadminservice.user.domain.dto;

import com.bitejiuyeke.biteadminservice.user.domain.vo.SysUserLoginVO;
import com.bitejiuyeke.bitecommonsecurity.domain.dto.LoginUserDTO;
import lombok.Data;

/**
 * B端用户登录信息
 */
@Data
public class SysUserLoginDTO extends LoginUserDTO {
    private String nickName;

    private String identity;

    private String status;

    /**
     * B端用户登录信息DTO转换VO
     * @return B端用户登录信息VO
     */
    public SysUserLoginVO convertToVO() {
        SysUserLoginVO sysUserLoginVO = new SysUserLoginVO();
        sysUserLoginVO.setIdentity(this.identity);
        sysUserLoginVO.setStatus(this.status);
        sysUserLoginVO.setNickName(this.nickName);
        sysUserLoginVO.setUserId(this.getUserId());
        sysUserLoginVO.setToken(this.getToken());
        sysUserLoginVO.setUserName(this.getUserName());
        sysUserLoginVO.setLoginTime(this.getLoginTime());
        sysUserLoginVO.setExpireTime(this.getExpireTime());
        return sysUserLoginVO;
    }
}
