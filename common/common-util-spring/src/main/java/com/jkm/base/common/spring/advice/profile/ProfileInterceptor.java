package com.jkm.base.common.spring.advice.profile;

import com.jkm.base.common.spring.advice.profile.method.ProfileAdvice;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by hutao on 15/7/8.
 * 下午2:06
 */
@Aspect
@Slf4j
@Component
public class ProfileInterceptor {
    @Setter
    private boolean openProfile = true;
    @Autowired
    private ProfileAdvice profileAdvice;

    /**
     * 定义一个切入点
     */
    @Pointcut("execution(@com.jkm.base.common.spring.advice.profile.ProfileAnotation * com.jkm..*.*(..))")
    public void profileMethod() {
        //TODO
    }

    /**
     * 统计性能
     *
     * @param pjp
     * @throws Throwable
     */
    @Around("profileMethod()")
    public Object doBasicProfiling(final ProceedingJoinPoint pjp) throws Throwable {
        if (openProfile) {
            final MethodSignature signature = (MethodSignature) pjp.getSignature();
            final Method method = signature.getMethod();
            final Class clazz = signature.getDeclaringType();
            try {
                pre(method, clazz);
                return pjp.proceed();
            } finally {
                post(method, clazz);
            }
        } else {
            return pjp.proceed();
        }
    }

    private void post(final Method method, final Class clazz) {
        profileAdvice.post(clazz, method);
    }

    private void pre(final Method method, final Class clazz) {
        profileAdvice.pre(clazz, method);
    }
}
