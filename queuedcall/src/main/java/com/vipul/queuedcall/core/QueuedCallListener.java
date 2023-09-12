package com.vipul.queuedcall.core;

import com.vipul.queuedcall.QueuedCall;
import com.vipul.queuedcall.QueuedCallRequest;
import com.vipul.queuedcall.QueuedCallResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueuedCallListener {
    private final QueuedCallEngine queuedCallEngine;

    public  void listen(QueuedCall data) {
        if ("request".equals(data.getType())) {
            QueuedCallRequest request = (QueuedCallRequest) data;
            queuedCallEngine.processRequest(request);
        }

        if ("response".equals(data.getType())) {
            QueuedCallResponse response = (QueuedCallResponse) data;
            queuedCallEngine.processResponse(response);
        }
    }
}
