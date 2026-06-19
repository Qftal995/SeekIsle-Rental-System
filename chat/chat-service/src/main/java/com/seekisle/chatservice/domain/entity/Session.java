package com.seekisle.chatservice.domain.entity;

import com.seekisle.commoncore.domain.entity.BaseDO;
import lombok.Data;

/**
 * @author: yibo
 */
@Data
public class Session extends BaseDO {
    private Long userId1;
    private Long userId2;
}
