package com.seekisle.chatservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seekisle.chatservice.domain.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: yibo
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
