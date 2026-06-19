package com.seekisle.gateway.handler;

import com.seekisle.commoncore.utils.ServletUtil;
import com.seekisle.commondomain.domain.ResultCode;
import com.seekisle.commondomain.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * 网关统一异常处理
 */
@Order(-1)
@Configuration
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    /**
     * 处理器
     *
     * @param exchange ServerWebExchange
     * @param ex 异常信息
     * @return 无
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        int retCode = ResultCode.ERROR.getCode();

        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        String retMsg;
        if (ex instanceof NotFoundException) {
            retCode = ResultCode.SERVICE_NOT_FOUND.getCode();
            retMsg = ResultCode.SERVICE_NOT_FOUND.getMsg();
        } else if (ex instanceof ResponseStatusException) {
            retMsg = ResultCode.ERROR.getMsg();
        } else if (ex instanceof ServiceException) {
            retMsg = ex.getMessage();
            retCode = ((ServiceException) ex).getCode();
        } else {
            retMsg = ResultCode.ERROR.getMsg();
        }

        int httpCode = Integer.parseInt(String.valueOf(retCode).substring(0,3));

        log.error("[网关异常处理]请求路径:{},异常信息:{}", exchange.getRequest().
                getPath(), ex.getMessage());

        return ServletUtil.webFluxResponseWriter(response, HttpStatus.valueOf(httpCode),retMsg, retCode);
    }
}