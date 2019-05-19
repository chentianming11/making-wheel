package com.chen.simple.spring.framework.beans.factory;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public interface BeanFactory {

    /**
     * 根据bean的名字，获取在IOC容器中得到bean实例
     * @param name
     * @return
     */
    Object getBean(String name);

    <T> T getBean(Class<T> clz);

}
