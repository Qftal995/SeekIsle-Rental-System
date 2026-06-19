package com.seekisle.commondomain.exception;


import com.seekisle.commondomain.domain.ResultCode;

/**
 * 服务异常
 */
public class ServiceException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 响应构造异常
     * @param resultCode 响应信息
     */
    public ServiceException(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMsg();
    }

    /**
     * 消息构造异常
     * @param message 异常消息
     */
    public ServiceException(String message) {
        this.message = message;
        this.code = ResultCode.ERROR.getCode();;
    }

    /**
     * 消息和响应码定制异常
     * @param message 消息
     * @param code 响应码
     */
    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    /**
     * 获取编码
     *
     * @return 响应码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置响应编码
     * @param code 响应编码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     *获取异常消息
     * @return 异常消息
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 设置异常消息
     * @param message 异常消息
     */
    public void setMessage(String message) {
        this.message = message;
    }
}