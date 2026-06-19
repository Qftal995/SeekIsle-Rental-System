package com.seekisle.portalservice.homepage.service;

import com.seekisle.portalservice.homepage.domain.dto.CityDescDTO;

import java.util.List;

/**
 * @author: yibo
 */
public interface IRegionService {

    List<CityDescDTO> regionChildren(Long parentId);

}
