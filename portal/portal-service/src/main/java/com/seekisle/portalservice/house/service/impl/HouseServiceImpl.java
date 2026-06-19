package com.seekisle.portalservice.house.service.impl;

import com.seekisle.adminapi.house.domain.vo.HouseDetailVO;
import com.seekisle.adminapi.house.feign.HouseFeignClient;
import com.seekisle.commondomain.domain.R;
import com.seekisle.commondomain.domain.ResultCode;
import com.seekisle.portalservice.house.domain.vo.HouseDataVO;
import com.seekisle.portalservice.house.service.IHouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: yibo
 */
@Component
@Slf4j
public class HouseServiceImpl implements IHouseService {

    @Autowired
    private HouseFeignClient houseFeignClient;

    @Override
    public HouseDataVO houseDetail(Long houseId) {
        if (null == houseId) {
            return null;
        }

        // 调用feign接口查询房源数据
        R<HouseDetailVO> r = houseFeignClient.detail(houseId);
        if (null == r
                || r.getCode() != ResultCode.SUCCESS.getCode()
                || null == r.getData()) {
            log.error("查询房源详情失败！");
            return null;
        }

        HouseDataVO houseDataVO = new HouseDataVO();
        BeanUtils.copyProperties(r.getData(), houseDataVO);
        return houseDataVO;

    }
}
