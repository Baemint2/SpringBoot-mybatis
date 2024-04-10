package com.moz1mozi.mybatis.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class TransactionLoggingAspect {

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void logTransactionStart(JoinPoint joinPoint) {
        log.info("트랜잭션 시작 : {}", joinPoint.getSignature());
    }

    @AfterReturning("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void logTransactionCommit(JoinPoint joinPoint) {
        log.info("트랜잭션 커밋 : {}", joinPoint.getSignature());
    }


    @AfterThrowing(pointcut = "@annotation(org.springframework.transaction.annotation.Transactional)", throwing = "ex")
    public void logTransactionRollback(JoinPoint joinPoint, Throwable ex) {
        log.info("트랜잭션 롤백: {} 예외 : {}", joinPoint.getSignature(), ex.getMessage());
    }

    @After("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void logTransactionEnd(JoinPoint joinPoint) {
        log.info("트랜잭션 종료: {}", joinPoint.getSignature());
    }
}
