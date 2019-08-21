package com.chen.yugong.framework.spring.aop.autoproxy;

import com.chen.yugong.framework.spring.aop.ProxyFactory;
import com.chen.yugong.framework.spring.aop.adapter.AfterReturningAdviceInterceptor;
import com.chen.yugong.framework.spring.aop.adapter.MethodBeforeAdviceInterceptor;
import com.chen.yugong.framework.spring.aop.advice.AfterReturningAdvice;
import com.chen.yugong.framework.spring.aop.advice.MethodBeforeAdvice;
import com.chen.yugong.framework.spring.beans.FactoryBean;
import com.chen.yugong.framework.spring.beans.factory.BeanFactory;
import com.chen.yugong.framework.spring.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 陈添明
 * @date 2019/6/1
 */
public class CustomAutoProxyCreator implements SmartInstantiationAwareBeanPostProcessor {

    private Properties config;

    private final Set<Object> earlyProxyReferences = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    private final Map<Object, Boolean> advisedBeans = new ConcurrentHashMap<>(256);

    public CustomAutoProxyCreator(Properties config) {
        this.config = config;
    }

    private Pattern pointCutClassPattern;

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

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        Object cacheKey = getCacheKey(bean.getClass(), beanName);
        if (!this.earlyProxyReferences.contains(cacheKey)) {
            this.earlyProxyReferences.add(cacheKey);
        }
        return wrapIfNecessary(bean, beanName, cacheKey);
    }

    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {

        // 判断是否不应该代理这个bean
        if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
            return bean;
        }

        // Create proxy if we have advice.
        // 获取这个 bean 的 advice
        List<Object> specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, bean);
        if (specificInterceptors != null) {
            this.advisedBeans.put(cacheKey, Boolean.TRUE);
            // // 创建代理
            Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors, bean);
            return proxy;
        }
        this.advisedBeans.put(cacheKey, Boolean.FALSE);
        return bean;

    }

    protected Object getCacheKey(Class<?> beanClass, String beanName) {
        if (StringUtils.isNotBlank(beanName)) {
            return (FactoryBean.class.isAssignableFrom(beanClass) ?
                    BeanFactory.FACTORY_BEAN_PREFIX + beanName : beanName);
        } else {
            return beanClass;
        }
    }

    protected Object createProxy(Class<?> beanClass, String beanName, List<Object> specificInterceptors, Object target) {
        ProxyFactory proxyFactory = new ProxyFactory();
        // 设置代理参数
        proxyFactory.setTarget(target);
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(beanClass);
        proxyFactory.setInterfaces(interfaces);
        proxyFactory.setSpecificInterceptors(specificInterceptors);
        return proxyFactory.getProxy();
    }

    /**
     * 获取当前Bean上面的通知
     *
     * @param beanClass
     * @param beanName
     * @param target
     * @return
     */
    @SneakyThrows
    protected List<Object> getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, Object target) {
        // 源码中通过pointCut表达式解析匹配实现，太复杂，这里简化处理
        String pointCut = config.getProperty("pointCut")
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");

        Class<?> targetClass = target.getClass();
        //玩正则
        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(
                pointCutForClassRegex.lastIndexOf(" ") + 1));
        // 切点表达式没有匹配到当前对象，返回null
        if (!pointCutClassPattern.matcher(targetClass.toString()).matches()) {
            return null;
        }

        String aspectBefore = config.getProperty("aspectBefore");
        String aspectAfterReturning = config.getProperty("aspectAfterReturning");

        Pattern pattern = Pattern.compile(pointCut);
        Class aspectClass = Class.forName(config.getProperty("aspectClass"));
        Object aspectInstance = aspectClass.newInstance();

        //执行器链
        List<Object> advisors = new ArrayList<>();

        for (Method m : targetClass.getMethods()) {
            String methodString = m.toString();
            if (methodString.contains("throws")) {
                methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
            }
            Matcher matcher = pattern.matcher(methodString);
            if (matcher.matches()) {
                //把每一个方法包装成 MethodIterceptor
                //before
                if (StringUtils.isNotBlank(aspectBefore)) {
                    //创建一个Advivce
                    MethodBeforeAdvice methodBeforeAdvice = new MethodBeforeAdvice(aspectInstance, aspectClass.getMethod(aspectBefore, null));
                    advisors.add(new MethodBeforeAdviceInterceptor(methodBeforeAdvice));
                }
                //before
                if (StringUtils.isNotBlank(aspectAfterReturning)) {
                    //创建一个Advivce
                    AfterReturningAdvice afterReturningAdvice = new AfterReturningAdvice(aspectInstance, aspectClass.getMethod(aspectAfterReturning, null));
                    advisors.add(new AfterReturningAdviceInterceptor(afterReturningAdvice));
                }
            }

        }
        return advisors;
    }


}
