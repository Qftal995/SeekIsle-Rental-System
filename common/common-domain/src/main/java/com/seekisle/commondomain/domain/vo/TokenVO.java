package com.seekisle.commondomain.domain.vo;

import lombok.Data;

/**
 * 登录响应token
 */
@Data
public class TokenVO {
    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 过期时间
     */
    private Long expires;
}
