package com.seekisle.adminapi.house.feign;

import com.seekisle.adminapi.house.domain.dto.SearchHouseListReqDTO;
import com.seekisle.adminapi.house.domain.vo.HouseDetailVO;
import com.seekisle.commondomain.domain.R;
import com.seekisle.commondomain.domain.vo.BasePageVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: yibo
 */
@FeignClient(contextId = "houseFeignClient", value = "admin", path = "/house")
public interface HouseFeignClient {

    /**
     * 查询房源列表，支持筛选、排序、翻页
     */
    @PostMapping("/list/search")
    R<BasePageVO<HouseDetailVO>> searchList(@Validated @RequestBody SearchHouseListReqDTO searchHouseListReqDTO);


    /**
     * 查询房源详情（带缓存）
     */
    @GetMapping("/detail")
    R<HouseDetailVO> detail(@RequestParam Long houseId);

}
