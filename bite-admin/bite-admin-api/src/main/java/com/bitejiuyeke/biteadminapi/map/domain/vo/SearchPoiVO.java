package com.bitejiuyeke.biteadminapi.map.domain.vo;

import lombok.Data;

/**
 * 搜索POI结果信息
 */
@Data
public class SearchPoiVO {

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
