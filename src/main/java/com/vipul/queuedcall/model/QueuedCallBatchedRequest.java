package com.vipul.queuedcall.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class QueuedCallBatchedRequest extends QueuedCall{
    private String type;
    private List<QueuedCallRequest> batch;
    private String name;
}
