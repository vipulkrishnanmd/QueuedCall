package com.vipul.bus;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class EventBase {
    private String type;
    private String id;

    public EventBase() {
        this.type = this.getClass().getName();
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
