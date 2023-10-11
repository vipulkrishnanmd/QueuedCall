package com.vipul.queuedcall;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class QueuedCall {
    private String type;
    private String id;
}
