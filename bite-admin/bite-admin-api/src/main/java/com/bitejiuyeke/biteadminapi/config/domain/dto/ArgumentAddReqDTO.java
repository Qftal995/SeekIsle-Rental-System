package com.bitejiuyeke.biteadminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 添加参数请求DTO
 */
@Data
public class ArgumentAddReqDTO {

    /**
     * 参数名
     */
    @NotBlank(message = "参数名不能为空")
    private String name;

    /**
     * 参数键
     */
    @NotBlank(message = "参数键不能为空")
    private String configKey;

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
