package com.chen.making.wheel.framework.spring.beans;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public class BeanWrapper {

    private Object wrappedInstance;

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    /**
     * 返回包装对象
     * @return
     */
    public Object getWrappedInstance() {
        return this.wrappedInstance;
    }

    /**
     * 返回包装后的 Class
     * 可能会是这个
     *
     * @return
     */
    public Class<?> getWrappedClass() {
        return this.wrappedInstance.getClass();
    }
}
