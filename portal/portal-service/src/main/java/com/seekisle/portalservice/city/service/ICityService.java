package com.seekisle.portalservice.city.service;

import com.seekisle.portalservice.city.domain.vo.CityPageVO;

/**
 * @author: yibo
 */
public interface ICityService {

    /**
     * 获取热门城市与全城市列表
     *
     * @return
     */
    CityPageVO getCityPage();

}
