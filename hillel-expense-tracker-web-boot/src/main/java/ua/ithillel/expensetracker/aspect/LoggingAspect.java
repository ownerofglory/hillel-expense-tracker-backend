package ua.ithillel.expensetracker.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(* ua.ithillel.expensetracker.service.*.*(..))")
    private void serviceMethods() {}

    @Pointcut("execution(* ua.ithillel.expensetracker.repo.*.*(..))")
    private void repoMethods() {}

    @Pointcut("execution(* ua.ithillel.expensetracker.dao.*.*(..))")
    private void daoMethods() {}

    @Pointcut("execution(* ua.ithillel.expensetracker.client.*.*(..))")
    private void clientMethods() {}

    @Pointcut("execution(* ua.ithillel.expensetracker.controller.*.*(..))")
    private void controllerMethods() {}

    @Around("serviceMethods() || repoMethods() || daoMethods() || clientMethods() || controllerMethods()")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // before calling the method
        log.info("Calling {}({})", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

        Object retVal = joinPoint.proceed();

        // after returning from methods
        log.info("Returning {} = {}", joinPoint.getSignature().getName(), retVal);

        return retVal;
    }

    @AfterThrowing(pointcut = "serviceMethods() || repoMethods() || daoMethods() || clientMethods() || controllerMethods()", throwing = "throwable")
    public void logAfterThrow(Throwable throwable) {
        log.error("Exception thrown by {}({})", throwable.getStackTrace()[0].getMethodName(), throwable.getMessage());
    }
}
