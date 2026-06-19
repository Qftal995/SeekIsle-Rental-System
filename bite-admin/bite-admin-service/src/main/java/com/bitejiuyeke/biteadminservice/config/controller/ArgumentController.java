package com.bitejiuyeke.biteadminservice.config.controller;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentAddReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentEditReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentListReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.ArgumentVO;
import com.bitejiuyeke.biteadminapi.config.feign.ArgumentFeignClient;
import com.bitejiuyeke.biteadminservice.config.service.ISysArgumentService;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数服务相关接口
 */
@RestController
@Slf4j
@RequestMapping("/argument")
public class ArgumentController  implements ArgumentFeignClient {

    /**
     * 参数相关服务类
     */
    @Resource
    private ISysArgumentService iSysArgumentService;

    /**
     * 新增参数
     * @param argumentAddReqDTO 添加参数DTO
     * @return 参数ID
     */
    @PostMapping("/add")
    public R<Long> add(@RequestBody ArgumentAddReqDTO argumentAddReqDTO) {
        return R.ok(iSysArgumentService.add(argumentAddReqDTO));
    }

    /**
     * 编辑参数
     * @param argumentEditReqDTO 编辑参数DTO
     * @return 参数ID
     */
    @PostMapping("/edit")
    public R<Long> edit(@RequestBody ArgumentEditReqDTO argumentEditReqDTO) {
        return R.ok(iSysArgumentService.edit(argumentEditReqDTO));
    }

    /**
     * 参数列表
     * @param argumentListReqDTO 查询参数DTO
     * @return 参数分页结果
     */
    @GetMapping("/list")
    public R<BasePageVO<ArgumentVO>> list(@Validated ArgumentListReqDTO argumentListReqDTO) {
        return R.ok(iSysArgumentService.list(argumentListReqDTO));
    }

    /**
     * 根据参数键列表查询参数列表
     * @param configKeys 参数键
     * @return 参数列表
     */
    @Override
    public List<ArgumentDTO> getByConfigKeys(List<String> configKeys) {
        return iSysArgumentService.getByConfigKeys(configKeys);
    }

    /**
     * 根据参数键查询参数
     * @param configKey 参数键
     * @return 参数
     */
    @Override
    public ArgumentDTO getByConfigKey(String configKey) {
        return iSysArgumentService.getByConfigKey(configKey);
    }
}
