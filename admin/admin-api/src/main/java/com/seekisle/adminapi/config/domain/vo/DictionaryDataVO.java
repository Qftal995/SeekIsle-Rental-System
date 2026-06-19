package com.seekisle.adminapi.config.domain.vo;

import lombok.Data;

@Data
public class DictionaryDataVO {
    private Long id;
    private String typeKey;
    private String dataKey;
    private String value;
    private String remark;
    private Integer sort;
    private Integer status;
}
