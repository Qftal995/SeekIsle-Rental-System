package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

/**
 * 逆地址解析结果
 */
@Data
public class AddrResultDTO extends QQMapBaseResponseDTO {

    /**
     * 以行政区划+道路+门牌号等信息组成的标准格式化地址
     */
    private String address;

    /**
     * 城市地址详细信息
     */
    private AdInfoDTO ad_info;
}
