package com.vipul.bus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Test extends EventBase {
    private String field;
    private String secondField;

    @JsonCreator
    public Test(
            @JsonProperty("field") String field,
            @JsonProperty("secondField") String secondField
    ) {
        this.field = field;
        this.secondField = secondField;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSecondField() {
        return secondField;
    }

    public void setSecondField(String secondField) {
        this.secondField = secondField;
    }
}
