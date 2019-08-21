package com.chen.yugong.framework.spring.aop;

import com.chen.yugong.framework.spring.aop.intercepter.MethodInterceptor;
import com.chen.yugong.framework.spring.aop.intercepter.MethodInvocation;
import com.chen.yugong.framework.spring.util.AopProxyUtils;
import com.chen.yugong.framework.spring.util.AopUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author 陈添明
 * @date 2019/6/1
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    private final Object proxy;
    private final Object target;
    private final Class<?> targetClass;
    private final Method method;
    private final Object[] arguments;
    private final List<Object> interceptorsAndDynamicMethodMatchers;

    /**
     * Index from 0 of the current interceptor we're invoking.
     * -1 until we invoke: then the current interceptor.
     */
    private int currentInterceptorIndex = -1;

    public ReflectiveMethodInvocation(Object proxy, Object target, Method method,
                                      Object[] arguments, Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {

        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = AopProxyUtils.adaptArgumentsIfNecessary(method, arguments);
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    /**
     * Get the arguments as an array object.
     * It is possible to change element values within this
     * array to change the arguments.
     *
     * @return the argument of the invocation
     */
    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    /**
     * Proceed to the next interceptor in the chain.
     * <p>The implementation and the semantics of this method depends
     * on the actual joinpoint type (see the children interfaces).
     *
     * @return see the children interfaces' proceed definition
     * @throws Throwable if the joinpoint throws an exception
     */
    @Override
    public Object proceed() throws Throwable {
        //如果Interceptor执行完了，则执行joinPoint
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return invokeJoinpoint();
        }
        Object interceptorOrInterceptionAdvice =
                this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);

        //执行当前Intercetpor
        return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
    }

    private Object invokeJoinpoint() throws Throwable {
        return AopUtils.invokeJoinpointUsingReflection(this.target, this.method, this.arguments);
    }

    /**
     * Return the object that holds the current joinpoint's static part.
     * <p>For instance, the target object for an invocation.
     *
     * @return the object (can be null if the accessible object is static)
     */
    @Override
    public Object getThis() {
        return this.target;
    }

    /**
     * Return the static part of this joinpoint.
     * <p>The static part is an accessible object on which a chain of
     * interceptors are installed.
     */
    @Override
    public AccessibleObject getStaticPart() {
        return this.method;
    }
}
