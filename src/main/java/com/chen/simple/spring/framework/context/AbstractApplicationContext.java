package com.chen.simple.spring.framework.context;

import com.chen.simple.spring.framework.beans.factory.BeanFactory;
import com.chen.simple.spring.framework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public abstract class AbstractApplicationContext implements BeanFactory {

    /** BeanPostProcessors to apply in createBean */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    public abstract void refresh();


    public abstract int getBeanDefinitionCount();


    public abstract String[] getBeanDefinitionNames();

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {

    }

    /**
     * Return the list of BeanPostProcessors that will get applied
     * to beans created with this factory.
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }
}
