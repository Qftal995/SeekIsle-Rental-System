package com.seekisle.adminservice.map.domain.dto;

import lombok.Data;

/**
 * QQ地图响应基类
 */
@Data
public class QQMapBaseResponseDTO {
    /**
     * 响应码 0表示成功
     */
    private int status;

    /**
     * 响应消息
     */
    private String message ;

    /**
     * 请求id
     */
    private String request_id;

}
