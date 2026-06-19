package com.seekisle.adminapi.appuser.domain.dto;

import com.seekisle.adminapi.appuser.domain.vo.AppUserVO;
import lombok.Data;

import java.io.Serializable;

/**
 * C端用户DTO
 */
@Data
public class AppUserDTO implements Serializable {

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

    /**
     * 构造函数
     * @return AppUserVO
     */
    public AppUserVO convertToVO() {
        AppUserVO appUserVO = new AppUserVO();
        appUserVO.setUserId(this.userId);
        appUserVO.setNickName(this.nickName);
        appUserVO.setPhoneNumber(this.phoneNumber);
        appUserVO.setAvatar(this.avatar);
        appUserVO.setOpenId(this.openId);
        return appUserVO;
    }

}
