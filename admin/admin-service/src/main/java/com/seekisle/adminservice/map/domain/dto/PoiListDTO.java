package com.seekisle.adminservice.map.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 地图POI列表
 */
@Data
public class PoiListDTO extends QQMapBaseResponseDTO {

    /**
     * 本次搜索结果总数
     */
    private Integer count;

    /**
     * 提示词数组，每项为一个POI对象
     */
    private List<PoiDTO> data;
}
