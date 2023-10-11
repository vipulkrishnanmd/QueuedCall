package com.vipul.queuedcall;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@Data
@Builder
@Jacksonized
public class QueuedCallBatchedRequest extends QueuedCall{
    private String type;
    private List<QueuedCallRequest> batch;
    private String name;
}
