package com.chen.simple.spring.framework.beans;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanName);

    int getBeanDefinitionCount();
}
