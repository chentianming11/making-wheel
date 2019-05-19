package com.chen.simple.spring.framework.beans;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public class BeanWrapper {

    private Object wrappedInstance;

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return this.wrappedInstance;
    }

    /**
     * 返回代理以后的 Class
     * 可能会是这个 $Proxy0
     *
     * @return
     */
    public Class<?> getWrappedClass() {
        return this.wrappedInstance.getClass();
    }
}
