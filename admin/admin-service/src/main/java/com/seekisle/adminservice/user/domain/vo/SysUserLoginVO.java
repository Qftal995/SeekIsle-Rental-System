package com.seekisle.adminservice.user.domain.vo;

import com.seekisle.commondomain.domain.vo.LoginUserVO;
import lombok.Data;

/**
 * B端用户登录信息
 */
@Data
public class SysUserLoginVO extends LoginUserVO {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户身份
     */
    private String identity;

    /**
     * 用户状态
     */
    private String status;
}
