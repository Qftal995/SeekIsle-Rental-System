package com.seekisle.chatservice.domain.vo;

import com.seekisle.adminapi.appuser.domain.vo.AppUserVO;
import lombok.Data;

/**
 * @author: yibo
 */
@Data
public class SessionAddResVO {
    private Long sessionId;

    private AppUserVO loginUser;

    private AppUserVO otherUser;

}