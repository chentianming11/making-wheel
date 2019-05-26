package com.chen.simple.spring.framework.context;

import com.chen.simple.spring.framework.annotation.Autowired;
import com.chen.simple.spring.framework.beans.BeanDefinition;
import com.chen.simple.spring.framework.beans.BeanWrapper;
import com.chen.simple.spring.framework.beans.DefinitionReader;
import com.chen.simple.spring.framework.beans.factory.DefaultListableBeanFactory;
import com.chen.simple.spring.framework.beans.factory.ObjectFactory;
import com.chen.simple.spring.framework.beans.factory.config.Scope;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import strman.Strman;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public class  ApplicationContext extends AbstractApplicationContext {

    /**
     * 配置文件路径
     */
    private String[] configLocations;

    private DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    private DefinitionReader reader;

    private final Map<String, Scope> scopes = new LinkedHashMap<>(8);

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        refresh();
    }


    @Override
    public void refresh() {
        // 调用载入Bean定义的方法
        loadBeanDefinitions(beanFactory);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanFactory.getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanFactory.getBeanDefinitionNames();
    }

    /**
     * 载入Bean定义
     *
     * @param beanFactory
     */
    public void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // 创建Bean定义reader
        reader = new DefinitionReader(beanFactory);
        for (String configLocation : configLocations) {
            reader.loadBeanDefinitions(configLocation);
        }
    }

    public Properties getConfig() {
        return reader.getConfig();
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
                    return createBean(beanName, mbd);
                });
                //获取给定Bean的实例对象
                bean = sharedInstance;
            }
            //IOC容器创建原型模式Bean实例对象
            else if (mbd.isPrototype()) {
                // It's a prototype -> create a new instance.
                //原型模式(Prototype)是每次都会创建一个新的对象
                bean = createBean(beanName, mbd);
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
                bean = scope.get(beanName, () -> createBean(beanName, mbd));
            }

        }
        return bean;
    }

    @Override
    public <T> T getBean(Class<T> clz) {
        String simpleName = clz.getSimpleName();
        Object bean = getBean(Strman.lowerFirst(simpleName));
        return (T) bean;
    }

    private Object createBean(String beanName, BeanDefinition mbd) {
        //封装被创建的Bean对象
        BeanWrapper instanceWrapper = createBeanInstance(beanName, mbd);
        final Object bean = instanceWrapper.getWrappedInstance();
        //向容器中缓存单例模式的Bean对象，以防循环引用
        if (mbd.isSingleton()) {
            singletonObjects.put(beanName, bean);
            // 别名也存一份
            List<String> alias = mbd.getAlias();
            if (alias != null) {
                for (String alia : alias) {
                    singletonObjects.put(alia, bean);
                }
            }

        }

        //将Bean实例对象封装，并且Bean定义中配置的属性值赋值给实例对象
        populateBean(beanName, mbd, instanceWrapper);
        return bean;
    }


    private void populateBean(String beanName, BeanDefinition mbd, BeanWrapper instanceWrapper) {
        try {
            Class<?> wrappedClass = instanceWrapper.getWrappedClass();
            Field[] fields = FieldUtils.getFieldsWithAnnotation(wrappedClass, Autowired.class);
            if (ArrayUtils.isEmpty(fields)) {
                return;
            }
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                Object bean = getBean(fieldType);
                FieldUtils.writeField(field, instanceWrapper.getWrappedInstance(), bean, true);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    private BeanWrapper createBeanInstance(String beanName, BeanDefinition mbd) {
        BeanWrapper beanWrapper = null;
        try {
            String beanClassName = mbd.getBeanClassName();
            Class<?> clz = Class.forName(beanClassName);
            Object instance = ConstructorUtils.invokeConstructor(clz);
            beanWrapper = new BeanWrapper(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanWrapper;
    }


    private BeanDefinition getBeanDefinition(String beanName) {
        return beanFactory.getBeanDefinition(beanName);
    }

    private Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    private Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        // 创建前置和后置做一些逻辑 避免循环引用问题
        return singletonFactory.getObject();
    }
}
