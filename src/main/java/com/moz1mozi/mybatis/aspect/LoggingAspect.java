package com.moz1mozi.mybatis.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.moz1mozi.mybatis..*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("메소드 시작 : {}", joinPoint.getSignature());
        // 메소드 실행 전 시간 측정
        long start = System.currentTimeMillis();

        try {
            // 대상 메소드 실행
            return joinPoint.proceed();
        } finally {
            // 메소드 실행 후 시간 측정 및 실행 시간 계산
            long executionTime = System.currentTimeMillis() - start;
            log.debug("메소드 종료 : {} 실행 시간: {} ms", joinPoint.getSignature(), executionTime);
        }
    }
}
