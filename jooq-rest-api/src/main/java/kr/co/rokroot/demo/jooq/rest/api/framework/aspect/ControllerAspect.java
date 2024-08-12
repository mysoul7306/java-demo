/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 12
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api.framework.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerAspect {

    private long start = 0L;

    @Pointcut("within(kr.co.rokroot.demo.jooq.rest.api.module..*Controller)")
    public void apiPointcut() {
    }

    @Before("apiPointcut()")
    public void before() {
        this.start = System.currentTimeMillis();
    }

    @Around("apiPointcut()")
    public Object apiProceed(ProceedingJoinPoint point) throws Throwable {
        point.getSignature();

        log.info("[API call] {}", point.getSignature().getName());

        return point.proceed();
    }

    @After("apiPointcut()")
    public void after() {
        log.info("### Established time(s): {}ms", System.currentTimeMillis() - this.start);
    }

}
