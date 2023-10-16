package com.vipul.queuedcall.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class QueuedCallResponse extends QueuedCall{
    private String type;
    private String id;
    private Object response;
}
