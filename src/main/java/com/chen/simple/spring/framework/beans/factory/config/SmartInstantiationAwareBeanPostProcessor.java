package com.chen.simple.spring.framework.beans.factory.config;

/**
 * @author 陈添明
 * @date 2019/6/4
 */
public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {


    /**
     * 返回提前访问的bean的引用
     * @param bean
     * @param beanName
     * @return
     */
    default Object getEarlyBeanReference(Object bean, String beanName)  {
        return bean;
    }
}
