package com.bitejiuyeke.biteadminservice.map.service.impl;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentDTO;
import com.bitejiuyeke.biteadminapi.map.domain.dto.LocationReqDTO;
import com.bitejiuyeke.biteadminapi.map.domain.dto.PlaceSearchReqDTO;
import com.bitejiuyeke.biteadminservice.config.service.ISysArgumentService;
import com.bitejiuyeke.biteadminapi.map.constants.MapConstants;
import com.bitejiuyeke.biteadminservice.map.domain.dto.*;
import com.bitejiuyeke.biteadminservice.map.domain.entity.SysRegion;
import com.bitejiuyeke.biteadminservice.map.mapper.RegionMapper;
import com.bitejiuyeke.biteadminservice.map.service.IBiteMapService;
import com.bitejiuyeke.biteadminservice.map.service.IMapProvider;
import com.bitejiuyeke.bitecommondomain.constants.CacheConstants;
import com.bitejiuyeke.bitecommoncache.utils.CacheUtil;
import com.bitejiuyeke.bitecommoncore.domain.dto.BasePageDTO;
import com.bitejiuyeke.bitecommoncore.utils.PageUtil;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BiteMapServiceImpl implements IBiteMapService {

    @Autowired
    ISysArgumentService sysArgumentService;

    @Autowired
    IMapProvider mapProvider;

    @Autowired
    RegionMapper regionMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    Cache<String, Object> caffeineCache;

    /**
     * 地图是否开启使用
     */
    @Value("${map.enabled:false}")
    private boolean mapEnable;

    /**
     * 地图是否开启使用
     */
    @Value("${map.regionenabled:false}")
    private boolean mapRegionEnable;

    /**
     * Tips: 缓存预热 ，启动时初始化区划信息
     */
    @PostConstruct
    public void initCityMap() {
        if(!mapRegionEnable){
            return;
        }
        List<SysRegion> sysRegions = loadRegionFromDB();
        // 初始化缓存
        loadCityInfo(sysRegions);
        loadCityPinyinInfo(sysRegions);
    }


    /**
     * 从db查询区划信息
     *
     * @return 区划列表信息
     */
    public List<SysRegion> loadRegionFromDB() {
        return regionMapper.selectAllReigon();
    }


    /**
     * 读取城市编码对应城市信息
     *
     * @param sysRegions 区域列表信息
     * @return 城市编码对应城市信息
     */
    public Map<String, SysRegionDTO> loadCityCodeInfo(List<SysRegion> sysRegions) {
        if (sysRegions == null) {
            log.info("读取数据库城市列表缓存信息");
            // 查询db
            sysRegions = loadRegionFromDB();
        }

        // 城市code和详情
        Map<String, SysRegionDTO> cityMap = new LinkedHashMap<>();

        for (SysRegion sysRegion : sysRegions) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                cityMap.put(sysRegion.getCode(), sysRegionDTO);
            }
        }

        CacheUtil.setL2Cache(redisService, MapConstants.CACHE_MAP_CITY_CODE, cityMap, caffeineCache, 120L,TimeUnit.SECONDS);

        return cityMap;
    }


    /**
     * 读取城市id 对应城市信息
     *
     * @param sysRegions 区划信息
     * @return 城市id 对应城市信息
     */
    public Map<String, SysRegionDTO> loadCityInfo(List<SysRegion> sysRegions) {
        if (sysRegions == null) {
            log.info("读取数据库城市列表缓存信息");
            // 查询db
            sysRegions = loadRegionFromDB();
        }

        // 城市id和详情
        Map<String, SysRegionDTO> cityMap = new LinkedHashMap<>();

        for (SysRegion sysRegion : sysRegions) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                cityMap.put(String.valueOf(sysRegion.getId()), sysRegionDTO);
            }
        }

        CacheUtil.setL2Cache(redisService, MapConstants.CACHE_MAP_CITY_KEY, cityMap, caffeineCache, 120L,TimeUnit.SECONDS);

        return cityMap;
    }

    /**
     * 读取城市拼音信息
     *
     * @param sysRegions 区划信息
     * @return 城市首字母对应城市列表信息
     */
    public Map<String, List<SysRegionDTO>> loadCityPinyinInfo(List<SysRegion> sysRegions) {
        if (sysRegions == null) {
            // 查询db
            log.info("从数据库加载区划信息");
            sysRegions = loadRegionFromDB();
        }

        Map<String, List<SysRegionDTO>> regionPinYinMap = new TreeMap<>();

        for (SysRegion sysRegion : sysRegions) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                String firstChar = sysRegionDTO.getPinyin().toUpperCase().substring(0, 1);

                if (regionPinYinMap.containsKey(firstChar)) {
                    regionPinYinMap.get(firstChar).add(sysRegionDTO);
                } else {
                    List<SysRegionDTO> regionDTOS = new ArrayList<>();
                    regionDTOS.add(sysRegionDTO);
                    regionPinYinMap.put(firstChar, regionDTOS);
                }
            }
        }

        CacheUtil.setL2Cache(redisService, MapConstants.CACHE_MAP_CITY_PINYIN_KEY, regionPinYinMap, caffeineCache, 120L,TimeUnit.SECONDS);

        return regionPinYinMap;
    }

    /**
     * 获取城市拼音信息
     *
     * @return 城市首字母对应城市列表信息
     */
    @Override
    public Map<String, List<SysRegionDTO>> getCityPylist() {
        if(!mapRegionEnable){
            throw new ServiceException(ResultCode.MAP_REGION_NOT_ENABLED);
        }

        Map<String, List<SysRegionDTO>> res = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_CITY_PINYIN_KEY, new TypeReference<Map<String, List<SysRegionDTO>>>() {
        }, caffeineCache);

        if (res != null && !res.isEmpty()) {
            return res;
        }

        // 从db查询
        res = loadCityPinyinInfo(null);

        return res;
    }

    /**
     * 获取城市编码对应城市信息
     *
     * @return 城市编码对应城市信息
     */
    public Map<String, SysRegionDTO> getCityCodeMap() {
        Map<String, SysRegionDTO> cityCodeMap = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_CITY_CODE, new TypeReference<Map<String, SysRegionDTO>>() {
        }, caffeineCache);

        if (cityCodeMap != null && !cityCodeMap.isEmpty()) {
            return cityCodeMap;
        }

        cityCodeMap = loadCityCodeInfo(null);
        return cityCodeMap;
    }


    /**
     * 获取城市id 对应城市信息
     *
     * @return 城市id 对应城市信息
     */
    public Map<String, SysRegionDTO> getCityMap() {
        Map<String, SysRegionDTO> cityMap = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_CITY_KEY, new TypeReference<Map<String, SysRegionDTO>>() {
        }, caffeineCache);

        if (cityMap != null && !cityMap.isEmpty()) {
            return cityMap;
        }

        cityMap = loadCityInfo(null);
        return cityMap;
    }

    /**
     * V1获取城市id 对应城市信息
     *
     * @return 城市id 对应城市信息
     */
    public Map<String, SysRegionDTO> getCityMapV1() {
        Map<String, SysRegionDTO> cityMap = loadCityInfoV1(null);
        return cityMap;
    }

    /**
     * V1读取城市id 对应城市信息
     * @param sysRegions 区划信息
     * @return 城市id 对应城市信息
     */
    public Map<String, SysRegionDTO> loadCityInfoV1(List<SysRegion> sysRegions) {
        if (sysRegions == null) {
            log.info("读取数据库城市列表缓存信息");
            // 查询db
            sysRegions = loadRegionFromDB();
        }

        // 城市id和详情
        Map<String, SysRegionDTO> cityMap = new LinkedHashMap<>();

        for (SysRegion sysRegion : sysRegions) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                cityMap.put(String.valueOf(sysRegion.getId()), sysRegionDTO);
            }
        }

        return cityMap;
    }

    /**
     * V2 获取城市id 对应城市信息
     *
     * @return 城市id 对应城市信息
     */
    public Map<String, SysRegionDTO> getCityMapV2() {
        Map<String, SysRegionDTO> cityMap = redisService.getCacheObject( MapConstants.CACHE_MAP_CITY_KEY_V2, new TypeReference<Map<String, SysRegionDTO>>() {
        });

        if (cityMap != null && !cityMap.isEmpty()) {
            return cityMap;
        }

        cityMap = loadCityInfoV2(null);
        return cityMap;
    }

    /**
     * V2 读取城市id 对应城市信息
     * @param sysRegions 区划信息
     * @return 城市id 对应城市信息
     */
    public Map<String, SysRegionDTO> loadCityInfoV2(List<SysRegion> sysRegions) {
        if (sysRegions == null) {
            log.info("读取数据库城市列表缓存信息");
            // 查询db
            sysRegions = loadRegionFromDB();
        }

        // 城市id和详情
        Map<String, SysRegionDTO> cityMap = new LinkedHashMap<>();

        for (SysRegion sysRegion : sysRegions) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                cityMap.put(String.valueOf(sysRegion.getId()), sysRegionDTO);
            }
        }

        redisService.setCacheObject( MapConstants.CACHE_MAP_CITY_KEY_V2, cityMap);

        return cityMap;
    }

    /**
     * 获取城市列表
     *
     * @return 城市列表
     */
    @Override
    public List<SysRegionDTO> getCityList() {
        if(!mapRegionEnable){
            throw new ServiceException(ResultCode.MAP_REGION_NOT_ENABLED);
        }

        Map<String, SysRegionDTO> cityMap = getCityMap();
        return new ArrayList<>(cityMap.values());
    }


    /**
     * 获取城市列表 V1
     *
     * @return 城市列表
     */
    public List<SysRegionDTO> getCityListV1() {
        if(!mapRegionEnable){
            throw new ServiceException(ResultCode.MAP_REGION_NOT_ENABLED);
        }

        Map<String, SysRegionDTO> cityMap = getCityMapV1();
        return new ArrayList<>(cityMap.values());
    }

    /**
     * 获取城市列表 V2
     *
     * @return 城市列表
     */
    public List<SysRegionDTO> getCityListV2() {
        if(!mapRegionEnable){
            throw new ServiceException(ResultCode.MAP_REGION_NOT_ENABLED);
        }

        Map<String, SysRegionDTO> cityMap = getCityMapV2();
        return new ArrayList<>(cityMap.values());
    }


    /**
     * 获取子区划信息
     * @param parentId 父区划id
     * @return 子区划信息
     */
    @Override
    public List<SysRegionDTO> getRegionChildren(Long parentId) {
        if(!mapRegionEnable){
            throw new ServiceException(ResultCode.MAP_REGION_NOT_ENABLED);
        }

        // 先读缓存
        String key = MapConstants.CACHE_MAP_CITY_CHILDREND_KEY + CacheConstants.CACHE_SPLIT_COLON + String.valueOf(parentId);
        List<SysRegionDTO> result = CacheUtil.getL2Cache(redisService, key, new TypeReference<List<SysRegionDTO>>() {
        }, caffeineCache);

        if (result != null && !result.isEmpty()) {
            return result;
        }

        if (parentId == null) {
            throw new ServiceException(ResultCode.INVALID_PARA);
        }

        List<SysRegion> sysRegions = regionMapper.selectRegionChildren(parentId);

        if (CollectionUtils.isEmpty(sysRegions)) {
            throw new ServiceException(ResultCode.INVALID_REGION);
        }

        result = new ArrayList<>();
        for (SysRegion sysRegion : sysRegions) {
            SysRegionDTO sysRegionDTO = new SysRegionDTO();
            BeanUtils.copyProperties(sysRegion, sysRegionDTO);
            result.add(sysRegionDTO);
        }

        CacheUtil.setL2Cache(redisService, key, result, caffeineCache, 120L,TimeUnit.SECONDS);

        return result;
    }

    /**
     * 获取热门城市列表
     *
     * @return 热门城市列表
     */
    @Override
    public List<SysRegionDTO> getHotCityList() {
        if(!mapRegionEnable){
            throw new ServiceException(ResultCode.MAP_REGION_NOT_ENABLED);
        }

        ArgumentDTO argumentDTO = sysArgumentService.getByConfigKey(MapConstants.CFG_HOT_CITY_KEY);

        if (argumentDTO == null || StringUtils.isEmpty(argumentDTO.getValue())){
            throw new ServiceException(ResultCode.INVALID_PARA);
        }


        String[] cityIds =  argumentDTO.getValue().split(",");

        String key = MapConstants.CACHE_MAP_HOT_CITY+CacheConstants.CACHE_SPLIT_COLON+StringUtils.join(cityIds,"_");

        List<SysRegionDTO> result = CacheUtil.getL2Cache(redisService, key, new TypeReference<List<SysRegionDTO>>() {
        }, caffeineCache);

        if (result != null && !result.isEmpty()) {
            return result;
        }

        result = new ArrayList<>();
        Map<String, SysRegionDTO> cityMap = getCityMap();
        for (String cityId : cityIds) {
            if (StringUtils.isEmpty(cityId) || !StringUtils.isNumeric(cityId)) {
                throw new ServiceException(ResultCode.INVALID_PARA);
            }

            SysRegionDTO sysRegionDTO = cityMap.get(cityId);
            if (sysRegionDTO == null) {
                throw new ServiceException(ResultCode.INVALID_PARA);
            }

            result.add(sysRegionDTO);
        }

        CacheUtil.setL2Cache(redisService, key, result, caffeineCache, 120L,TimeUnit.SECONDS);
        return result;
    }


    /**
     * 地图搜索信息
     *
     * @param placeSearchDTO 搜索条件
     * @return 搜索结果
     */
    @Override
    public BasePageDTO<SearchPoiDTO> searchSuggestOnMap(PlaceSearchReqDTO placeSearchDTO){
        if(!mapEnable){
            throw  new ServiceException(ResultCode.MAP_NOT_ENABLED);
        }

        SuggestSearchDTO suggestSearchDTO = new SuggestSearchDTO();

        Map<String, SysRegionDTO> cityMap = getCityMap();
        if (!cityMap.containsKey(String.valueOf(placeSearchDTO.getId()))) {
            throw new ServiceException(ResultCode.INVALID_REGION);
        }

        suggestSearchDTO.setId(cityMap.get(String.valueOf(placeSearchDTO.getId())).getCode());
        suggestSearchDTO.setKeyword(placeSearchDTO.getKeyword());
        //默认仅查询首页，超过的随着输入的准确性越来越少
        if (placeSearchDTO.getPageNo() == null) {
            suggestSearchDTO.setPageIndex(1);
            suggestSearchDTO.setPageSize(MapConstants.QQMAP_DEFAULT_SIZE);
        } else {
            suggestSearchDTO.setPageIndex(placeSearchDTO.getPageNo());
            suggestSearchDTO.setPageSize(placeSearchDTO.getPageSize());
        }

        // 调用地图接口查询
        PoiListDTO poiListDTO = mapProvider.searchQQMapPlaceByRegion(suggestSearchDTO);

        List<PoiDTO> poilist = poiListDTO.getData();

        // 拼装响应结果
        BasePageDTO<SearchPoiDTO> result = new BasePageDTO<>();
        result.setTotals(poiListDTO.getCount());
        result.setTotalPages(PageUtil.getTotalPages(poiListDTO.getCount(), suggestSearchDTO.getPageSize()));

        List<SearchPoiDTO> pageRes = new ArrayList<>();
        for (PoiDTO poiDTO : poilist) {
            SearchPoiDTO searchPoiDTO = new SearchPoiDTO();
            searchPoiDTO.setAddress(poiDTO.getAddress());
            searchPoiDTO.setTitle(poiDTO.getTitle());
            searchPoiDTO.setLongitude(poiDTO.getLocation().getLng());
            searchPoiDTO.setLatitude(poiDTO.getLocation().getLat());
            pageRes.add(searchPoiDTO);
        }

        result.setList(pageRes);
        return result;
    }

    /**
     * 根据位置获取城市信息
     *
     * @param location 位置信息
     * @return 城市信息
     */
    @Override
    public RegionCityDTO getCityByLocation(LocationReqDTO location){

        if(!mapEnable){
            throw  new ServiceException(ResultCode.MAP_NOT_ENABLED);
        }

        RegionCityDTO result = new RegionCityDTO();
        GeoResultDTO addrResultDTO = mapProvider.getQQMapDistrictByLonLat(location);

        if (addrResultDTO == null || addrResultDTO.getResult() == null
                || addrResultDTO.getResult().getAd_info() == null ||
                addrResultDTO.getResult().getAd_info().getNation_code() == null
                || addrResultDTO.getResult().getAd_info().getCity_code() == null) {
            throw new ServiceException(ResultCode.QQMAP_LOCATE_FAILED);
        }

        String mapCountryCode = addrResultDTO.getResult().getAd_info().getNation_code();
        String mapCityCode = addrResultDTO.getResult().getAd_info().getCity_code();
        String cityCode = mapCityCode.substring(mapCountryCode.length());

        Map<String, SysRegionDTO> cityCodeMap = getCityCodeMap();
        if (cityCodeMap.containsKey(cityCode)) {
            result.setId(cityCodeMap.get(cityCode).getId());
            result.setName(cityCodeMap.get(cityCode).getName());
            result.setFullName(cityCodeMap.get(cityCode).getFullName());
        } else {
            throw new ServiceException(ResultCode.QQMAP_CITY_UNKNOW);
        }
        return result;
    }

}
