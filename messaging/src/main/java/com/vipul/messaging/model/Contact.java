package com.vipul.messaging.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Document
public @Data class Contact {
    @Id
    private String id;
    @Field
    private String name;

    @Field
    @Indexed(unique = true)
    private String externalRef;

    @Field
    private String namespace;
}
