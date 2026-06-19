package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

/**
 * 地址行政区划信息
 */
@Data
public class AdInfoDTO {

    /**
     * 国家代码（ISO3166标准3位数字码）
     */
    private String nation_code;

    /**
     * 行政区划代码
     */
    private String adcode;

    /**
     * 城市代码
     */
    private String city_code;


    /**
     * 行政区划名称
     */
    private String name;

    /**
     * 	国家
     */
    private String nation;

    /**
     * 省 / 直辖市
     */
    private String province;

    /**
     * 市 / 地级区 及同级行政区划，如果当前城市为省直辖县级区划，city与district字段均会返回此城市
     */
    private String city;

    /**
     * 区 / 县级市 及同级行政区划
     */
    private String district;

}
