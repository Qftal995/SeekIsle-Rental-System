package com.bitejiuyeke.biteadminapi.appuser.domain.dto;

import lombok.Data;

/**
 * 用户查询参数
 */
@Data
public class AppUserFindReqDTO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 根据昵称模糊查询
     */
    private String nickName;

}
