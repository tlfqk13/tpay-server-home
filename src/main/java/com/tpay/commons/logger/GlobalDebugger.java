package com.tpay.commons.logger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalDebugger {

    private final LogTrace logTrace;

    @Pointcut("execution(* com.tpay.domains..*.*(..))" +
            " && !@annotation(NotDebugMethod)")
    private void pointcut() {}

    @Around("pointcut()")
    public Object trace(ProceedingJoinPoint point) throws Throwable {
        TraceStatus status = null;
        try{
            status = logTrace.begin(point.getSignature().toShortString());
            Object result = point.proceed();
            logTrace.end(status);
            return result;
        }catch (Throwable e){
            logTrace.exception(status, e);
            throw e;
        }
    }
}
