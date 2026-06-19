package com.seekisle.chatservice.controller;

import com.seekisle.chatservice.domain.dto.SessionAddReqDTO;
import com.seekisle.chatservice.domain.dto.SessionGetReqDTO;
import com.seekisle.chatservice.domain.dto.SessionHouseReqDTO;
import com.seekisle.chatservice.domain.dto.SessionListReqDTO;
import com.seekisle.chatservice.domain.vo.SessionAddResVO;
import com.seekisle.chatservice.domain.vo.SessionGetResVO;
import com.seekisle.chatservice.service.ISessionService;
import com.seekisle.commondomain.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: yibo
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private ISessionService sessionService;


    /**
     * 新建咨询会话
     */
    @PostMapping("/add")
    public R<SessionAddResVO> add(@Validated @RequestBody SessionAddReqDTO sessionAddReqDTO) {
        return R.ok(sessionService.add(sessionAddReqDTO));
    }

    /**
     * 查询咨询会话
     */
    @PostMapping("/get")
    public R<SessionGetResVO> get(@Validated @RequestBody SessionGetReqDTO sessionGetReqDTO ) {
        return R.ok(sessionService.get(sessionGetReqDTO));
    }

    /**
     * 查询咨询会话列表
     */
    @PostMapping("/list")
    public R<List<SessionGetResVO>> list(@Validated @RequestBody SessionListReqDTO sessionListReqDTO ) {
        return R.ok(sessionService.list(sessionListReqDTO));
    }

    /**
     * 查看会话下是否聊过某房源
     *
     * @param sessionHouseReqDTO
     * @return
     */
    @PostMapping("/has_house")
    public R<Boolean> hasHouse(@Validated @RequestBody SessionHouseReqDTO sessionHouseReqDTO) {
        return R.ok(sessionService.hasHouse(sessionHouseReqDTO));
    }


}
