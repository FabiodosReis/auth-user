package com.application.security.infra.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;


@Aspect
@Component
@Log4j2
@RequiredArgsConstructor
public class LogAspect {

    private final ObjectMapper mapper;

    @Around("@annotation(com.application.security.infra.annotation.Log)")
    public Object test(ProceedingJoinPoint joinPoint) throws Throwable {
        var parameterMap = new HashMap<String, Object>();
        var method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        var className = method.getDeclaringClass().getSimpleName();
        var methodArguments = joinPoint.getArgs();
        var parameterNameList =
                Arrays.stream(((CodeSignature) joinPoint.getSignature()).getParameterNames())
                        .toList();

        for(int i = 0; i < parameterNameList.size(); i++){
            parameterMap.put(parameterNameList.get(i), mapper.writeValueAsString(methodArguments[i]));
        }

        log.info("[{}] Init method {}", className, method.getName());
        parameterMap.forEach((key, value) -> log.info("[{}] {} {}", className, key, value));

        Object proceed = joinPoint.proceed();

        log.info("[{}] method {} successfully", className, method.getName());
        return proceed;
    }
}
