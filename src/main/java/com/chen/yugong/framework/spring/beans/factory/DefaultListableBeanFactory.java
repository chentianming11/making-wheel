package com.chen.yugong.framework.spring.beans.factory;

import com.chen.yugong.framework.mybatis.spring.MapperFactoryBean;
import com.chen.yugong.framework.mybatis.spring.SqlSessionFactoryBean;
import com.chen.yugong.framework.spring.annotation.Autowired;
import com.chen.yugong.framework.spring.aop.AopProxy;
import com.chen.yugong.framework.spring.beans.BeanDefinition;
import com.chen.yugong.framework.spring.beans.BeanDefinitionRegistry;
import com.chen.yugong.framework.spring.beans.BeanWrapper;
import com.chen.yugong.framework.spring.beans.FactoryBean;
import com.chen.yugong.framework.spring.beans.factory.config.BeanPostProcessor;
import com.chen.yugong.framework.spring.beans.factory.config.Scope;
import com.chen.yugong.framework.spring.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import strman.Strman;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
@Getter
public class DefaultListableBeanFactory implements BeanDefinitionRegistry, BeanFactory {

    /**
     * 存储注册信息的BeanDefinition
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);


    /**
     * BeanPostProcessors to apply in createBean
     */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final Map<String, Scope> scopes = new LinkedHashMap<>(8);

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);


    /**
     * 添加Bean后置处理器
     *
     * @param beanPostProcessor
     */
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition != null) {
            return beanDefinition;
        }

        Set<Map.Entry<String, BeanDefinition>> entries = beanDefinitionMap.entrySet();
        for (Map.Entry<String, BeanDefinition> entry : entries) {
            BeanDefinition bd = entry.getValue();
            List<String> alias = bd.getAlias();
            if (alias.contains(beanName)) {
                return bd;
            }
        }
        return null;

    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[beanDefinitionMap.size()]);
    }

    /**
     * 根据bean的名字，获取在IOC容器中得到bean实例
     *
     * @param name
     * @return
     */
    @Override
    public Object getBean(String name) {
        final String beanName = name;
        Object bean;
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null) {
            bean = sharedInstance;
        } else {
            final BeanDefinition mbd = getBeanDefinition(beanName);
            //创建单例模式Bean的实例对象
            if (mbd.isSingleton()) {
                //这里使用了一个匿名内部类，创建Bean实例对象，并且注册给所依赖的对象
                sharedInstance = getSingleton(beanName, () -> {
                    //创建一个指定Bean实例对象，如果有父级继承，则合并子类和父类的定义
                    return createBean(mbd);
                });
                //获取给定Bean的实例对象
                bean = sharedInstance;
            }
            //IOC容器创建原型模式Bean实例对象
            else if (mbd.isPrototype()) {
                // It's a prototype -> create a new instance.
                //原型模式(Prototype)是每次都会创建一个新的对象
                bean = createBean(mbd);
            }

            //要创建的Bean既不是单例模式，也不是原型模式，则根据Bean定义资源中
            //配置的生命周期范围，选择实例化Bean的合适方法，这种在Web应用程序中
            //比较常用，如：request、session、application等生命周期
            else {
                String scopeName = mbd.getScope();
                final Scope scope = this.scopes.get(scopeName);
                //Bean定义资源中没有配置生命周期范围，则Bean定义不合法
                if (scope == null) {
                    throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
                }
                bean = scope.get(beanName, () -> createBean(mbd));
            }

        }
        return bean;
    }


    private Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    private Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        // 创建前置和后置做一些逻辑 避免循环引用问题
        return singletonFactory.getObject();
    }

    private Object createBean(BeanDefinition mbd) {
        String beanName = mbd.getFactoryBeanName();
        //封装被创建的Bean对象
        BeanWrapper instanceWrapper = createBeanInstance(beanName, mbd);
        final Object bean = instanceWrapper.getWrappedInstance();
        //向容器中缓存单例模式的Bean对象，以防循环引用
        if (mbd.isSingleton()) {
            singletonObjects.put(beanName, bean);
            // 别名也存一份
            List<String> alias = mbd.getAlias();
            if (alias != null) {
                for (String ali : alias) {
                    singletonObjects.put(ali, bean);
                }
            }
        }
        Object exposedObject = bean;
        //DI注入 将Bean实例对象封装，并且Bean定义中配置的属性值赋值给实例对象
        populateBean(beanName, mbd, instanceWrapper);
        // 初始化Bean
        exposedObject = initializeBean(beanName, exposedObject, mbd);

        // 如果是工厂Bean，返回getObject()
        if (exposedObject instanceof FactoryBean) {
            try {

                /**
                 * 特殊处理，置入mybatis配置Bean的属性
                 * 没办法，要不然还得把配置化Bean的高级装配实现一遍，太麻烦了
                 */
                if (exposedObject instanceof MapperFactoryBean) {
                    MapperFactoryBean mapperFactoryBean = (MapperFactoryBean) exposedObject;
                    mapperFactoryBean.setMapperInterface(mbd.getSourceBeanClass());
                    SqlSessionFactoryBean sqlSessionFactoryBean = getBean(SqlSessionFactoryBean.class);
                    mapperFactoryBean.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
                }

                return ((FactoryBean) exposedObject).getObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return exposedObject;
    }


    /**
     * 初始容器创建的Bean实例对象，为其添加BeanPostProcessor后置处理器
     *
     * @param beanName
     * @param mbd
     * @return
     */
    private Object initializeBean(String beanName, Object bean, BeanDefinition mbd) {
        Object wrappedBean = bean;
        //对BeanPostProcessor后置处理器的postProcessBeforeInitialization
        //回调方法的调用，为Bean实例初始化前做一些处理
        if (mbd == null || !mbd.isSynthetic()) {
            wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
        }

        //调用Bean实例对象初始化的方法，这个初始化方法是在Spring Bean定义配置
        //文件中通过init-method属性指定的
        try {
            invokeInitMethods(beanName, wrappedBean, mbd);
        } catch (Throwable ex) {
            throw new RuntimeException("Invocation of init method failed", ex);
        }
        //对BeanPostProcessor后置处理器的postProcessAfterInitialization
        //回调方法的调用，为Bean实例初始化之后做一些处理
        if (mbd == null || !mbd.isSynthetic()) {
            wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        }

        return wrappedBean;
    }

    /**
     * 调用Bean实例对象初始化的方法，这个初始化方法是在Spring Bean定义配置
     *
     * @param beanName
     * @param mbd
     */
    private void invokeInitMethods(String beanName, Object bean, BeanDefinition mbd) throws Exception {
        boolean isInitializingBean = (bean instanceof InitializingBean);
        if (isInitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }

        if (mbd != null) {
            String initMethodName = mbd.getInitMethodName();
            if (StringUtils.isBlank(initMethodName)) {
                return;
            }
            // 调用自定义init方法
            MethodUtils.invokeExactMethod(bean, initMethodName);
        }
    }

    /**
     * 调用BeanPostProcessor后置处理器实例对象初始化之后的处理方法
     *
     * @param beanName
     * @return
     */
    private Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        //遍历容器为所创建的Bean添加的所有BeanPostProcessor后置处理器
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            //调用Bean实例所有的后置处理中的初始化后处理方法，为Bean实例对象在
            //初始化之后做一些自定义的处理操作
            Object current = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 调用BeanPostProcessor后置处理器实例对象初始化之前的处理方法
     *
     * @param beanName
     * @return
     */
    private Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        //遍历容器为所创建的Bean添加的所有BeanPostProcessor后置处理器
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            //调用Bean实例所有的后置处理中的初始化前处理方法，为Bean实例对象在
            //初始化之前做一些自定义的处理操作
            Object current = beanProcessor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }


    /**
     * bean依赖注入
     *
     * @param beanName
     * @param mbd
     * @param instanceWrapper
     */
    private void populateBean(String beanName, BeanDefinition mbd, BeanWrapper instanceWrapper) {
        try {
            Class<?> beanClass = instanceWrapper.getWrappedClass();
            Object instance = instanceWrapper.getWrappedInstance();
            // 注入这里直接通过反射简单处理
            if (Proxy.isProxyClass(beanClass)) {
                // 当前bean是代理对象
                AopProxy aopProxy = (AopProxy) Proxy.getInvocationHandler(instance);
                instance = aopProxy.getTarget();
                beanClass = instance.getClass();
            }

            Field[] fields = FieldUtils.getFieldsWithAnnotation(beanClass, Autowired.class);
            if (ArrayUtils.isEmpty(fields)) {
                return;
            }
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                Object bean = getBean(fieldType);
                field.setAccessible(true);
                field.set(instance, bean);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    private BeanWrapper createBeanInstance(String beanName, BeanDefinition mbd) {
        BeanWrapper beanWrapper = null;
        try {
            Class<?> clz = mbd.getBeanClass();
            Object bean = ConstructorUtils.invokeConstructor(clz);
            Object exposedObject = getEarlyBeanReference(beanName, mbd, bean);
            beanWrapper = new BeanWrapper(exposedObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanWrapper;
    }


    /**
     * Obtain a reference for early access to the specified bean,
     * typically for the purpose of resolving a circular reference.
     * @param beanName the name of the bean (for error handling purposes)
     * @param mbd the merged bean definition for the bean
     * @param bean the raw bean instance
     * @return the object to expose as bean reference
     */
    private Object getEarlyBeanReference(String beanName, BeanDefinition mbd, Object bean) {
        Object exposedObject = bean;
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
                SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor) bp;
                exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
            }
        }
        return exposedObject;
    }


    @Override
    public <T> T getBean(Class<T> clz) {
        String simpleName = clz.getSimpleName();
        Object bean = getBean(Strman.lowerFirst(simpleName));
        return (T) bean;
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons) {
        List<String> names = new ArrayList<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Class<?> beanClass = beanDefinition.getBeanClass();
            if (!type.isAssignableFrom(beanClass)) {
                return;
            }
            boolean singleton = beanDefinition.isSingleton();
            if (!includeNonSingletons && singleton) {
                return;
            }
            names.add(beanName);
        });
        return names.toArray(new String[names.size()]);
    }
}
