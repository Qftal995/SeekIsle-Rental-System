package com.seekisle.adminapi.appuser.domain.dto;

import com.seekisle.commondomain.domain.dto.BasePageReqDTO;
import lombok.Data;

/**
 * 查询C端用户DTO
 */
@Data
public class AppUserListReqDTO extends BasePageReqDTO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * openId
     */
    private String openId;

}