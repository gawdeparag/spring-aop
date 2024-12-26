package com.example.spring_aop.aspect;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OpenTelemetryAspect {
    @Autowired
    private Tracer tracer;

    public OpenTelemetryAspect(Tracer tracer) {
        this.tracer = tracer;
    }

//    @Pointcut("within(com.example.spring_aop..*(..))")
//    public void applicationPackagePointcut() { }

    @Around("execution(* com.example.spring_aop.service.*.*(..))")
//    @Around("applicationPackagePointcut()")   // No trace_id/span_id seen
//    @Around("@annotation(org.springframework.stereotype.Service)")
//    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || "
//            + "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
//            + "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object traceMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        // Fetch the global tracer
        this.tracer = GlobalOpenTelemetry.getTracer("spring-aop");
        String methodName = joinPoint.getSignature().getName();

        // Start a span for the method
        Span span = tracer.spanBuilder(methodName).startSpan();

        try (Scope scope = span.makeCurrent()) {
            System.out.println("Trace ID: " + span.getSpanContext().getTraceId());
            System.out.println("Span ID: " + span.getSpanContext().getSpanId());
            return joinPoint.proceed();
        } finally {
            span.end();
        }
    }
}
