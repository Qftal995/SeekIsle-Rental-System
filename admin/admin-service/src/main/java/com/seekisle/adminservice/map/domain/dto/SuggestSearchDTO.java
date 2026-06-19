package com.seekisle.adminservice.map.domain.dto;

import lombok.Data;

/**
 * 城市搜索地点查询条件
 */
@Data
public class SuggestSearchDTO {

    /**
     * 搜索关键字
     */
    private String keyword;

    /**
     * 城市id
     */
    private String id;

    /**
     * 页码
     */
    private Integer pageIndex;

    /**
     * 页数
     */
    private Integer pageSize;
}
