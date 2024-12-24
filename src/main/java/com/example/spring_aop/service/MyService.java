package com.example.spring_aop.service;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    public void performTask() {
        System.out.println("Task performed!");
    }
}
