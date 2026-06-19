package com.seekisle.commonsecurity.service;

import com.seekisle.commoncore.utils.ServletUtil;
import com.seekisle.commondomain.constants.CacheConstants;
import com.seekisle.commondomain.constants.SecurityConstants;
import com.seekisle.commondomain.constants.TokenConstants;
import com.seekisle.commonsecurity.domain.dto.LoginUserDTO;
import com.seekisle.commonsecurity.domain.dto.TokenDTO;
import com.seekisle.commonredis.service.RedisService;
import com.seekisle.commonsecurity.utils.JwtUtil;
import com.seekisle.commonsecurity.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * token服务
 */
@Component
public class TokenService {

    /**
     * 毫秒
     */
    protected static final long MILLIS_SECOND = 1000;

    /**
     * 60s
     */
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    /**
     * 刷新周期
     */
    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 过期时间
     */
    private final static Long EXPIRE_TIME = CacheConstants.EXPIRATION;

    /**
     * token的key
     */
    private final static String ACCESS_TOKEN = TokenConstants.LOGIN_TOKEN_KEY;

    /**
     * redis服务信息
     */
    @Autowired
    private RedisService redisService;

    /**
     * 创建token
     *
     * @param loginUserDTO 登录信息
     * @return token信息
     */
    public TokenDTO createToken(LoginUserDTO loginUserDTO) {
        String token = UUID.randomUUID().toString();
        loginUserDTO.setToken(token);
        refreshToken(loginUserDTO);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.USER_ID, loginUserDTO.getUserId());
        claimsMap.put(SecurityConstants.USERNAME, loginUserDTO.getUserName());
        claimsMap.put(SecurityConstants.USER_FROM, loginUserDTO.getUserFrom());

        // 接口返回信息
        TokenDTO tokenDTO =new TokenDTO();
        tokenDTO.setAccessToken(JwtUtil.createToken(claimsMap));
        tokenDTO.setExpires(EXPIRE_TIME);
        return tokenDTO;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUserDTO getLoginUser() {
        return getLoginUser(ServletUtil.getRequest());
    }

    /**
     * 设置用户身份信息
     *
     * @param loginUserDTO 用户身份信息
     */
    public void setLoginUser(LoginUserDTO loginUserDTO) {
        if (null != loginUserDTO && StringUtils.isNotEmpty(loginUserDTO.getToken())) {
            refreshToken(loginUserDTO);
        }
    }

    /**
     * 获取用户身份信息
     * @param request 请求信息
     * @return 用户信息
     */
    public LoginUserDTO getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtil.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     * @param token token信息
     * @return 用户信息
     */
    public LoginUserDTO getLoginUser(String token) {
        LoginUserDTO user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = JwtUtil.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey),LoginUserDTO.class);
                return user;
            }
        } catch (Exception e) {
        }
        return user;
    }

    /**
     * 删除用户缓存信息
     * @param token 信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userkey = JwtUtil.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }

    /**
     * 删除指定用户缓存信息
     *
     * @param userId  用户id
     * @param userFrom 用户来源
     */
    public void delUser(Long userId, String userFrom) {
        if (null == userId) {
            return;
        }
        Collection<String> tokenKeys = redisService.keys(ACCESS_TOKEN + "*");
        for (String tokenkey : tokenKeys) {
            LoginUserDTO user = redisService.getCacheObject(tokenkey,LoginUserDTO.class);
            if (null != user
                    && userId.equals(user.getUserId())
                    && userFrom.equals(user.getUserFrom())) {
                redisService.deleteObject(tokenkey);
            }
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUserDTO 用户信息
     */
    public void verifyToken(LoginUserDTO loginUserDTO) {
        long expireTime = loginUserDTO.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUserDTO);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUserDTO 登录信息
     */
    public void refreshToken(LoginUserDTO loginUserDTO) {
        loginUserDTO.setLoginTime(System.currentTimeMillis());
        loginUserDTO.setExpireTime(loginUserDTO.getLoginTime() + EXPIRE_TIME * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUserDTO.getToken());
        redisService.setCacheObject(userKey, loginUserDTO, EXPIRE_TIME, TimeUnit.MINUTES);
    }

    /**
     * token key信息
     *
     * @param token token
     * @return  token key
     */
    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }
}
