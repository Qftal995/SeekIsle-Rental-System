package com.seekisle.chatservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seekisle.chatservice.domain.entity.Session;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: yibo
 */
@Mapper
public interface SessionMapper extends BaseMapper<Session> {
}
