package bullish.electronic.store.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Log4j2
/**
 * Add aspect around all controller methods, to log method execution time
 * later you can search and notify ops team, if any methods runs more then 100ms
 */
public class AspectAopConfig {
    @Around("execution(* bullish.electronic.store.controller.*.*.*(..))")
    public Object addMetrics(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = point.proceed();
        stopWatch.stop();
        log.info("Execution time of " + className + "." + methodName + "=" + stopWatch.getTotalTimeMillis() + "ms");
        return result;
    }
}
