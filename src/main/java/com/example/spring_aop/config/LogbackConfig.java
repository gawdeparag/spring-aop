package com.example.spring_aop.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogbackConfig {

    @PostConstruct
    public static void configure() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//        context.reset(); // Reset to remove default configurations
        System.out.println("CONTEXT IN LOGBACK CONFIG::"+context.getName());

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);

        // Create a PatternLayoutEncoder
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg trace_id=%X{trace_id} span_id=%X{span_id}%n");
        encoder.start();
        System.out.println("ENCODER::"+encoder);

        // Create a ConsoleAppender
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();
        System.out.println("CONSOLE APPENDER::"+consoleAppender.getEncoder().toString());

        // Configure the root logger
        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(consoleAppender);
        rootLogger.setLevel(ch.qos.logback.classic.Level.INFO);

        System.out.println("ROOT LOGGER::"+rootLogger.getLevel());
        System.out.println("Logback has been configured programmatically.");
    }
}
