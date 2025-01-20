package com.example.spring_aop.service;

//import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.springframework.stereotype.Service;

@Service
//@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MyService {

    public void performTask() {
//        MyService proxy = (MyService) AopContext.currentProxy();
//        proxy.callPrivateMethod();    //AspectJ Weaver approach
        callPrivateMethod();
        System.out.println("Task performed!");
        callAnotherMethod();
    }

    private void callPrivateMethod() {
        System.out.println("Private method called and IDs attached to it!");
    }

    public void callAnotherMethod() {
        System.out.println("Another method called and IDs attached to it!");
    }

    public void hello() {
        System.out.println("Hello World!");
    }

}
