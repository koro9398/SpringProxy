package com.example.springproxy.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SimpleServiceAspect {
    // @Pointcut("within(com.example.springproxy.simpleservice.*) && execution(* *..*Service.someMethod())")
    @Pointcut("execution(* com.example.springproxy.simpleservice..*Service*.someMethod())")
    void pointcutExpression() {}

    @Around("pointcutExpression()")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        String result = (String) joinPoint.proceed();
        return "Proxied result: " + result;
    }
}
