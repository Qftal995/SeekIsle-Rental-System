package com.bitejiuyeke.bitegateway.filter;


import com.bitejiuyeke.bitecommoncore.utils.ServletUtil;
import com.bitejiuyeke.bitecommoncore.utils.StringUtil;
import com.bitejiuyeke.bitecommondomain.constants.SecurityConstants;
import com.bitejiuyeke.bitecommondomain.constants.TokenConstants;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.bitejiuyeke.bitecommonsecurity.utils.JwtUtil;
import com.bitejiuyeke.bitegateway.config.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    /**
     * 排除过滤的 uri 地址，nacos自行添加
     */
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    /**
     * redis服务
     */
    @Autowired
    private RedisService redisService;

    /**
     * 过滤
     *
     * @param exchange ServerWebExchange
     * @param chain 过滤链
     * @return 无
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtil.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, ResultCode.TOKEN_EMPTY);
        }
        Claims claims;
        try {
            claims = JwtUtil.parseToken(token);
            if (claims == null) {
                return unauthorizedResponse(exchange,  ResultCode.TOKEN_INVALID);
            }
        } catch (Exception e) {
            return unauthorizedResponse(exchange, ResultCode.TOKEN_OVERTIME);
        }

        String userkey = JwtUtil.getUserKey(claims);
        boolean islogin = redisService.hasKey(getTokenKey(userkey));
        if (!islogin) {
            return unauthorizedResponse(exchange,  ResultCode.LOGIN_STATUS_OVERTIME);
        }
        String userid = JwtUtil.getUserId(claims);
        String username = JwtUtil.getUserName(claims);
        String userFrom = JwtUtil.getUserFrom(claims);
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)) {
            return unauthorizedResponse(exchange, ResultCode.TOKEN_CHECK_FAILED);
        }

        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.USER_ID, userid);
        addHeader(mutate, SecurityConstants.USERNAME, username);
        addHeader(mutate, SecurityConstants.USER_FROM, userFrom);

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    /**
     * 添加header
     *
     * @param mutate 请求
     * @param name 名称信息
     * @param value 值信息
     */
    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtil.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    /**
     * 未授权
     *
     * @param exchange ServerWebExchange
     * @param resultCode 结果码
     * @return 无
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, ResultCode resultCode) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        int retCode = Integer.parseInt(String.valueOf(resultCode.getCode()).substring(0,3));
        return ServletUtil.webFluxResponseWriter(exchange.getResponse(),HttpStatus.valueOf(retCode), resultCode.getMsg(), resultCode.getCode());
    }

    /**
     * 获取缓存key
     *
     * @param token token信息
     * @return tokenkey信息
     */
    private String getTokenKey(String token) {
        return TokenConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 获取请求token
     * @param request 请求
     * @return token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(SecurityConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }

    /**
     * 执行顺序
     *
     * @return 顺序编号
     */
    @Override
    public int getOrder() {
        return -200;
    }
}