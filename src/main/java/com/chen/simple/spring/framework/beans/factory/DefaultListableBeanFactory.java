package com.chen.simple.spring.framework.beans.factory;

import com.chen.simple.spring.framework.beans.BeanDefinition;
import com.chen.simple.spring.framework.beans.BeanDefinitionRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public class DefaultListableBeanFactory implements BeanDefinitionRegistry {

    /**
     * 存储注册信息的BeanDefinition
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }
}
