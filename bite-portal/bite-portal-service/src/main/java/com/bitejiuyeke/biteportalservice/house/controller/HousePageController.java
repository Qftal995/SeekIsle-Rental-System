package com.bitejiuyeke.biteportalservice.house.controller;

import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.biteportalservice.house.domain.vo.HouseDataVO;
import com.bitejiuyeke.biteportalservice.house.service.IHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: yibo
 */
@RestController
@RequestMapping("/housepage")
public class HousePageController {

    @Autowired
    private IHouseService houseService;

    /**
     * C端查询房源详情
     */
    @GetMapping("/get/nologin")
    public R<HouseDataVO> houseDetail(Long houseId) {
        return R.ok(houseService.houseDetail(houseId));
    }

}
