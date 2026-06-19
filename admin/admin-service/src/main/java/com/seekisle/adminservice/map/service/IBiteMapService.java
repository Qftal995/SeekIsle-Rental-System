package com.seekisle.adminservice.map.service;

import com.seekisle.adminapi.map.domain.dto.LocationReqDTO;
import com.seekisle.adminapi.map.domain.dto.PlaceSearchReqDTO;
import com.seekisle.adminservice.map.domain.dto.*;
import com.seekisle.commoncore.domain.dto.BasePageDTO;

import java.util.List;
import java.util.Map;

/**
 * 比特地图服务接口层
 */
public interface IBiteMapService {

    /**
     * 获取热门城市
     */
    List<SysRegionDTO> getHotCityList();

    /**
     * 获取城市拼音信息
     *
     * @return 城市首字母对应城市列表信息
     */
    Map<String, List<SysRegionDTO>> getCityPylist();

    /**
     * 获取城市列表
     *
     * @return 城市列表
     */
    List<SysRegionDTO> getCityList();

    /**
     * 获取子区划信息
     * @param parentId 父区划id
     * @return 子区划信息
     */
    List<SysRegionDTO> getRegionChildren(Long parentId);

    /**
     * 地图搜索信息
     *
     * @param placeSearchDTO 搜索条件
     * @return 搜索结果
     */
    BasePageDTO<SearchPoiDTO> searchSuggestOnMap(PlaceSearchReqDTO placeSearchDTO);

    /**
     * 根据位置获取城市信息
     *
     * @param location 位置信息
     * @return 城市信息
     */
    RegionCityDTO getCityByLocation(LocationReqDTO location);
}
