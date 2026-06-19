package com.seekisle.adminservice.house.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seekisle.adminservice.house.domain.dto.HouseDescDTO;
import com.seekisle.adminservice.house.domain.dto.HouseListReqDTO;
import com.seekisle.adminservice.house.domain.entity.House;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: yibo
 */
@Mapper
public interface HouseMapper extends BaseMapper<House> {

    Long selectCountWithStatus(HouseListReqDTO houseListReqDTO);

    List<HouseDescDTO> selectPageWithStatus(HouseListReqDTO houseListReqDTO);
}
