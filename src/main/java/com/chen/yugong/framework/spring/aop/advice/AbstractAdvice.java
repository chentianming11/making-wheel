package com.chen.yugong.framework.spring.aop.advice;


import com.chen.yugong.framework.spring.aop.intercepter.Advice;

import java.lang.reflect.Method;

/**
 * @author 陈添明
 * @date 2019/6/2
 */
public abstract class AbstractAdvice implements Advice {


    protected Object aspectObject;

    protected Method aspectMethod;

    public AbstractAdvice(Object aspectObject, Method aspectMethod) {
        this.aspectObject = aspectObject;
        this.aspectMethod = aspectMethod;
    }

    public void before(Method method, Object[] args, Object target) throws Throwable {

    };

    public void afterReturning(Object retVal, Method method, Object[] arguments, Object target) throws Throwable {

    }

    public abstract boolean isBeforeAdvice();

    public abstract boolean isAfterAdvice();

}
