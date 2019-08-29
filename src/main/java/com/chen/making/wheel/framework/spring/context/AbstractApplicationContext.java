package com.chen.making.wheel.framework.spring.context;


import com.chen.making.wheel.framework.spring.beans.factory.BeanFactory;
import com.chen.making.wheel.framework.spring.beans.factory.DefaultListableBeanFactory;
import com.chen.making.wheel.framework.spring.beans.factory.config.BeanFactoryPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public abstract class AbstractApplicationContext implements BeanFactory {

    /** BeanFactoryPostProcessors to apply on refresh */
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();


    public void refresh() {

        //2、 告诉子类启动refreshBeanFactory()方法，Bean定义资源文件的载入从
        //子类的refreshBeanFactory()方法启动
        DefaultListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // Allows post-processing of the bean factory in context subclasses.
        //4、为容器的某些子类指定特殊的BeanPost事件处理器
        postProcessBeanFactory(beanFactory);

        // Invoke factory processors registered as beans in the context.
        //5、调用所有注册的BeanFactoryPostProcessor的Bean
        invokeBeanFactoryPostProcessors(beanFactory);

        // Register bean processors that intercept bean creation.
        //6、为BeanFactory注册BeanPost事件处理器.
        //BeanPostProcessor是Bean后置处理器，用于监听容器触发的事件
        registerBeanPostProcessors(beanFactory);

    }


    /**
     * 为容器的某些子类指定特殊的BeanPost事件处理器
     * @param beanFactory
     */
    protected abstract void postProcessBeanFactory(DefaultListableBeanFactory beanFactory);


    /**
     * 调用所有注册的BeanFactoryPostProcessor的Bean
     * @param beanFactory
     */
    protected abstract void invokeBeanFactoryPostProcessors(DefaultListableBeanFactory beanFactory);

    /**
     * 为BeanFactory注册BeanPost事件处理器.
     * @param beanFactory
     */
    protected abstract void registerBeanPostProcessors(DefaultListableBeanFactory beanFactory);

    protected abstract DefaultListableBeanFactory obtainFreshBeanFactory();



    public abstract int getBeanDefinitionCount();


    public abstract String[] getBeanDefinitionNames();

    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
    }


    /**
     * Return the list of BeanFactoryPostProcessors that will get applied
     * to the internal BeanFactory.
     */
    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

}
