package com.seekisle.adminservice.map.domain.dto;

import lombok.Data;

/**
 * 区域城市信息DTO
 */
@Data
public class RegionCityDTO {
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
