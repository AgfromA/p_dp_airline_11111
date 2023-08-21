package app.util.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("@annotation(app.util.aop.Loggable)")
    public void loggable() {}

    @Around("loggable()")
    public Object getExecutionTime(ProceedingJoinPoint point) {
        var start = System.currentTimeMillis();
        Object object;
        try {
            object = point.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        long time = end - start;
        var signature = (MethodSignature) point.getSignature();
        var method = signature.getMethod();

        log.info("{}.{}, execution time: {} ms",
                method.getDeclaringClass().getSimpleName(), method.getName(), time);
        return object;
    }
}