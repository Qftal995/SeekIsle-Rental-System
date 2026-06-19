package com.seekisle.adminservice.house.domain.entity;

import com.seekisle.commoncore.domain.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: yibo
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class CityHouse extends BaseDO {
    private Long cityId;
    private String cityName;
    private Long houseId;
}