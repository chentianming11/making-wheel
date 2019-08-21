package com.chen.yugong.framework.spring.beans.factory;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public interface BeanFactory {

    //如果需要得到工厂本身，需要转义
    String FACTORY_BEAN_PREFIX = "&";

    /**
     * 根据bean的名字，获取在IOC容器中得到bean实例
     * @param name
     * @return
     */
    Object getBean(String name);

    <T> T getBean(Class<T> clz);


    String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons);

}
