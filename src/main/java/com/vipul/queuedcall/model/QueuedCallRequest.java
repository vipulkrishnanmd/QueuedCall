package com.vipul.queuedcall.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class QueuedCallRequest extends QueuedCall{
    private String type;
    private String id;
    private String name;
    private Class[] paramTypes;
    private Object[] args;
}
