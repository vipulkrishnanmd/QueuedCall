package com.vipul.messaging.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collections;
import java.util.List;

@Builder
@Document
public @Data class Conversation {
    @Id
    private String id;

    @Field()
    @Builder.Default
    private List<Message> messages = Collections.emptyList();

    @Field
    @Builder.Default
    private List<String> participantIds = Collections.emptyList();
}
