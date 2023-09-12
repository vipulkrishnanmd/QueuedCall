package com.vipul.queuedcall;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

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
