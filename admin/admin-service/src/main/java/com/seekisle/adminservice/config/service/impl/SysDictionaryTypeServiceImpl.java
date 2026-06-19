package com.seekisle.adminservice.config.service.impl;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seekisle.adminapi.config.domain.dto.*;
import com.seekisle.adminapi.config.domain.vo.DictionaryDataVO;
import com.seekisle.adminapi.config.domain.vo.DictionaryTypeVO;
import com.seekisle.adminservice.config.domain.entity.SysDictionaryData;
import com.seekisle.adminservice.config.domain.entity.SysDictionaryType;
import com.seekisle.adminservice.config.mapper.SysDictionaryDataMapper;
import com.seekisle.adminservice.config.mapper.SysDictionaryTypeMapper;
import com.seekisle.adminservice.config.service.ISysDictionaryService;
import com.seekisle.commondomain.domain.vo.BasePageVO;
import com.seekisle.commondomain.exception.ServiceException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 字典类型表 服务实现类
 */
@Service
public class SysDictionaryTypeServiceImpl implements ISysDictionaryService {

    @Resource
    private SysDictionaryDataMapper sysDictionaryDataMapper;

    @Resource
    private SysDictionaryTypeMapper sysDictionaryTypeMapper;

    @Override
    public Long addType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        LambdaQueryWrapper<SysDictionaryType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SysDictionaryType::getId).eq(SysDictionaryType::getValue, dictionaryTypeWriteReqDTO.getValue()).or()
                .eq(SysDictionaryType::getTypeKey, dictionaryTypeWriteReqDTO.getTypeKey());
        SysDictionaryType sysDictionaryType = sysDictionaryTypeMapper.selectOne(lambdaQueryWrapper);
        sysDictionaryTypeMapper.selectList(lambdaQueryWrapper);
        if (sysDictionaryType != null) {
            throw new ServiceException("字典类型键或者值已存在");
        }
        sysDictionaryType = new SysDictionaryType();
        sysDictionaryType.setValue(dictionaryTypeWriteReqDTO.getValue());
        sysDictionaryType.setTypeKey(dictionaryTypeWriteReqDTO.getTypeKey());
        if (StringUtils.isNotBlank(dictionaryTypeWriteReqDTO.getRemark())) {
            sysDictionaryType.setRemark(dictionaryTypeWriteReqDTO.getRemark());
        }
        sysDictionaryTypeMapper.insert(sysDictionaryType);
        return sysDictionaryType.getId();
    }

    @Override
    public Long editType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        SysDictionaryType sysDictionaryType = sysDictionaryTypeMapper.selectOne(new LambdaQueryWrapper<SysDictionaryType>().eq(SysDictionaryType::getTypeKey, dictionaryTypeWriteReqDTO.getTypeKey()));
        if (sysDictionaryType == null) {
            throw new ServiceException("字典类型不存在");
        }
        if (sysDictionaryTypeMapper.selectOne(new LambdaQueryWrapper<SysDictionaryType>()
                .ne(SysDictionaryType::getTypeKey, dictionaryTypeWriteReqDTO.getTypeKey())
                .eq(SysDictionaryType::getValue, dictionaryTypeWriteReqDTO.getValue())
        ) != null) {
            throw new ServiceException("字典类型名称有已存在");
        }
        sysDictionaryType.setValue(dictionaryTypeWriteReqDTO.getValue());
        sysDictionaryType.setRemark(dictionaryTypeWriteReqDTO.getRemark());
        sysDictionaryTypeMapper.updateById(sysDictionaryType);
        return sysDictionaryType.getId();
    }

    @Override
    public BasePageVO<DictionaryTypeVO> listType(DictionaryTypeListReqDTO dictionaryTypeListReqDTO) {
        BasePageVO<DictionaryTypeVO> result = new BasePageVO<>();
        LambdaQueryWrapper<SysDictionaryType> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(dictionaryTypeListReqDTO.getValue())) {
            queryWrapper.likeRight(SysDictionaryType::getValue, dictionaryTypeListReqDTO.getValue());
        }
        if (StringUtils.isNotBlank(dictionaryTypeListReqDTO.getTypeKey())) {
            queryWrapper.eq(SysDictionaryType::getTypeKey, dictionaryTypeListReqDTO.getTypeKey());
        }
        Page<SysDictionaryType> page = sysDictionaryTypeMapper.selectPage(
                new Page<>(dictionaryTypeListReqDTO.getPageNo().longValue(), dictionaryTypeListReqDTO.getPageSize().longValue()),
                queryWrapper
        );
        result.setTotals(Integer.parseInt(String.valueOf(page.getTotal())));
        result.setTotalPages(Integer.parseInt(String.valueOf(page.getPages())));
        List<DictionaryTypeVO> list = new ArrayList<>();
        for (SysDictionaryType sysDictionaryType : page.getRecords()) {
            DictionaryTypeVO dictionaryTypeVO = new DictionaryTypeVO();
            BeanUtils.copyProperties(sysDictionaryType, dictionaryTypeVO);
            list.add(dictionaryTypeVO);
        }
        result.setList(list);
        return result;
    }

    @Override
    public Long addData(DictionaryDataAddReqDTO dictionaryDataAddReqDTO) {
        if (sysDictionaryTypeMapper.selectOne(new LambdaQueryWrapper<SysDictionaryType>().eq(SysDictionaryType::getTypeKey, dictionaryDataAddReqDTO.getTypeKey())) == null) {
            throw new ServiceException("字典类型不存在");
        }
        SysDictionaryData sysDictionaryData = sysDictionaryDataMapper.selectOne(new LambdaQueryWrapper<SysDictionaryData>()
                .eq(SysDictionaryData::getValue, dictionaryDataAddReqDTO.getValue())
                .or()
                .eq(SysDictionaryData::getDataKey, dictionaryDataAddReqDTO.getDataKey())
        );
        if (sysDictionaryData != null) {
            throw new ServiceException("字典数据键或值已存在");
        }
        sysDictionaryData = new SysDictionaryData();
        sysDictionaryData.setDataKey(dictionaryDataAddReqDTO.getDataKey());
        sysDictionaryData.setTypeKey(dictionaryDataAddReqDTO.getTypeKey());
        if (dictionaryDataAddReqDTO.getSort() != null) {
            sysDictionaryData.setSort(dictionaryDataAddReqDTO.getSort());
        }
        if (StringUtils.isNotBlank(dictionaryDataAddReqDTO.getRemark())) {
            sysDictionaryData.setRemark(dictionaryDataAddReqDTO.getRemark());
        }
        sysDictionaryData.setValue(dictionaryDataAddReqDTO.getValue());
        sysDictionaryDataMapper.insert(sysDictionaryData);
        return sysDictionaryData.getId();
    }

    @Override
    public Long editData(DictionaryDataEditReqDTO dictionaryDataEditReqDTO) {
        SysDictionaryData sysDictionaryData = sysDictionaryDataMapper.selectOne(new LambdaQueryWrapper<SysDictionaryData>().eq(SysDictionaryData::getDataKey, dictionaryDataEditReqDTO.getDataKey()));
        if (sysDictionaryData == null) {
            throw new ServiceException("字典数据不存在");
        }
        if (sysDictionaryDataMapper.selectOne(new LambdaQueryWrapper<SysDictionaryData>().ne(SysDictionaryData::getDataKey, dictionaryDataEditReqDTO.getDataKey()).eq(SysDictionaryData::getValue, dictionaryDataEditReqDTO.getValue())) != null) {
            throw new ServiceException("字典数据值已存在");
        }
        sysDictionaryData.setValue(dictionaryDataEditReqDTO.getValue());
        if (dictionaryDataEditReqDTO.getSort() != null) {
            sysDictionaryData.setSort(dictionaryDataEditReqDTO.getSort());
        }
        sysDictionaryData.setRemark(dictionaryDataEditReqDTO.getRemark());
        sysDictionaryDataMapper.updateById(sysDictionaryData);
        return sysDictionaryData.getId();
    }

    @Override
    public BasePageVO<DictionaryDataVO> listData(DictionaryDataListReqDTO dictionaryDataListReqDTO) {
        BasePageVO<DictionaryDataVO> result = new BasePageVO<>();
        LambdaQueryWrapper<SysDictionaryData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictionaryData::getTypeKey, dictionaryDataListReqDTO.getTypeKey());
        if (StringUtils.isNotBlank(dictionaryDataListReqDTO.getValue())) {
            queryWrapper.likeRight(SysDictionaryData::getValue, dictionaryDataListReqDTO.getValue());
        }
        queryWrapper.orderByAsc(SysDictionaryData::getSort);
        queryWrapper.orderByAsc(SysDictionaryData::getId);
        Page<SysDictionaryData> page = sysDictionaryDataMapper.selectPage(
                new Page<>(dictionaryDataListReqDTO.getPageNo().longValue(), dictionaryDataListReqDTO.getPageSize().longValue()),
                queryWrapper
        );
        result.setTotals(Integer.parseInt(String.valueOf(page.getTotal())));
        result.setTotalPages(Integer.parseInt(String.valueOf(page.getPages())));
        List<DictionaryDataVO> list = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : page.getRecords()) {
            DictionaryDataVO dictionaryTypeVO = new DictionaryDataVO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryTypeVO);
            list.add(dictionaryTypeVO);
        }
        result.setList(list);
        return result;
    }

    /**
     * 获取某个字典类型下的所有字典数据
     * @param dictTypeCode 字典类型键
     * @return 字典数据列表
     */
    @Override
    public List<DictionaryDataDTO> selectDictDataByType(String dictTypeCode) {
        List<SysDictionaryData> list = sysDictionaryDataMapper.selectList(new LambdaQueryWrapper<SysDictionaryData>().eq(SysDictionaryData::getTypeKey, dictTypeCode));
        List<DictionaryDataDTO> result = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : list) {
            DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryDataDTO);
            result.add(dictionaryDataDTO);
        }
        return result;
    }

    /**
     * 获取多个字典类型下的所有字典数据
     * @param dictTypeCodes 字典类型键列表
     * @return 哈希 字典类型键->字典数据列表
     */
    @Override
    public Map<String, List<DictionaryDataDTO>> selectDictDataByTypes(List<String> dictTypeCodes) {
        List<SysDictionaryData> list = sysDictionaryDataMapper.selectList(new LambdaQueryWrapper<SysDictionaryData>().in(SysDictionaryData::getTypeKey, dictTypeCodes));
        List<DictionaryDataDTO> result = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : list) {
            DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryDataDTO);
            result.add(dictionaryDataDTO);
        }
        Map<String, List<DictionaryDataDTO>> map = Maps.newHashMap();
        for (DictionaryDataDTO dictionaryDataDTO : result) {
            List<DictionaryDataDTO> value;
            if (map.get(dictionaryDataDTO.getTypeKey()) == null) {
                value = new ArrayList<>();
                value.add(dictionaryDataDTO);
                map.put(dictionaryDataDTO.getTypeKey(), value);
            } else {
                value = map.get(dictionaryDataDTO.getTypeKey());
                value.add(dictionaryDataDTO);
            }
        }
        return map;
    }

    /**
     * 根据字典数据键获取字典数据对象
     * @param dataKey 字典数据键
     * @return 字典数据对象
     */
    @Override
    public DictionaryDataDTO selectDictDataByDataKey(String dataKey) {
        List<DictionaryDataDTO> result = selectDictDataByDataKeys(new ArrayList<>(){{
            add(dataKey);
        }});
        return !result.isEmpty() ? result.get(0) : null;
    }

    /**
     * 根据字典数据键列表获取字典数据对象列表
     * @param dataKeys 字典数据键列表
     * @return 字典数据对象列表
     */
    @Override
    public List<DictionaryDataDTO> selectDictDataByDataKeys(List<String> dataKeys) {
        if (dataKeys.isEmpty()) return Collections.emptyList();
        List<SysDictionaryData> list = sysDictionaryDataMapper.selectList(new LambdaQueryWrapper<SysDictionaryData>().in(SysDictionaryData::getDataKey, dataKeys));
        List<DictionaryDataDTO> result = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : list) {
            DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryDataDTO);
            result.add(dictionaryDataDTO);
        }
        return result;
    }
}
