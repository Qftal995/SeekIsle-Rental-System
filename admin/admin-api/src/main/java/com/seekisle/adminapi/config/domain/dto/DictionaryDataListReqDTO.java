package com.seekisle.adminapi.config.domain.dto;

import com.seekisle.commondomain.domain.dto.BasePageReqDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字典数据列表DTO
 */
@Data
public class DictionaryDataListReqDTO extends BasePageReqDTO {

    /**
     * 字典类型键
     */
    @NotBlank(message = "字典类型键名不能为空")
    private String typeKey;

    /**
     * 字典数据值
     */
    private String value;
}
