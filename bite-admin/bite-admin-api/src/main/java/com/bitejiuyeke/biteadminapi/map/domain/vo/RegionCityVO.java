package com.bitejiuyeke.biteadminapi.map.domain.vo;

import lombok.Data;

/**
 * 区域城市VO
 */
@Data
public class RegionCityVO {
    /**
     * 城市id
     */
    private Long id;

    /**
     * 城市名称
     */
    private String name;

    /**
     * 区域全称
     */
    private String fullName;
}
