package com.seekisle.adminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 编辑参数DTO
 */
@Data
public class ArgumentEditReqDTO {

    /**
     * 参数键
     */
    @NotBlank(message = "参数键不能为空")
    private String configKey;

    /**
     * 参数名
     */
    @NotBlank(message = "参数名不能为空")
    private String name;

    /**
     * 参数值
     */
    @NotBlank(message = "参数值不能为空")
    private String value;

    /**
     * 备注
     */
    private String remark;
}
