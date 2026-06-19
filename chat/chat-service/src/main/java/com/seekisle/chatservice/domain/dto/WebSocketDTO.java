package com.seekisle.chatservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: yibo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketDTO<T> {

    private String type;

    private T data;

}