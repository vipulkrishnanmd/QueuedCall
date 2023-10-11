package com.vipul.queuedcall.core;

import com.vipul.queuedcall.QueuedCall;

public interface QueuedCallSender {
    public abstract void send(QueuedCall queuedCall);
}
