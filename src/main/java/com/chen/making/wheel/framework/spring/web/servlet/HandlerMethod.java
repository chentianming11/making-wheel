package com.chen.making.wheel.framework.spring.web.servlet;

import com.chen.making.wheel.framework.spring.annotation.RequestParam;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 陈添明
 * @date 2019/4/21
 */
@Data
@Accessors(chain = true)
public class HandlerMethod {

    private Object controller;

    private Method method;

    private String url;

    private List<Param> paramList = new ArrayList<>();

    public HandlerMethod(Object controller, Method method, String url) {
        this.controller = controller;
        this.method = method;
        this.url = url;
        initParamList(method);
    }


    /**
     * 初始化参数列表
     * @param method
     */
    private void initParamList(Method method) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String name = parameter.getName();
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if (Objects.nonNull(requestParam) && !StringUtils.isBlank(requestParam.value())) {
                name = requestParam.value();
            }
            Param param = new Param(name, i, parameter.getType());
            paramList.add(param);
        }
    }

}
