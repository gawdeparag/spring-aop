package com.example.spring_aop.service;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    public void performTask() {
        callPrivateMethod();
        System.out.println("Task performed!");
    }

    private void callPrivateMethod() {
        System.out.println("Private method called and IDs attached to it!");
    }

    public void hello() {
        System.out.println("Hello world!");
    }
}
