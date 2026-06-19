package com.seekisle.adminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 编辑字典数据DTO
 */
@Data
public class DictionaryDataEditReqDTO {

    /**
     * 字典数据键
     */
    @NotBlank(message = "字典数据键名不能为空")
    private String dataKey;

    /**
     * 字典数据值
     */
    @NotBlank(message = "字典数据值不能为空")
    private String value;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序值
     */
    private Integer sort;
}
