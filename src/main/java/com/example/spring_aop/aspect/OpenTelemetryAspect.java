package com.example.spring_aop.aspect;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class OpenTelemetryAspect {

    private final Tracer tracer;

    public OpenTelemetryAspect(Tracer tracer) {
        this.tracer = tracer;
    }

    // Pointcut for all controller methods
    @Pointcut("execution(* com.example.spring_aop.controller..*(..))")
    public void controllerMethods() {}

    // Start a trace for controller methods
    @Around("controllerMethods()")
    public Object traceController(ProceedingJoinPoint joinPoint) throws Throwable {
        String traceName = joinPoint.getSignature().toShortString();
        Span trace = tracer.spanBuilder(traceName).startSpan();

        try (Scope scope = trace.makeCurrent()) {
            System.out.println("Trace started: " + traceName);
            return joinPoint.proceed();
        } catch (Exception ex) {
            trace.recordException(ex);
            throw ex;
        } finally {
            trace.end();
            System.out.println("Trace ended: " + traceName);
        }
    }

    // Maintain a map of spans for tracking lifecycle
    private final Map<String, Span> activeSpans = new HashMap<>();

    // Pointcut for service methods
    @Pointcut("execution(* com.example.spring_aop.service..*(..))")
    public void serviceMethods() {}

    // Before advice to start a span
    @Before(value = "serviceMethods()")
    public void startSpan(JoinPoint joinPoint) {
        String spanName = joinPoint.getSignature().toShortString();
        Span span = tracer.spanBuilder(spanName).startSpan();
        Scope scope = span.makeCurrent();

        // Store span and scope in a thread-local map
        activeSpans.put(spanName, span);
        System.out.println("activeSpans traced in startSpan: "+activeSpans);

        System.out.println("Span started: " + spanName + ", traceId: "+span.getSpanContext().getTraceId()
        + " spanId: "+span.getSpanContext().getSpanId());

    }

    // After advice to end a span
    @After(value = "serviceMethods()")
    public void endSpan(JoinPoint joinPoint) {
        String spanName = joinPoint.getSignature().toShortString();
        Span span = activeSpans.get(spanName);
        System.out.println("activeSpans traced in endSpan: "+activeSpans);

        if (span != null) {
            span.end();
            activeSpans.remove(spanName);
            System.out.println("Span ended: " + spanName);
        }
    }

    // After throwing advice to record exceptions
    @AfterThrowing(value = "serviceMethods()", throwing = "exception")
    public void handleException(JoinPoint joinPoint, Throwable exception) {
        String spanName = joinPoint.getSignature().toShortString();
        Span span = activeSpans.get(spanName);

        if (span != null) {
            span.recordException(exception);
            span.end();
            activeSpans.remove(spanName);
            System.out.println("Exception recorded and span ended: " + spanName);
        }
    }
}
