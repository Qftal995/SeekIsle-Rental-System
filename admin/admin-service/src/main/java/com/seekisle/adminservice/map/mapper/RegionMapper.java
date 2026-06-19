package com.seekisle.adminservice.map.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seekisle.adminservice.map.domain.entity.SysRegion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 区域mapper信息
 */
@Mapper
public interface RegionMapper extends BaseMapper<SysRegion> {

    /**
     * 获取城市列表
     */
    List<SysRegion> selectAllReigon();

    /**
     * 获取下级区域列表
     *
     * @param parentId 父级id
     * @return 子级区域列表
     */
    List<SysRegion> selectRegionChildren(@Param("parentId") Long parentId);

}
