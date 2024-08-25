/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 12
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.jooq.rest.api.framework.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerAspect {

    private long start = 0L;

    @Pointcut(value = "within(kr.co.rokroot.java.demo.jooq.rest.api.module..*Controller)")
    private void apiPointcut() {  }

    @Before(value = "apiPointcut()")
    public void before() {
        this.start = System.currentTimeMillis();
    }

    @Around(value = "apiPointcut()")
    public Object apiProceed(ProceedingJoinPoint point) throws Throwable {
        point.getSignature();

        log.info("[API call] {}", point.getSignature().getName());

        return point.proceed();
    }

    @After(value = "apiPointcut()")
    public void after() {
        log.info("### Established time(s): {}ms", String.valueOf(System.currentTimeMillis() - this.start));
    }

}
