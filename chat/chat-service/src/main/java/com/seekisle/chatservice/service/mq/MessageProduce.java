package com.seekisle.chatservice.service.mq;

import com.seekisle.chatservice.config.RabbitMqConfig;
import com.seekisle.chatservice.domain.dto.MessageSendReqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: yibo
 */
@Component
@Slf4j
public class MessageProduce {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(MessageSendReqDTO messageSendReqDTO) {
        try {
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "", messageSendReqDTO);
        } catch (Exception e) {
            log.error("咨询聊天消息发送异常！", e);
        }
    }


}
