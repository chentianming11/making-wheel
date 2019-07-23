package com.chen.simple.spring.framework.spring.aop.adapter;

import com.chen.simple.spring.framework.spring.aop.advice.MethodBeforeAdvice;
import com.chen.simple.spring.framework.spring.aop.intercepter.MethodInterceptor;
import com.chen.simple.spring.framework.spring.aop.intercepter.MethodInvocation;

/**
 * 前置拦截通知
 * @author 陈添明
 * @date 2019/6/2
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    private MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis() );
        return mi.proceed();
    }
}
