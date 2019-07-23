package com.chen.simple.spring.framework.spring.context;

import com.chen.simple.spring.framework.spring.beans.factory.BeanFactory;
import com.chen.simple.spring.framework.spring.beans.factory.DefaultListableBeanFactory;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public abstract class AbstractApplicationContext implements BeanFactory {



    public void refresh() {

        // 告诉子类启动refreshBeanFactory()方法，Bean定义资源文件的载入从
        //子类的refreshBeanFactory()方法启动
        DefaultListableBeanFactory  beanFactory = obtainFreshBeanFactory();

        // Register bean processors that intercept bean creation.
        //6、为BeanFactory注册BeanPost事件处理器.
        //BeanPostProcessor是Bean后置处理器，用于监听容器触发的事件
        registerBeanPostProcessors(beanFactory);

    }

    protected abstract void registerBeanPostProcessors(DefaultListableBeanFactory beanFactory);

    protected abstract DefaultListableBeanFactory obtainFreshBeanFactory();



    public abstract int getBeanDefinitionCount();


    public abstract String[] getBeanDefinitionNames();


}
