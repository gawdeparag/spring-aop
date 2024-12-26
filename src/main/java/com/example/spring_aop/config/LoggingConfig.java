package com.example.spring_aop.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    @PostConstruct
    public void configureLogger() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Create a console appender
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);

        // Set a pattern layout encoder for the appender
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();

        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        // Configure the root logger
        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO); // Set the desired log level (INFO, DEBUG, etc.)
        rootLogger.addAppender(consoleAppender);

        // Optionally, configure a specific logger
        Logger specificLogger = context.getLogger("io.opentelemetry.exporter.logging");
        specificLogger.setLevel(Level.DEBUG); // Customize level for specific package
    }
}
