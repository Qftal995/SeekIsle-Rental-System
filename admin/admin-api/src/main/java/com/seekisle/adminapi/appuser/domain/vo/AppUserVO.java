package com.seekisle.adminapi.appuser.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户VO
 */
@Data
public class AppUserVO implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * openId
     */
    private String openId;

}
