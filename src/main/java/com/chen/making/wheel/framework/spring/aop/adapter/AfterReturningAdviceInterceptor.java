package com.chen.making.wheel.framework.spring.aop.adapter;

import com.chen.making.wheel.framework.spring.aop.advice.AfterReturningAdvice;
import com.chen.making.wheel.framework.spring.aop.intercepter.MethodInterceptor;
import com.chen.making.wheel.framework.spring.aop.intercepter.MethodInvocation;

/**
 * @author 陈添明
 * @date 2019/6/2
 */
public class AfterReturningAdviceInterceptor implements MethodInterceptor {

    private final AfterReturningAdvice advice;


    public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }
}
