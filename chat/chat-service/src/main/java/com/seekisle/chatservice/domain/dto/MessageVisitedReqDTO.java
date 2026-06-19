package com.seekisle.chatservice.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author: yibo
 */
@Data
public class MessageVisitedReqDTO {
    @NotNull(message = "会话id不能为空！")
    private Long sessionId;
}