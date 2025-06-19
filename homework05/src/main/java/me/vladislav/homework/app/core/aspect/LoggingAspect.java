package me.vladislav.homework.app.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
  private final AtomicLong callsCounter = new AtomicLong(0);

  @Before("execution(* me.vladislav.homework.app.api.route.*Controller.*(..))")
  public void logBeforeControllerMethods() {
    log.info("Controller method is about to be called");
  }

  @Around("execution(* me.vladislav.homework.app.api.route.*Controller.*(..))")
  public Object logAroundControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    callsCounter.incrementAndGet();

    Object result = joinPoint.proceed();

    long endTime = System.currentTimeMillis();
    log.info(
        "Finished execution of {}.{} - Duration: {}ms",
        joinPoint.getTarget().getClass().getSimpleName(),
        joinPoint.getSignature().getName(),
        (endTime - startTime));
    callsCounter.incrementAndGet();
    return result;
  }

  public long getCallsCount() {
    return callsCounter.get();
  }
}
