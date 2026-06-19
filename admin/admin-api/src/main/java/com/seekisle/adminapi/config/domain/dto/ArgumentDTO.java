package com.seekisle.adminapi.config.domain.dto;

import lombok.Data;

/**
 * 参数DTO
 */
@Data
public class ArgumentDTO {

    /**
     *  参数ID
     */
    private Long id;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数键
     */
    private String configKey;

    /**
     * 参数值
     */
    private String value;

    /**
     * 参数备注
     */
    private String remark;
}
