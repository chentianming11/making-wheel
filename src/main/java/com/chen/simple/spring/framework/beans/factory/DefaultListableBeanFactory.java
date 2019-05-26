package com.chen.simple.spring.framework.beans.factory;

import com.chen.simple.spring.framework.beans.BeanDefinition;
import com.chen.simple.spring.framework.beans.BeanDefinitionRegistry;
import lombok.Getter;

import java.util.List;
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
    @Getter
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
        // 以别名也存一份
        List<String> alias = beanDefinition.getAlias();
        if (alias == null) {
            return;
        }
        for (String alia : alias) {
            beanDefinitionMap.put(alia, beanDefinition);
        }
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[beanDefinitionMap.size()]);
    }

}
