package com.chen.making.wheel.framework.spring.aop.advice;

import java.lang.reflect.Method;

/**
 * @author 陈添明
 * @date 2019/6/2
 */
public class MethodBeforeAdvice  extends AbstractAdvice {


    public MethodBeforeAdvice(Object aspectInstance, Method method) {
        super(aspectInstance, method);
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        aspectMethod.invoke(aspectObject, null);
    }



    @Override
    public boolean isBeforeAdvice() {
        return true;
    }

    @Override
    public boolean isAfterAdvice() {
        return false;
    }
}
