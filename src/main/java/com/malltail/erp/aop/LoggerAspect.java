package com.malltail.erp.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggerAspect {
    /**
     * Before : 대상 메서드가 실행되기 전 Advice를 실행.
     * @param jointPoint
     */
    @Before("execution(* com.malltail.erp.controller.*.*(..))")
    public void logBefore(JoinPoint jointPoint){
        //log.info("///___Before: " +jointPoint.getSignature().getName());
        //log.info("Security check passed for user: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
    }

    /**
     * After : 대상 메서드가 실행된 후에 Advice를 실행
     * @param joinPoint
     */
    @After("execution(* com.malltail.erp.controller.*.*(..))")
    public void logAfter(JoinPoint joinPoint){
       // log.info("///___After: " +joinPoint.getSignature().getName());
    }

    /**
     * AfterReturning: 대상 메서드가 정상적으로 실행되고 반환된 후에 Advice를 실행.
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "execution(* com.malltail.erp.controller.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result){
        //log.info("///___AfterReturning: " +joinPoint.getSignature().getName()+ " result: " +result);
    }

    /**
     * AfterThrowing: 대상 메서드에서 예외가 발생했을때 Advice를 실행.
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "execution(* com.malltail.erp.controller.*.*(..))", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e){
       //log.info("///___AfterThrowing: " +joinPoint.getSignature().getName()+ " exception :" +e.getMessage());
    }

    /**
     * Around: 대상 메서드 실행 전,후 또는 예외발생시에 Advice 실행.
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.malltail.erp.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //log.info("///___Around Before: " +joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        //log.info("///___Around after: " +joinPoint.getSignature().getName());
        return result;
    }

//    @Before("execution(* com.example.project.service.*.*(..))")
//    public void startTransaction() {
//        System.out.println("Transaction started");
//    }
//
//    @AfterReturning("execution(* com.example.project.service.*.*(..))")
//    public void commitTransaction() {
//        System.out.println("Transaction committed");
//    }



//    @Around("execution(* com.example.project..*Controller.*(..)) || " +
//            "execution(* com.example.project..*Service.*(..)) || " +
//            "execution(* com.example.project..*Mapper.*(..))")
//    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        String name = joinPoint.getSignature().getDeclaringTypeName();
//        String type =
//                StringUtils.contains(name, "Controller") ? "Controller ==> " :
//                StringUtils.contains(name, "Service") ? "Service ==> " :
//                StringUtils.contains(name, "Mapper") ? "Mapper ==>" :
//                "";
//
//        log.debug(type + name + "." + joinPoint.getSignature().getName() +"()");
//        return joinPoint.proceed();
//
//    }

}
