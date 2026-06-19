package com.seekisle.adminservice.map.service.impl;

import com.seekisle.adminapi.map.constants.MapConstants;
import com.seekisle.adminservice.map.domain.dto.GeoResultDTO;
import com.seekisle.adminapi.map.domain.dto.LocationReqDTO;
import com.seekisle.adminservice.map.domain.dto.PoiListDTO;
import com.seekisle.adminservice.map.domain.dto.SuggestSearchDTO;
import com.seekisle.adminservice.map.service.IMapProvider;
import com.seekisle.commondomain.domain.ResultCode;
import com.seekisle.commondomain.exception.ServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯地图服务
 */
@Component
@RefreshScope
@Data
@Slf4j
@ConditionalOnProperty(value = "map.type", havingValue = "qqmap")
public class QQMapServiceImpl implements IMapProvider {

    /**
     * http客户端
     */
    @Autowired
    RestTemplate restTemplate;

    /**
     * API KEY
     */
    @Value("${qqmap.key}")
    private String key;

    /**
     * API Server地址
     */
    @Value("${qqmap.apiServer}")
    private String apiServer;


    /**
     * 地图搜索信息
     * @param suggestSearchDTO 搜索条件
     * @return 搜索结果
     */
    @Override
    public PoiListDTO searchQQMapPlaceByRegion(SuggestSearchDTO suggestSearchDTO){
        String url = apiServer+ MapConstants.QQMAP_API_PLACE_SUGGESTION+"?key={key}&region={id}" +
                "&region_fix={region_fix}&page_index={page_index}&page_size={page_size}&keyword={keyword}";
        Map<String,Object> params = new HashMap<>(1);
        params.put("key",key);
        params.put("id", suggestSearchDTO.getId());
        //限定为本城市的数据
        params.put("region_fix",1);
        params.put("page_index",suggestSearchDTO.getPageIndex());
        params.put("page_size",suggestSearchDTO.getPageSize());
        params.put("keyword", URLEncoder.encode(suggestSearchDTO.getKeyword(), StandardCharsets.UTF_8));

        ResponseEntity<PoiListDTO> response =  restTemplate.getForEntity(url,PoiListDTO.class,params);
        if (!response.getStatusCode().is2xxSuccessful()){
            log.error("获取行政规划异常",response);
            throw new ServiceException(ResultCode.QQMAP_QUERY_FAILED);
        }

        return response.getBody();
    }


    /**
     * 根据经纬度获取区划信息
     *
     * @param location 位置信息
     * @return 区划信息
     */
    @Override
    public GeoResultDTO getQQMapDistrictByLonLat(LocationReqDTO location){
        String url = apiServer+MapConstants.QQMAP_GEOCODER+"?key={key}&location="+location.formatInfo();
        Map<String,String> params = new HashMap<>(1);
        params.put("key",key);

        ResponseEntity<GeoResultDTO> response =  restTemplate.getForEntity(url,GeoResultDTO.class,params);
        if (!response.getStatusCode().is2xxSuccessful()){
            log.error("获取行政规划异常",response);
            return null;
        }

        return response.getBody();
    }
}
