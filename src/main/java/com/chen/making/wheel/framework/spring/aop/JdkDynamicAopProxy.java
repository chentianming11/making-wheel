package com.chen.making.wheel.framework.spring.aop;


import com.chen.making.wheel.framework.spring.aop.intercepter.MethodInvocation;
import com.chen.making.wheel.framework.spring.util.AopProxyUtils;
import com.chen.making.wheel.framework.spring.util.AopUtils;
import com.chen.making.wheel.framework.spring.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author 陈添明
 * @date 2019/6/1
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    /** Config used to configure this proxy */
    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport config) {
        this.advised = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(ClassUtils.getDefaultClassLoader());
    }

    @Override
    public Object getTarget() {
        return advised.getTarget();
    }


    @Override
    public Object getProxy(ClassLoader classLoader) {
        Class<?>[] proxiedInterfaces = advised.getProxiedInterfaces();
        return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodInvocation invocation;
        Object retVal;
        //获得目标对象的类
        Object target = advised.getTarget();
        Class<?> targetClass = (target != null ? target.getClass() : null);

        //获取可以应用到此方法上的Interceptor列表
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
        //如果没有可以应用到此方法的通知(Interceptor)，此直接反射调用 method.invoke(target, args)
        if (chain.isEmpty()) {
            Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
            retVal = AopUtils.invokeJoinpointUsingReflection(target, method, argsToUse);
        }
        else {
            invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
            // Proceed to the joinpoint through the interceptor chain.
            retVal = invocation.proceed();
        }
        return retVal;
    }


}
