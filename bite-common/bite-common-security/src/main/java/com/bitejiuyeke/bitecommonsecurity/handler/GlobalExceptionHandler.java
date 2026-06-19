package com.bitejiuyeke.bitecommonsecurity.handler;

import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 设置响应码
     *
     * @param response 响应信息
     * @param errcode 响应码
     */
    private void setResponseCode(HttpServletResponse response,Integer errcode){
        int httpCode = Integer.parseInt(String.valueOf(errcode).substring(0,3));
        response.setStatus(httpCode);
    }

    /**
     * 请求方式不支持
     * @param e 异常信息
     * @param request 请求
     * @param response 响应
     * @return 异常结果
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        setResponseCode(response, ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getCode());
        return R.fail(ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getCode(), ResultCode.REQUEST_METNHOD_NOT_SUPPORTED.getMsg());
    }

    /**
     * 业务异常
     *
     * @param e 异常信息
     * @param request 请求
     * @param response 响应
     * @return 业务异常结果
     */
    @ExceptionHandler(ServiceException.class)
    public R<?> handleServiceException(ServiceException e, HttpServletRequest request,
                                       HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生业务异常", requestURI,  e);
        setResponseCode(response,e.getCode());
        return R.fail(e.getCode(),e.getMessage());
    }

    /**
     * 绑定异常
     *
     * @param e 异常信息
     * @param response 响应信息
     * @return 异常结果
     */
    @ExceptionHandler({BindException.class})
    public R<?> handleBindException(BindException e,
                                       HttpServletResponse response) {
        log.error("绑定异常",e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        setResponseCode(response, ResultCode.INVALID_PARA.getCode());
        return R.fail(ResultCode.INVALID_PARA.getCode(),message);
    }


    /**
     * 类型不匹配异常
     *
     * @param e 异常信息
     * @param response 响应
     * @return 不匹配结果
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public R<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                             HttpServletResponse response) {
        log.error("类型不匹配异常",e);
        setResponseCode(response, ResultCode.PARA_TYPE_MISMATCH.getCode());
        return R.fail(ResultCode.PARA_TYPE_MISMATCH.getCode(), ResultCode.PARA_TYPE_MISMATCH.getMsg());
    }

    /**
     * 验证异常
     * @param e 异常信息
     * @param response 响应
     * @return 异常报文
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                       HttpServletResponse response) {
        log.error("验证异常",e);
        setResponseCode(response, ResultCode.INVALID_PARA.getCode());
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return R.fail(ResultCode.INVALID_PARA.getCode(),message);
    }


    /**
     * 拦截运行时异常
     *
     * @param e 异常信息
     * @param request 请求信息
     * @param response 响应信息
     * @return 响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public R<?> handleRuntimeException(RuntimeException e, HttpServletRequest request,
                                       HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生运行时异常.", requestURI, e);
        setResponseCode(response, ResultCode.ERROR.getCode());
        return R.fail(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMsg());
    }

    /**
     * 系统异常
     * @param e 异常信息
     * @param request 请求
     * @param response 响应
     * @return 响应结果
     */
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e, HttpServletRequest request,
                                HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生异常.", requestURI, e);
        setResponseCode(response, ResultCode.ERROR.getCode());
        return R.fail(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMsg());
    }
}
