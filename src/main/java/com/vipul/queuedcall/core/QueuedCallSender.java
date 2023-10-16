package com.vipul.queuedcall.core;

import com.vipul.queuedcall.model.QueuedCall;

public interface QueuedCallSender {
    public abstract void send(QueuedCall queuedCall);
}
