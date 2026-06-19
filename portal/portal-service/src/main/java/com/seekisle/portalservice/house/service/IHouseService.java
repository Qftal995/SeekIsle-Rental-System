package com.seekisle.portalservice.house.service;

import com.seekisle.portalservice.house.domain.vo.HouseDataVO;

/**
 * @author: yibo
 */
public interface IHouseService {

    /**
     * 查询房源详细信息
     *
     * @param houseId
     * @return
     */
    HouseDataVO houseDetail(Long houseId);
}
