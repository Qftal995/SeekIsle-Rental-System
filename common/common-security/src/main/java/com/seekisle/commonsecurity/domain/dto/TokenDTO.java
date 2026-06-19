package com.seekisle.commonsecurity.domain.dto;

import com.seekisle.commondomain.domain.vo.TokenVO;
import lombok.Data;

/**
 * token 信息
 */
@Data
public class TokenDTO {
    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 过期时间
     */
    private Long expires;

    /**
     * 转换vo
     *
     * @return tokenvo信息
     */
    public TokenVO convertToVO() {
        TokenVO tokenVO = new TokenVO();
        tokenVO.setAccessToken(this.accessToken);
        tokenVO.setExpires(this.expires);
        return tokenVO;
    }
}
