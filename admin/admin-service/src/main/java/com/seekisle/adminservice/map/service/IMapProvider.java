package com.seekisle.adminservice.map.service;

import com.seekisle.adminservice.map.domain.dto.GeoResultDTO;
import com.seekisle.adminapi.map.domain.dto.LocationReqDTO;
import com.seekisle.adminservice.map.domain.dto.PoiListDTO;
import com.seekisle.adminservice.map.domain.dto.SuggestSearchDTO;

public interface IMapProvider {

    /**
     * 地图搜索信息
     * @param suggestSearchDTO 搜索条件
     * @return 搜索结果
     */
    PoiListDTO searchQQMapPlaceByRegion(SuggestSearchDTO suggestSearchDTO);


    /**
     * 根据经纬度获取区划信息
     *
     * @param location 位置信息
     * @return 区划信息
     */
    GeoResultDTO getQQMapDistrictByLonLat(LocationReqDTO location);
}
