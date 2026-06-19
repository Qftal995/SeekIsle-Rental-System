package com.seekisle.adminapi.map.domain.dto;

import com.seekisle.commondomain.domain.dto.BasePageReqDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 地图模糊搜素相关位置
 */
@Data
public class PlaceSearchReqDTO extends BasePageReqDTO {

    /**
     * 搜索关键字
     */
    @NotBlank(message = "搜索内容不能为空")
    private String keyword;

    /**
     * 城市ID
     */
    @NotNull(message = "城市不能为空")
    private Long id;
}
