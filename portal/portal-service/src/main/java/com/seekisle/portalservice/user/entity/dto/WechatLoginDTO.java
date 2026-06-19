package com.seekisle.portalservice.user.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 微信登录信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WechatLoginDTO extends LoginDTO {

    /**
     * openID
     */
    @NotBlank(message = "openID不能为空！")
    private String openId;

}
