package com.chen.making.wheel.framework.spring.aop;

import com.chen.making.wheel.framework.spring.aop.intercepter.Advice;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈添明
 * @date 2019/6/1
 */
@Data
public class AdvisedSupport implements Advice {

    private List<Class<?>> interfaces = new ArrayList<>();

    private  List<Object> specificInterceptors = new ArrayList<>();

    private Object target;


    public Class<?>[] getProxiedInterfaces() {
        return this.interfaces.toArray(new Class<?>[this.interfaces.size()]);
    }


    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        return specificInterceptors;
    }
}
