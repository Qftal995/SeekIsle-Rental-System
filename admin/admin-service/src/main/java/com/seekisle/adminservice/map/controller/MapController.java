package com.seekisle.adminservice.map.controller;

import com.seekisle.adminapi.map.domain.dto.LocationReqDTO;
import com.seekisle.adminapi.map.domain.dto.PlaceSearchReqDTO;
import com.seekisle.adminapi.map.domain.vo.RegionCityVO;
import com.seekisle.adminapi.map.domain.vo.RegionVO;
import com.seekisle.adminapi.map.domain.vo.SearchPoiVO;
import com.seekisle.adminapi.map.feign.MapFeignClient;
import com.seekisle.adminservice.map.domain.dto.*;
import com.seekisle.adminservice.map.service.IBiteMapService;
import com.seekisle.commoncore.domain.dto.BasePageDTO;
import com.seekisle.commoncore.utils.BeanCopyUtil;
import com.seekisle.commondomain.domain.R;
import com.seekisle.commondomain.domain.vo.BasePageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 地图相关处理
 */
@RestController
@Slf4j
public class MapController implements MapFeignClient {

    /**
     * 地图服务
     */
    @Autowired
    IBiteMapService mapService;

    /**
     * 获取热门城市列表
     *
     * @return 热门城市列表
     */
    @Override
    public R<List<RegionVO>> getHotCityList() {
        List<SysRegionDTO> pinyinList = mapService.getHotCityList();
        return R.ok(BeanCopyUtil.copyListProperties(pinyinList, RegionVO::new));
    }

    /**
     * 城市拼音归类查询
     */
    @Override
    public R<Map<String, List<RegionVO>>> getCityPylist() {
        Map<String, List<SysRegionDTO>> pinyinList = mapService.getCityPylist();
        Map<String, List<RegionVO>> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<SysRegionDTO>> alphaRegion : pinyinList.entrySet()) {
            result.put(alphaRegion.getKey(), BeanCopyUtil.copyListProperties(alphaRegion.getValue(), RegionVO::new));
        }
        return R.ok(result);
    }

    /**
     * 城市列表查询
     *
     * @return 城市列表信息
     */
    @Override
    public R<List<RegionVO>> getCityList() {
        List<SysRegionDTO> regionDTOS = mapService.getCityList();
        List<RegionVO> result = BeanCopyUtil.copyListProperties(regionDTOS, RegionVO::new);
        return R.ok(result);
    }

    /**
     * 下级区划信息查询
     *
     * @param parentId 父区划id
     * @return 子区划列表信息
     */
    @Override
    public R<List<RegionVO>> regionChildren(Long parentId) {
        List<SysRegionDTO> regionDTOS = mapService.getRegionChildren(parentId);
        List<RegionVO> result = BeanCopyUtil.copyListProperties(regionDTOS, RegionVO::new);
        return R.ok(result);
    }

    /**
     * 地图搜索
     *
     * @param placeSearchDTO 搜索条件
     * @return 搜索结果
     */
    @Override
    public R<BasePageVO<SearchPoiVO>> searchSuggestOnMap(@Validated PlaceSearchReqDTO placeSearchDTO) {
        BasePageDTO<SearchPoiDTO> basePageListDTO = mapService.searchSuggestOnMap(placeSearchDTO);

        BasePageVO<SearchPoiVO>  result = new BasePageVO<>();
        BeanUtils.copyProperties(basePageListDTO,result);
        return R.ok(result);
    }

    /**
     * 根据经纬度获取当前所在的城市
     *
     * @param location 经纬度信息
     * @return 经纬度对应的城市信息
     */
    @Override
    public R<RegionCityVO> locateCityByLocation(@Validated LocationReqDTO location) {
        RegionCityDTO regionCityDTO = mapService.getCityByLocation(location);
        RegionCityVO  result = new RegionCityVO();
        BeanUtils.copyProperties(regionCityDTO,result);
        return R.ok(result);
    }

}
