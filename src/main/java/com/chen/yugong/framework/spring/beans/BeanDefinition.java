package com.chen.yugong.framework.spring.beans;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
@Data
@Accessors(chain = true)
public class BeanDefinition {

    private static final String SCOPE_DEFAULT = "";

    private static final String SCOPE_SINGLETON = "singleton";

    private static final String SCOPE_PROTOTYPE = "prototype";

    private Class<?> beanClass;

    private String factoryBeanName;

    private boolean lazyInit = false;

    private List<String> alias;

    private Class<?> sourceBeanClass;

    /**
     * this bean definition is 'synthetic', that is,
     * not defined by the application itself.
     */
    private boolean synthetic = false;

    private String scope = SCOPE_DEFAULT;
    private String initMethodName;

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
    }

    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    public String getInitMethodName() {
        return initMethodName;
    }
}
