package com.chen.simple.spring.framework.aop.autoproxy;

import com.chen.simple.spring.framework.aop.ProxyFactory;
import com.chen.simple.spring.framework.beans.FactoryBean;
import com.chen.simple.spring.framework.beans.factory.BeanFactory;
import com.chen.simple.spring.framework.beans.factory.config.BeanPostProcessor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 陈添明
 * @date 2019/6/1
 */
public abstract class AbstractAutoProxyCreator implements BeanPostProcessor{

    private final Set<Object> earlyProxyReferences = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    private final Map<Object, Boolean> advisedBeans = new ConcurrentHashMap<>(256);

    /**
     * Create a proxy with the configured interceptors if the bean is
     * identified as one to proxy by the subclass.
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean != null) {
            Object cacheKey = getCacheKey(bean.getClass(), beanName);
            if (!this.earlyProxyReferences.contains(cacheKey)) {
                return wrapIfNecessary(bean, beanName, cacheKey);
            }
        }
        return bean;
    }

    protected Object getCacheKey(Class<?> beanClass, String beanName) {
        if (!StringUtils.isNotBlank(beanName)) {
            return (FactoryBean.class.isAssignableFrom(beanClass) ?
                    BeanFactory.FACTORY_BEAN_PREFIX + beanName : beanName);
        }
        else {
            return beanClass;
        }
    }

    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {

        // 判断是否不应该代理这个bean
        if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
            return bean;
        }

        // Create proxy if we have advice.
        // 获取这个 bean 的 advice
        Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, bean);
        if (specificInterceptors != null) {
            this.advisedBeans.put(cacheKey, Boolean.TRUE);
            // // 创建代理
            Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors, bean);
            return proxy;
        }

        this.advisedBeans.put(cacheKey, Boolean.FALSE);
        return bean;
    }

    protected Object createProxy(Class<?> beanClass, String beanName, Object[] specificInterceptors, Object target) {
        ProxyFactory proxyFactory = new ProxyFactory();
        // 设置代理参数
        proxyFactory.setTarget(target);
        proxyFactory.setInterfaces(Arrays.asList(target.getClass().getInterfaces()));

        return proxyFactory.getProxy();
    }

    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, Object target) {
       return null;
    }
}
