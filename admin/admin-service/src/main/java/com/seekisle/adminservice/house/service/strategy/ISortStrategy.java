package com.seekisle.adminservice.house.service.strategy;

import com.seekisle.adminapi.house.domain.dto.SearchHouseListReqDTO;
import com.seekisle.adminservice.house.domain.dto.HouseDTO;

import java.util.List;

/**
 * @author: yibo
 */
public interface ISortStrategy {

    /**
     * 排序
     */
    List<HouseDTO> sort(List<HouseDTO> houseDTOList, SearchHouseListReqDTO reqDTO);

}
