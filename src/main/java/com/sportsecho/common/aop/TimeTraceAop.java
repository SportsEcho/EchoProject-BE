package com.sportsecho.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "TimeTraceAop")
public class TimeTraceAop {

    @Pointcut("execution(* com.sportsecho.member..*(..))")
    public void user() {
    }

    @Pointcut("execution(* com.sportsecho.memberProduct..*(..))")
    public void memberProduct() {
    }

    @Pointcut("execution(* com.sportsecho.purchase..*(..))")
    public void purchase() {
    }

    @Around("user() || memberProduct() || purchase()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        log.info("START: " + joinPoint.toString());

        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;

            log.info("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}