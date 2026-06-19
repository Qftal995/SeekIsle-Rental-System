package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

/**
 * 搜索结果POI
 */
@Data
public class SearchPoiDTO {

    /**
     * POI（地点）名称
     */
    private String title;

    /**
     * 地址
     */
    private String address;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

}
