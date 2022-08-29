package com.tpay.commons.logger;

public interface LogTrace {
    TraceStatus begin(String message);

    void end(TraceStatus status);
    void exception(TraceStatus status, Throwable e);
}
