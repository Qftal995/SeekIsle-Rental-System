package com.seekisle.adminservice.map.domain.dto;

import lombok.Data;

/**
 * POI信息
 */
@Data
public class PoiDTO {

    /**
     * POI（地点）唯一标识
     */
    private String id;

    /**
     * POI（地点）名称
     */
    private String title;

    /**
     * 地址
     */
    private String address;

    /**
     * POI类型，值说明：0:普通POI / 1:公交车站 / 2:地铁站 / 3:公交线路 / 4:行政区划
     */
    private String type;

    /**
     * 提示所述位置坐标
     */
    private LocationDTO location;

}
