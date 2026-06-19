package com.seekisle.adminservice.user.domain.entity;


import com.seekisle.commoncore.domain.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * C端用户对象 app_user
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class AppUser extends BaseDO {

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 微信openId
     */
    private String openId;

}
