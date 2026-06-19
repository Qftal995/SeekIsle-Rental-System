package com.seekisle.adminservice.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seekisle.adminapi.config.domain.dto.ArgumentAddReqDTO;
import com.seekisle.adminapi.config.domain.dto.ArgumentDTO;
import com.seekisle.adminapi.config.domain.dto.ArgumentEditReqDTO;
import com.seekisle.adminapi.config.domain.dto.ArgumentListReqDTO;
import com.seekisle.adminapi.config.domain.vo.ArgumentVO;
import com.seekisle.adminservice.config.domain.entity.SysArgument;
import com.seekisle.adminservice.config.mapper.SysArgumentMapper;
import com.seekisle.adminservice.config.service.ISysArgumentService;
import com.seekisle.commondomain.domain.ResultCode;
import com.seekisle.commondomain.domain.vo.BasePageVO;
import com.seekisle.commondomain.exception.ServiceException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统参数表 服务实现类
 */
@Service
@Slf4j
public class SysArgumentServiceImpl extends ServiceImpl<SysArgumentMapper, SysArgument> implements ISysArgumentService {

    @Resource
    private SysArgumentMapper sysArgumentMapper;

    /**
     * 新增参数
     * @param argumentAddReqDTO 添加参数DTO
     * @return 参数ID
     */
    @Override
    public Long add(ArgumentAddReqDTO argumentAddReqDTO) {
        LambdaQueryWrapper<SysArgument> wrapper = new LambdaQueryWrapper<>();
        // 尽量减少使用select *
        // 加的时候，名和键都不能重复
        wrapper.select(SysArgument::getId).eq(SysArgument::getName, argumentAddReqDTO.getName()).or().eq(SysArgument::getConfigKey, argumentAddReqDTO.getConfigKey());
        SysArgument sysArgument = sysArgumentMapper.selectOne(wrapper);
        if (sysArgument != null) {
            throw new ServiceException("参数已经存在", ResultCode.INVALID_PARA.getCode());
        }
        sysArgument = new SysArgument();
        sysArgument.setName(argumentAddReqDTO.getName());
        sysArgument.setValue(argumentAddReqDTO.getValue());
        sysArgument.setConfigKey(argumentAddReqDTO.getConfigKey());
        if (StringUtils.isNotBlank(argumentAddReqDTO.getRemark())) {
            sysArgument.setRemark(argumentAddReqDTO.getRemark());
        }
        sysArgumentMapper.insert(sysArgument);
        return sysArgument.getId();
    }

    /**
     * 编辑参数
     * @param argumentEditReqDTO 编辑参数DTO
     * @return 参数ID
     */
    @Override
    public Long edit(ArgumentEditReqDTO argumentEditReqDTO) {
        SysArgument sysArgument = sysArgumentMapper.selectOne(new LambdaQueryWrapper<SysArgument>().eq(SysArgument::getConfigKey, argumentEditReqDTO.getConfigKey()));
        if (sysArgument == null) {
            throw new ServiceException("参数不存在", ResultCode.INVALID_PARA.getCode());
        }
        sysArgument.setName(argumentEditReqDTO.getName());
        sysArgument.setValue(argumentEditReqDTO.getValue());
        sysArgument.setRemark(argumentEditReqDTO.getRemark());
        sysArgumentMapper.updateById(sysArgument);
        return sysArgument.getId();
    }

    /**
     * 参数列表
     * @param argumentListReqDTO 查询参数DTO
     * @return 参数分页结果
     */
    @Override
    public BasePageVO<ArgumentVO> list(ArgumentListReqDTO argumentListReqDTO) {
        BasePageVO<ArgumentVO> result = new BasePageVO<>();
        LambdaQueryWrapper<SysArgument> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(argumentListReqDTO.getConfigKey())) {
            queryWrapper.eq(SysArgument::getConfigKey, argumentListReqDTO.getConfigKey());
        }
        if (StringUtils.isNotBlank(argumentListReqDTO.getName())) {
            queryWrapper.likeRight(SysArgument::getName, argumentListReqDTO.getName());
        }
        Page<SysArgument> page = sysArgumentMapper.selectPage(new Page<>(argumentListReqDTO.getPageNo().longValue(), argumentListReqDTO.getPageSize().longValue()), queryWrapper);
        result.setTotals(Integer.parseInt(String.valueOf(page.getTotal())));
        result.setTotalPages(Integer.parseInt(String.valueOf(page.getPages())));
        List<ArgumentVO> list = new ArrayList<>();
        for (SysArgument sysArgument : page.getRecords()) {
            ArgumentVO argumentVo = new ArgumentVO();
            BeanUtils.copyProperties(sysArgument, argumentVo);
            list.add(argumentVo);
        }
        result.setList(list);
        return result;
    }

    /**
     * 根据参数键列表获取参数对象列表
     * @param configKeys 参数键列表
     * @return 参数对象列表
     */
    @Override
    public List<ArgumentDTO> getByConfigKeys(List<String> configKeys) {
        if (configKeys.isEmpty()) {
            return null;
        }
        LambdaQueryWrapper<SysArgument> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysArgument::getConfigKey, configKeys);
        List<SysArgument> result = sysArgumentMapper.selectList(queryWrapper);
        List<ArgumentDTO> list = new ArrayList<>();
        for (SysArgument sysArgument : result) {
            ArgumentDTO argumentDTO = new ArgumentDTO();
            BeanUtils.copyProperties(sysArgument, argumentDTO);
            list.add(argumentDTO);
        }
        return list;
    }

    /**
     * 根据参数键获取参数对象
     * @param configKey 参数键
     * @return 参数DTO
     */
    @Override
    public ArgumentDTO getByConfigKey(String configKey) {
        List<ArgumentDTO> list = getByConfigKeys(new ArrayList<>(){{add(configKey);}});
        return list.isEmpty() == true ? null : list.get(0);
    }
}
