package com.seekisle.adminservice.config.controller;

import com.seekisle.adminapi.config.domain.dto.*;
import com.seekisle.adminservice.AdminServiceApplication;
import com.seekisle.adminservice.config.service.ISysDictionaryService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest(classes = AdminServiceApplication.class)
class DictionaryControllerTest {

    @Resource
    private ISysDictionaryService sysDictionaryService;

    @Test
    @Transactional
    void addType() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        Assertions.assertTrue(sysDictionaryService.addType(dto) > 0L);
    }

    @Test
    @Transactional
    void editType() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
        dto.setTypeKey("weight");
        dto.setValue("重量1");
        dto.setRemark("重量配置1");
        Assertions.assertTrue(sysDictionaryService.editType(dto) > 0L);
    }

    @Test
    @Transactional
    void listType() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
        dto.setTypeKey("height");
        dto.setValue("高度");
        dto.setRemark("高度配置");
        sysDictionaryService.addType(dto);
        DictionaryTypeListReqDTO dictionaryTypeListReqDTO = new DictionaryTypeListReqDTO();
        Assertions.assertTrue(sysDictionaryService.listType(dictionaryTypeListReqDTO).getList().size() > 0);
        dictionaryTypeListReqDTO.setTypeKey("weight");
        Assertions.assertTrue(sysDictionaryService.listType(dictionaryTypeListReqDTO).getList().size() == 1);
        dictionaryTypeListReqDTO.setTypeKey("weight1");
        Assertions.assertTrue(sysDictionaryService.listType(dictionaryTypeListReqDTO).getList().size() == 0);
    }

    @Test
    @Transactional
    void addData() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
        DictionaryDataAddReqDTO dictionaryDataAddReqDTO = new DictionaryDataAddReqDTO();
        dictionaryDataAddReqDTO.setTypeKey("weight");
        dictionaryDataAddReqDTO.setDataKey("tenKg");
        dictionaryDataAddReqDTO.setValue("十公斤");
        dictionaryDataAddReqDTO.setRemark("十公斤重量配置");
        dictionaryDataAddReqDTO.setSort(10);
        Assertions.assertTrue(sysDictionaryService.addData(dictionaryDataAddReqDTO) > 0L);
    }

    @Test
    @Transactional
    void editData() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
        DictionaryDataAddReqDTO dictionaryDataAddReqDTO = new DictionaryDataAddReqDTO();
        dictionaryDataAddReqDTO.setTypeKey("weight");
        dictionaryDataAddReqDTO.setDataKey("tenKg");
        dictionaryDataAddReqDTO.setValue("十公斤");
        dictionaryDataAddReqDTO.setRemark("十公斤重量配置");
        dictionaryDataAddReqDTO.setSort(10);
        sysDictionaryService.addData(dictionaryDataAddReqDTO);
        DictionaryDataEditReqDTO dictionaryDataEditReqDTO = new DictionaryDataEditReqDTO();
        dictionaryDataEditReqDTO.setValue("十公斤plus");
        dictionaryDataEditReqDTO.setDataKey("tenKg");
        dictionaryDataEditReqDTO.setRemark("十公斤重量配置plus");
        dictionaryDataEditReqDTO.setSort(20);
        Assertions.assertTrue(sysDictionaryService.editData(dictionaryDataEditReqDTO) > 0L);
    }

    @Test
    @Transactional
    void listData() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
        DictionaryDataAddReqDTO dictionaryDataAddReqDTO = new DictionaryDataAddReqDTO();
        dictionaryDataAddReqDTO.setTypeKey("weight");
        dictionaryDataAddReqDTO.setDataKey("tenKg");
        dictionaryDataAddReqDTO.setValue("十公斤");
        dictionaryDataAddReqDTO.setRemark("十公斤重量配置");
        dictionaryDataAddReqDTO.setSort(10);
        sysDictionaryService.addData(dictionaryDataAddReqDTO);
        dictionaryDataAddReqDTO.setDataKey("twentyKg");
        dictionaryDataAddReqDTO.setValue("二十公斤");
        dictionaryDataAddReqDTO.setRemark("二十公斤重量配置");
        dictionaryDataAddReqDTO.setSort(20);
        sysDictionaryService.addData(dictionaryDataAddReqDTO);
        DictionaryDataListReqDTO dictionaryDataListReqDTO = new DictionaryDataListReqDTO();
        dictionaryDataListReqDTO.setTypeKey("weight");
        Assertions.assertTrue(sysDictionaryService.listData(dictionaryDataListReqDTO).getList().size() == 2);
        dictionaryDataListReqDTO.setValue("二十公斤");
        Assertions.assertTrue(sysDictionaryService.listData(dictionaryDataListReqDTO).getList().size() == 1);
    }

    @Test
    @Transactional
    void selectDictDataByType() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
        DictionaryDataAddReqDTO dictionaryDataAddReqDTO = new DictionaryDataAddReqDTO();
        dictionaryDataAddReqDTO.setTypeKey("weight");
        dictionaryDataAddReqDTO.setDataKey("tenKg");
        dictionaryDataAddReqDTO.setValue("十公斤");
        dictionaryDataAddReqDTO.setRemark("十公斤重量配置");
        dictionaryDataAddReqDTO.setSort(10);
        sysDictionaryService.addData(dictionaryDataAddReqDTO);
        dictionaryDataAddReqDTO.setDataKey("twentyKg");
        dictionaryDataAddReqDTO.setValue("二十公斤");
        dictionaryDataAddReqDTO.setRemark("二十公斤重量配置");
        dictionaryDataAddReqDTO.setSort(20);
        sysDictionaryService.addData(dictionaryDataAddReqDTO);
        Assertions.assertTrue(sysDictionaryService.selectDictDataByType("weight").size() == 2);
    }

    @Test
    @Transactional
    void selectDictDataByTypes() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
        dto.setTypeKey("height");
        dto.setValue("高度");
        dto.setRemark("高度配置");
        sysDictionaryService.addType(dto);
        DictionaryDataAddReqDTO dictionaryDataAddReqDTO = new DictionaryDataAddReqDTO();
        dictionaryDataAddReqDTO.setTypeKey("weight");
        dictionaryDataAddReqDTO.setDataKey("tenKg");
        dictionaryDataAddReqDTO.setValue("十公斤");
        dictionaryDataAddReqDTO.setRemark("十公斤重量配置");
        dictionaryDataAddReqDTO.setSort(10);
        sysDictionaryService.addData(dictionaryDataAddReqDTO);
        dictionaryDataAddReqDTO.setTypeKey("height");
        dictionaryDataAddReqDTO.setDataKey("tenMeter");
        dictionaryDataAddReqDTO.setValue("十米");
        dictionaryDataAddReqDTO.setRemark("十米高度配置");
        dictionaryDataAddReqDTO.setSort(10);
        sysDictionaryService.addData(dictionaryDataAddReqDTO);
        Assertions.assertTrue(sysDictionaryService.selectDictDataByTypes(Arrays.asList("weight", "height")).get("weight").size() == 1);
    }

    @Test
    @Transactional
    void getDicDataByKey() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
        DictionaryDataAddReqDTO dictionaryDataAddReqDTO = new DictionaryDataAddReqDTO();
        dictionaryDataAddReqDTO.setTypeKey("weight");
        dictionaryDataAddReqDTO.setDataKey("tenKg");
        dictionaryDataAddReqDTO.setValue("十公斤");
        dictionaryDataAddReqDTO.setRemark("十公斤重量配置");
        dictionaryDataAddReqDTO.setSort(10);
        sysDictionaryService.addData(dictionaryDataAddReqDTO);
        dictionaryDataAddReqDTO.setDataKey("twentyKg");
        dictionaryDataAddReqDTO.setValue("二十公斤");
        dictionaryDataAddReqDTO.setRemark("二十公斤重量配置");
        dictionaryDataAddReqDTO.setSort(20);
        sysDictionaryService.addData(dictionaryDataAddReqDTO);
        Assertions.assertTrue(sysDictionaryService.selectDictDataByDataKey("tenKg").getValue().equals("十公斤"));
    }

    @Test
    @Transactional
    void getDicDataByKeys() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
        DictionaryDataAddReqDTO dictionaryDataAddReqDTO = new DictionaryDataAddReqDTO();
        dictionaryDataAddReqDTO.setTypeKey("weight");
        dictionaryDataAddReqDTO.setDataKey("tenKg");
        dictionaryDataAddReqDTO.setValue("十公斤");
        dictionaryDataAddReqDTO.setRemark("十公斤重量配置");
        dictionaryDataAddReqDTO.setSort(10);
        sysDictionaryService.addData(dictionaryDataAddReqDTO);
        dictionaryDataAddReqDTO.setDataKey("twentyKg");
        dictionaryDataAddReqDTO.setValue("二十公斤");
        dictionaryDataAddReqDTO.setRemark("二十公斤重量配置");
        dictionaryDataAddReqDTO.setSort(20);
        sysDictionaryService.addData(dictionaryDataAddReqDTO);
        Assertions.assertTrue(sysDictionaryService.selectDictDataByDataKeys(Arrays.asList("tenKg", "twentyKg")).size() == 2);
    }
}