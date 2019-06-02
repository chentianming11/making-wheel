package com.chen.simple.spring.framework.aop;

/**
 * @author 陈添明
 * @date 2019/6/1
 */
public class CglibAopProxy implements AopProxy {
    /**
     * Create a new proxy object.
     * <p>Uses the AopProxy's default class loader (if necessary for proxy creation):
     * usually, the thread context class loader.
     *
     * @return the new proxy object (never {@code null})
     * @see Thread#getContextClassLoader()
     */
    @Override
    public Object getProxy() {
        return null;
    }

    /**
     * Create a new proxy object.
     * <p>Uses the given class loader (if necessary for proxy creation).
     * {@code null} will simply be passed down and thus lead to the low-level
     * proxy facility's default, which is usually different from the default chosen
     * by the AopProxy implementation's {@link #getProxy()} method.
     *
     * @param classLoader the class loader to create the proxy with
     *                    (or {@code null} for the low-level proxy facility's default)
     * @return the new proxy object (never {@code null})
     */
    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
