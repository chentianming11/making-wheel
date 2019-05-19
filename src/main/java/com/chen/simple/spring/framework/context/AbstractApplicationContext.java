package com.chen.simple.spring.framework.context;

import com.chen.simple.spring.framework.beans.factory.BeanFactory;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public abstract class AbstractApplicationContext implements BeanFactory {


    public abstract void refresh();


    public abstract int getBeanDefinitionCount();


    public abstract String[] getBeanDefinitionNames();

}
