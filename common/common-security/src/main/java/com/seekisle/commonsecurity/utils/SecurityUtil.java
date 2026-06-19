package com.seekisle.commonsecurity.utils;

import com.seekisle.commoncore.utils.ServletUtil;
import com.seekisle.commondomain.constants.SecurityConstants;
import com.seekisle.commondomain.constants.TokenConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * 安全工具类
 */
public class SecurityUtil {
    /**
     * 获取请求token
     * @return token信息
     */
    public static String getToken() {
        return getToken(ServletUtil.getRequest());
    }

    /**
     * 根据request获取请求token
     * @param request 请求
     * @return token信息
     */
    public static String getToken(HttpServletRequest request) {
        // 从header获取token标识
        String token = request.getHeader(SecurityConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     * @param token 可能有前缀的token
     * @return token信息
     */
    public static String replaceTokenPrefix(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }
}
