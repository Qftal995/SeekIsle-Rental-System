package com.bitejiuyeke.biteadminservice.config.service;


import com.bitejiuyeke.biteadminapi.config.domain.dto.*;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DictionaryDataVO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DictionaryTypeVO;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;

import java.util.List;
import java.util.Map;//n

/**
 * 字典类型表 服务类
 */
public interface ISysDictionaryService {

    /**
     * 新增字典类型
     * @param dictionaryTypeWriteReqDTO 新增字典类型DTO
     * @return Long
     */
    Long addType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO);

    /**
     * 编辑字典类型
     * @param dictionaryTypeWriteReqDTO 编辑字典类型DTO
     * @return Long
     */
    Long editType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO);

    /**
     * 字典类型列表
     * @param dictionaryTypeListReqDTO 字典类型列表DTO
     * @return BasePageVO
     */
    BasePageVO<DictionaryTypeVO> listType(DictionaryTypeListReqDTO dictionaryTypeListReqDTO);

    /**
     * 添加字典数据
     * @param dictionaryDataAddReqDTO 添加字典数据DTO
     * @return Long
     */
    Long addData(DictionaryDataAddReqDTO dictionaryDataAddReqDTO);

    /**
     * 编辑字典数据
     * @param dictionaryDataEditReqDTO 编辑字典数据DTO
     * @return Long
     */
    Long editData(DictionaryDataEditReqDTO dictionaryDataEditReqDTO);

    /**
     * 字典数据列表
     * @param dictionaryDataListReqDTO 字典数据列表DTO
     * @return BasePageVO
     */
    BasePageVO<DictionaryDataVO> listData(DictionaryDataListReqDTO dictionaryDataListReqDTO);

    /**
     * 获取某个字典类型下的所有字典数据
     * @param dictTypeCode 字典类型键
     * @return 字典数据列表
     */
    List<DictionaryDataDTO> selectDictDataByType(String dictTypeCode);

    /**
     * 获取多个字典类型下的所有字典数据
     * @param dictTypeCodes 字典类型键列表
     * @return 哈希 字典类型键->字典数据列表
     */
    Map<String, List<DictionaryDataDTO>> selectDictDataByTypes(List<String> dictTypeCodes);

    /**
     * 根据字典数据键获取字典数据对象
     * @param dataKey 字典数据键
     * @return 字典数据对象
     */
    DictionaryDataDTO selectDictDataByDataKey(String dataKey);

    /**
     * 根据字典数据键列表获取字典数据对象列表
     * @param dataKeys 字典数据键列表
     * @return 字典数据对象列表
     */
    List<DictionaryDataDTO> selectDictDataByDataKeys(List<String> dataKeys);
}
