package com.example.spring_aop.aspect;


import com.example.spring_aop.config.LogbackConfig;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
    @Around("execution(* com.example..*(..))")
    public Object addTraceIdsToMDC(ProceedingJoinPoint joinPoint) throws Throwable {
        String spanName = joinPoint.getSignature().toShortString();
        System.out.println("SPAN NAME FROM ASPECT::"+spanName);
        Span span = OpenTelemetry.noop().getTracer("spring-aop")
                .spanBuilder(spanName).startSpan();
        Object result = joinPoint.proceed();
        System.out.println("SPAN CREATED::"+span);
        System.out.println("RESULT::"+result);
        return result;
    }
}
