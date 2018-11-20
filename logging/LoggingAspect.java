package com.ppzt.catalog.configuration.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Slf4j
@Aspect
@Configuration
public class LoggingAspect {

    @Pointcut("within(com.eustimenko.integration..*)")
    public void controllerPackagePointcut() {
    }

    @Around("@annotation(com.eustimenko.shared.annotation.TrackTime)")
    public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            final long timeTaken = System.currentTimeMillis() - startTime;
            log.info("Time Taken by {} is {}", joinPoint, timeTaken);
        }
    }

    @Around("controllerPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        final Signature signature = joinPoint.getSignature();
        final String declaringTypeName = signature.getDeclaringTypeName();
        final String name = signature.getName();

        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", declaringTypeName, name,
                    Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", declaringTypeName, name, result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()), declaringTypeName, name);
            throw e;
        }
    }
}
