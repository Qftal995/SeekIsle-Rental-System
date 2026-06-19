package com.seekisle.chatservice.service;

import com.seekisle.chatservice.domain.dto.SessionAddReqDTO;
import com.seekisle.chatservice.domain.dto.SessionGetReqDTO;
import com.seekisle.chatservice.domain.dto.SessionHouseReqDTO;
import com.seekisle.chatservice.domain.dto.SessionListReqDTO;
import com.seekisle.chatservice.domain.vo.SessionAddResVO;
import com.seekisle.chatservice.domain.vo.SessionGetResVO;

import java.util.List;

/**
 * @author: yibo
 */
public interface ISessionService {

    /**
     * 新建咨询会话
     *
     * @param sessionAddReqDTO
     * @return
     */
    SessionAddResVO add(SessionAddReqDTO sessionAddReqDTO);

    /**
     * 查询俩用户的会话信息
     *
     * @param sessionGetReqDTO
     * @return
     */
    SessionGetResVO get(SessionGetReqDTO sessionGetReqDTO);

    /**
     * 获取会话列表
     *
     * @param sessionListReqDTO
     * @return
     */
    List<SessionGetResVO> list(SessionListReqDTO sessionListReqDTO);

    /**
     * 判断会话中是否聊过某房源
     *
     * @param sessionHouseReqDTO
     * @return
     */
    Boolean hasHouse(SessionHouseReqDTO sessionHouseReqDTO);
}
