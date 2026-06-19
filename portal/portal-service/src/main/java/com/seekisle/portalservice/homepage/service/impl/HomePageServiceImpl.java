package com.seekisle.portalservice.homepage.service.impl;

import com.seekisle.adminapi.house.domain.dto.SearchHouseListReqDTO;
import com.seekisle.adminapi.house.domain.vo.HouseDetailVO;
import com.seekisle.adminapi.house.feign.HouseFeignClient;
import com.seekisle.adminapi.map.domain.dto.LocationReqDTO;
import com.seekisle.adminapi.map.domain.vo.RegionCityVO;
import com.seekisle.adminapi.map.feign.MapFeignClient;
import com.seekisle.commoncore.utils.BeanCopyUtil;
import com.seekisle.commoncore.utils.JsonUtil;
import com.seekisle.commondomain.domain.R;
import com.seekisle.commondomain.domain.ResultCode;
import com.seekisle.commondomain.domain.vo.BasePageVO;
import com.seekisle.commondomain.exception.ServiceException;
import com.seekisle.portalservice.homepage.domain.dto.CityDescDTO;
import com.seekisle.portalservice.homepage.domain.dto.DictDataDTO;
import com.seekisle.portalservice.homepage.domain.dto.HouseListReqDTO;
import com.seekisle.portalservice.homepage.domain.dto.PullDataListReqDTO;
import com.seekisle.portalservice.homepage.domain.vo.CityDescVO;
import com.seekisle.portalservice.homepage.domain.vo.DictsVO;
import com.seekisle.portalservice.homepage.domain.vo.HouseDescVO;
import com.seekisle.portalservice.homepage.domain.vo.PullDataListVO;
import com.seekisle.portalservice.homepage.service.IDictionaryService;
import com.seekisle.portalservice.homepage.service.IHomePageService;
import com.seekisle.portalservice.homepage.service.IRegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: yibo
 */
@Component
@Slf4j
public class HomePageServiceImpl implements IHomePageService {

    @Autowired
    private MapFeignClient mapFeignClient;
    @Autowired
    private IRegionService regionService;
    @Autowired
    private IDictionaryService dictionaryService;
    @Autowired
    private HouseFeignClient houseFeignClient;


    @Override
    public CityDescVO getCityDesc(Double lat, Double lng) {
        // 校验参数
        if(null == lat || null == lng) {
            throw new ServiceException("城市经纬度不能为空！", ResultCode.INVALID_PARA.getCode());
        }

        // 发起远程调用：map服务
        LocationReqDTO locationReqDTO = new LocationReqDTO();
        locationReqDTO.setLat(lat);
        locationReqDTO.setLng(lng);
        R<RegionCityVO> result = mapFeignClient.locateCityByLocation(locationReqDTO);
        if (null == result
                || result.getCode() != ResultCode.SUCCESS.getCode()
                || null == result.getData()) {
            throw new ServiceException("根据定位获取城市信息失败！");
        }

        // 构造返回
        CityDescVO cityDescVO = new CityDescVO();
        BeanUtils.copyProperties(result.getData(), cityDescVO);
        return cityDescVO;
    }

    @Override
    public PullDataListVO getPullData(PullDataListReqDTO pullDataListReqDTO) {
        PullDataListVO result = new PullDataListVO();

        // 查询城市下区域列表
        List<CityDescDTO> cityDescDTOList =
                regionService.regionChildren(pullDataListReqDTO.getCityId());
        result.setRegionList(
                BeanCopyUtil.copyListProperties(cityDescDTOList, CityDescVO::new));


        // 查询字典数据列表
        Map<String, List<DictDataDTO>> dictDataMap =
                dictionaryService.batchFindDictionaryDataByTypes(pullDataListReqDTO.getDirtTypes());
        Map<String, List<DictsVO>> dictMap = new HashMap<>();
        for (Map.Entry<String, List<DictDataDTO>> entry : dictDataMap.entrySet()) {
            List<DictsVO> list = entry.getValue().stream()
                    .map(dictDataDTO -> {
                        DictsVO dictsVO = new DictsVO();
                        dictsVO.setId(dictDataDTO.getId());
                        dictsVO.setKey(dictDataDTO.getDataKey());
                        dictsVO.setName(dictDataDTO.getValue());
                        return dictsVO;
                    }).collect(Collectors.toList());
            dictMap.put(entry.getKey(), list);
        }
        result.setDictMap(dictMap);

        // 构造返回
        return result;

    }

    @Override
    public BasePageVO<HouseDescVO> houseList(HouseListReqDTO reqDTO) {

        // feign 接口查询房源列表
        SearchHouseListReqDTO searchHouseListReqDTO = new SearchHouseListReqDTO();
        BeanUtils.copyProperties(reqDTO, searchHouseListReqDTO);
        R<BasePageVO<HouseDetailVO>> r = houseFeignClient.searchList(searchHouseListReqDTO);
        if (null == r
                || r.getCode() != ResultCode.SUCCESS.getCode()
                || null == r.getData()) {
            log.error("查询房源列表失败！req:{}", JsonUtil.obj2String(searchHouseListReqDTO));
            throw new ServiceException("查询房源列表失败！");
        }

        // 构造返回
        BasePageVO<HouseDescVO> result = new BasePageVO<>();
        result.setTotals(r.getData().getTotals());
        result.setTotalPages(r.getData().getTotalPages());
        result.setList(convertHouseList(r.getData().getList()));
        return result;
    }

    private List<HouseDescVO> convertHouseList(List<HouseDetailVO> houseDetailVOList) {
        if (CollectionUtils.isEmpty(houseDetailVOList)) {
            return Arrays.asList();
        }

        // 查字典：rentType，position:west（datakey）
        List<String> dataKeys = houseDetailVOList.stream()
                .flatMap(houseDetailVO -> Stream.of(houseDetailVO.getRentType(), houseDetailVO.getPosition()))
                .distinct()
                .collect(Collectors.toList());
        Map<String, DictDataDTO> dictDataDTOMap = dictionaryService.batchFindDictionaryData(dataKeys);

        return houseDetailVOList.stream()
                .map(houseDetailVO -> {
                    HouseDescVO houseDescVO = new HouseDescVO();
                    houseDescVO.setHouseId(houseDetailVO.getHouseId());
                    houseDescVO.setHeadImage(houseDetailVO.getHeadImage());
                    houseDescVO.setTitle(houseDetailVO.getTitle());
                    houseDescVO.setArea(houseDetailVO.getArea());
                    houseDescVO.setRegionName(houseDetailVO.getRegionName());
                    houseDescVO.setPrice(houseDetailVO.getPrice());

                    DictDataDTO rentTypeData = dictDataDTOMap.get(houseDetailVO.getRentType());
                    houseDescVO.setRentType(rentTypeData.getValue());
                    DictDataDTO positionData = dictDataDTOMap.get(houseDetailVO.getPosition());
                    houseDescVO.setPosition(positionData.getValue());
                    return houseDescVO;
                }).collect(Collectors.toList());
    }
}
