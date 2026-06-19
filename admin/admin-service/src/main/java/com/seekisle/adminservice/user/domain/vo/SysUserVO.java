package com.seekisle.adminservice.user.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * B端用户VO信息
 */
@Data
public class SysUserVO implements Serializable {

    /**
     * B端人员id
     */
    private Long userId;

    /**
     * 身份
     */
    private String identity;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
