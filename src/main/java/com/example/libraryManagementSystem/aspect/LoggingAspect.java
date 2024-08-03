package com.example.libraryManagementSystem.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Define a pointcut for all methods in the service package
    @Pointcut("execution(* com.example.libraryManagementSystem.service..*(..))")
    public void serviceLayer() {}

    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Executing: {}", joinPoint.getSignature());
    }

    @After("serviceLayer()")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("Completed: {}", joinPoint.getSignature());
    }

    @AfterThrowing(pointcut = "serviceLayer()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in {} with cause = {}", joinPoint.getSignature(), exception.getCause() != null ? exception.getCause() : "NULL");
    }
}
