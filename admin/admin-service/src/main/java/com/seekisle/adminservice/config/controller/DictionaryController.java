package com.seekisle.adminservice.config.controller;

import com.seekisle.adminapi.config.domain.dto.*;
import com.seekisle.adminapi.config.domain.vo.DictionaryDataVO;
import com.seekisle.adminapi.config.domain.vo.DictionaryTypeVO;
import com.seekisle.adminapi.config.feign.DictionaryFeignClient;
import com.seekisle.adminservice.config.service.ISysDictionaryService;
import com.seekisle.commondomain.domain.R;
import com.seekisle.commondomain.domain.vo.BasePageVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 字典服务相关接口
 */
@RestController
@Slf4j
public class DictionaryController implements DictionaryFeignClient {

    /**
     * 字典类型表 服务类
     */
    @Resource
    private ISysDictionaryService iSysDictionaryService;

    /**
     * 新增字典类型
     * @param dictionaryTypeWriteReqDTO 新增字典类型DTO
     * @return Long
     */
    @PostMapping("/dictionary_type/add")
    public R<Long> addType(@RequestBody @Validated DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        return R.ok(iSysDictionaryService.addType(dictionaryTypeWriteReqDTO));
    }

    /**
     * 编辑字典类型
     * @param dictionaryTypeWriteReqDTO 编辑字典类型DTO
     * @return Long
     */
    @PostMapping("/dictionary_type/edit")
    public R<Long> editType(@RequestBody @Validated DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        return R.ok(iSysDictionaryService.editType(dictionaryTypeWriteReqDTO));
    }

    /**
     * 字典类型列表
     * @param dictionaryTypeListReqDTO 字典类型列表DTO
     * @return BasePageVO
     */
    @GetMapping("/dictionary_type/list")
    public R<BasePageVO<DictionaryTypeVO>> listType(@Validated DictionaryTypeListReqDTO dictionaryTypeListReqDTO) {
        return R.ok(iSysDictionaryService.listType(dictionaryTypeListReqDTO));
    }

    /**
     * 添加字典数据
     * @param dictionaryDataAddReqDTO 添加字典数据DTO
     * @return Long
     */
    @PostMapping("/dictionary_data/add")
    public R<Long> addData(@RequestBody @Validated DictionaryDataAddReqDTO dictionaryDataAddReqDTO) {
        return R.ok(iSysDictionaryService.addData(dictionaryDataAddReqDTO));
    }

    /**
     * 编辑字典数据
     * @param dictionaryDataEditReqDTO 编辑字典数据DTO
     * @return Long
     */
    @PostMapping("/dictionary_data/edit")
    public R<Long> editData(@RequestBody @Validated DictionaryDataEditReqDTO dictionaryDataEditReqDTO) {
        return R.ok(iSysDictionaryService.editData(dictionaryDataEditReqDTO));
    }

    /**
     * 字典数据列表
     * @param dictionaryDataListReqDTO 字典数据列表DTO
     * @return BasePageVO
     */
    @GetMapping("/dictionary_data/list")
    public R<BasePageVO<DictionaryDataVO>> listData(@Validated DictionaryDataListReqDTO dictionaryDataListReqDTO) {
        return R.ok(iSysDictionaryService.listData(dictionaryDataListReqDTO));
    }

    /**
     * 获取某个字典类型下的所有字典数据
     * @param typeKey 字典类型键
     * @return 字典数据列表
     */
    @Override
    public List<DictionaryDataDTO> selectDictDataByType(String typeKey) {
        return iSysDictionaryService.selectDictDataByType(typeKey);
    }

    /**
     * 获取多个字典类型下的所有字典数据
     * @param typeKeys 字典类型键列表
     * @return 哈希 字典类型键->字典数据列表
     */
    @Override
    public Map<String, List<DictionaryDataDTO>> selectDictDataByTypes(List<String> typeKeys) {
        return iSysDictionaryService.selectDictDataByTypes(typeKeys);
    }

    /**
     * 根据字典数据键获取字典数据对象
     * @param dataKey 字典数据键
     * @return 字典数据对象
     */
    @Override
    public DictionaryDataDTO getDicDataByKey(String dataKey) {
        return iSysDictionaryService.selectDictDataByDataKey(dataKey);
    }

    /**
     * 根据字典数据键列表获取字典数据对象列表
     * @param dataKeys 字典数据键列表
     * @return 字典数据对象列表
     */
    @Override
    public List<DictionaryDataDTO> getDicDataByKeys(List<String> dataKeys) {
        return iSysDictionaryService.selectDictDataByDataKeys(dataKeys);
    }

}
