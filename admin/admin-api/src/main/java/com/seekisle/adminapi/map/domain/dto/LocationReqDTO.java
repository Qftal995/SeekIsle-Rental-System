package com.seekisle.adminapi.map.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 位置查询
 */
@Data
public class LocationReqDTO {

    /**
     * 纬度
     */
    @NotNull(message = "经纬度必须输入")
    private Double lat;

    /**
     * 经度
     */
    @NotNull(message = "经纬度必须输入")
    private Double lng;

    /**
     * 格式化信息
     *
     * @return 格式化后的经纬度
     */
    public String formatInfo() {
        return lat +"," + lng;
    }
}
