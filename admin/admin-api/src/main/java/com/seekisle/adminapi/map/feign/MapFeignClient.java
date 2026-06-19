package com.seekisle.adminapi.map.feign;

import com.seekisle.adminapi.map.domain.dto.LocationReqDTO;
import com.seekisle.adminapi.map.domain.dto.PlaceSearchReqDTO;
import com.seekisle.adminapi.map.domain.vo.RegionCityVO;
import com.seekisle.adminapi.map.domain.vo.RegionVO;
import com.seekisle.adminapi.map.domain.vo.SearchPoiVO;
import com.seekisle.commondomain.domain.R;
import com.seekisle.commondomain.domain.vo.BasePageVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 地图相关接口
 */
@FeignClient(contextId = "mapFeignClient", value = "admin" )
public interface MapFeignClient {

    /**
     * 城市拼音归类查询
     */
    @GetMapping("/map/city_pinyin_list")
    R<Map<String, List<RegionVO>>> getCityPylist();

    /**
     * 获取热门城市
     *
     * @return 热门城市列表
     */
    @GetMapping("/map/city_hot_list")
    R<List<RegionVO>> getHotCityList();

    /**
     * 城市列表查询
     *
     * @return 城市列表信息
     */
    @GetMapping("/map/city_list")
    R<List<RegionVO>> getCityList();

    /**
     * 下级区划信息查询
     *
     * @param parentId 父区划id
     * @return 子区划列表信息
     */
    @GetMapping("/map/region_children_list")
    R<List<RegionVO>> regionChildren(@RequestParam Long parentId);

    /**
     * 地图搜索
     *
     * @param placeSearchDTO 搜索条件
     * @return 搜索结果
     */
    @PostMapping("/map/search")
    R<BasePageVO<SearchPoiVO>> searchSuggestOnMap(@RequestBody PlaceSearchReqDTO placeSearchDTO);

    /**
     * 根据经纬度获取当前所在的城市
     *
     * @param location 经纬度信息
     * @return 经纬度对应的城市信息
     */
    @PostMapping("/map/locate_city_by_location")
    R<RegionCityVO> locateCityByLocation(@RequestBody LocationReqDTO location);
}
