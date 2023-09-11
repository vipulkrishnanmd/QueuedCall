package com.vipul.messaging.model;

import lombok.Data;

@Data
public class Message {
    private String authorId;
    private String text;
    private String conversationId;
}
