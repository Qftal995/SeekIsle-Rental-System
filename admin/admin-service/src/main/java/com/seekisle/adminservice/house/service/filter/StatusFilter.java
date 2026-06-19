package com.seekisle.adminservice.house.service.filter;

import com.seekisle.adminapi.house.domain.dto.SearchHouseListReqDTO;
import com.seekisle.adminservice.house.domain.dto.HouseDTO;
import com.seekisle.adminservice.house.domain.enums.HouseStatusEnum;
import org.springframework.stereotype.Component;

/**
 * @author: yibo
 */
@Component
public class StatusFilter implements IHouseFilter{
    @Override
    public Boolean filter(HouseDTO houseDTO, SearchHouseListReqDTO reqDTO) {
        return houseDTO.getStatus().equalsIgnoreCase(HouseStatusEnum.UP.name());
    }
}
