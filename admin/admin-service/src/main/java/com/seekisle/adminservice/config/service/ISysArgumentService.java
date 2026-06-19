package com.seekisle.adminservice.config.service;

import com.seekisle.adminapi.config.domain.dto.ArgumentAddReqDTO;
import com.seekisle.adminapi.config.domain.dto.ArgumentDTO;
import com.seekisle.adminapi.config.domain.dto.ArgumentEditReqDTO;
import com.seekisle.adminapi.config.domain.dto.ArgumentListReqDTO;
import com.seekisle.adminapi.config.domain.vo.ArgumentVO;
import com.seekisle.commondomain.domain.vo.BasePageVO;

import java.util.List;

/**
 * 参数相关服务类
 */
public interface ISysArgumentService {

    /**
     * 新增参数
     * @param argumentAddReqDTO 添加参数DTO
     * @return 参数ID
     */
    Long add(ArgumentAddReqDTO argumentAddReqDTO);

    /**
     * 编辑参数
     * @param argumentEditReqDTO 编辑参数DTO
     * @return 参数ID
     */
    Long edit(ArgumentEditReqDTO argumentEditReqDTO);

    /**
     * 参数列表
     * @param argumentListReqDTO 查询参数DTO
     * @return 参数分页结果
     */
    BasePageVO<ArgumentVO> list(ArgumentListReqDTO argumentListReqDTO);

    /**
     * 根据参数键列表获取参数对象列表
     * @param configKeys 参数键列表
     * @return 参数对象列表
     */
    List<ArgumentDTO> getByConfigKeys(List<String> configKeys);

    /**
     * 根据参数键获取参数对象
     * @param configKey 参数键
     * @return 参数DTO
     */
    ArgumentDTO getByConfigKey(String configKey);
}
