package com.vipul.queuedcall.core;

import com.vipul.queuedcall.QueuedCall;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class QueuedCallListener {
    @Autowired
    private QueuedCallEngine queuedCallEngine;

    public abstract void listen();

    protected void processQueuedCall(QueuedCall data) {
        this.queuedCallEngine.listen(data);
    }
}
