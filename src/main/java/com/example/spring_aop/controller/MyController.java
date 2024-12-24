package com.example.spring_aop.controller;

import com.example.spring_aop.service.MyService;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @Autowired
    MyService myService;

    @GetMapping("/task")
    public void performTask() {
        myService.performTask();
    }
}
