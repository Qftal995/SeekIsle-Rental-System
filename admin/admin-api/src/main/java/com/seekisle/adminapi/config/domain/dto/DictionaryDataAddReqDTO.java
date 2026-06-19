package com.seekisle.adminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 添加字典数据DTO
 */
@Data
public class DictionaryDataAddReqDTO {

    /**
     * 字典类型键
     */
    @NotBlank(message = "字典类型键不能为空")
    private String typeKey;

    /**
     * 字典数据键
     */
    @NotBlank(message = "字典数据键不能为空")
    private String dataKey;

    /**
     * 字典数据值
     */
    @NotBlank(message = "字典数据不能为空")
    private String value;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;
}
