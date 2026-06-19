package com.seekisle.adminservice.house.service.filter;

import com.seekisle.adminapi.house.domain.dto.SearchHouseListReqDTO;
import com.seekisle.adminservice.house.domain.dto.HouseDTO;

/**
 * @author: yibo
 */
public interface IHouseFilter {

    /**
     * 过滤房源
     *
     * @param houseDTO
     * @param reqDTO
     * @return
     */
    Boolean filter(HouseDTO houseDTO, SearchHouseListReqDTO reqDTO);

}
