package com.bitejiuyeke.biteadminapi.config.feign;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentDTO;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

/**
 * 参数相关接口
 */
@FeignClient(contextId = "argumentFeignClient", value = "bite-admin", path = "/argument")
public interface ArgumentFeignClient {
    /**
     * 根据参数键列表查询参数列表
     * @param configKeys 参数键
     * @return 参数列表
     */
    List<ArgumentDTO> getByConfigKeys(List<String> configKeys);

    /**
     * 根据参数键查询参数
     * @param configKey 参数键
     * @return 参数
     */
    ArgumentDTO getByConfigKey(String configKey);
}
