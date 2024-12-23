package com.example.spring_aop.aspect;

import com.example.spring_aop.config.OpenTelemetryConfig;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final OpenTelemetryConfig openTelemetryConfig;

    @Autowired
    public LoggingAspect(OpenTelemetryConfig openTelemetryConfig) {
        this.openTelemetryConfig = openTelemetryConfig;
    }

    @Around("@annotation(io.opentelemetry.extension.annotations.WithSpan) || execution(* com.example..*(..))")
    public Object traceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String spanName = joinPoint.getSignature().toShortString();
        System.out.println("SPAN NAME:: "+spanName);
        Span span = openTelemetryConfig.tracer().spanBuilder(spanName).startSpan();
        System.out.println("SPAN:: "+span);
        try (Scope scope = span.makeCurrent()) {
            System.out.println("Trace ID: " + span.getSpanContext().getTraceId());
            System.out.println("Span ID: " + span.getSpanContext().getSpanId());
            return joinPoint.proceed();
        } catch (Exception ex) {
            span.recordException(ex);
            throw ex;
        } finally {
            span.end();
        }
    }

}
