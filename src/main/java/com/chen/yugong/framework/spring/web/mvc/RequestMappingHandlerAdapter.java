package com.chen.yugong.framework.spring.web.mvc;

import com.chen.yugong.framework.spring.web.method.HandlerMethodReturnValueHandler;
import com.chen.yugong.framework.spring.web.method.ModelAndViewContainer;
import com.chen.yugong.framework.spring.web.method.ModelAndViewMethodReturnValueHandler;
import com.chen.yugong.framework.spring.web.method.RequestResponseBodyMethodProcessor;
import com.chen.yugong.framework.spring.web.servlet.HandlerMethod;
import com.chen.yugong.framework.spring.web.servlet.Param;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {

    /**
     * 方法返回值处理器
     * 源码是实例化bean的时候注入进去的，这里简单处理
     */
    private List<HandlerMethodReturnValueHandler> returnValueHandlers = ImmutableList.of(new ModelAndViewMethodReturnValueHandler(), new RequestResponseBodyMethodProcessor());

    @Override
    public boolean supports(Object handler) {
        if (handler instanceof HandlerMethod) {
            return true;
        }
        return false;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws InvocationTargetException, IllegalAccessException, IOException {
        return handleInternal(request, response, (HandlerMethod) handler);
    }


    private ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws InvocationTargetException, IllegalAccessException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        List<Param> paramList = handler.getParamList();
        Object[] args = new Object[paramList.size()];
        paramList.forEach(param -> {
            Class<?> type = param.getType();
            Object value;
            // 处理HttpServletRequest和HttpServletResponse参数
            if (type == HttpServletRequest.class) {
                value = request;

            } else if (type == HttpServletResponse.class) {
                value = response;
            } else {
                String[] strArray = parameterMap.get(param.getName());
                value = converter(strArray, param.getType());
            }
            args[param.getIndex()] = value;
        });
        Method method = handler.getMethod();
        Object controller = handler.getController();
        // 反射调用
        Object result = method.invoke(controller, args);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        handleReturnValueObject(result, method, mavContainer, response);
        return getModelAndView(mavContainer);
    }


    /**
     * 获取ModelAndView
     * @param mavContainer
     * @return
     */
    private ModelAndView getModelAndView(ModelAndViewContainer mavContainer){
        if (mavContainer.isRequestHandled()) {
            return null;
        }
        ModelAndView mav = new ModelAndView(mavContainer.getView(), mavContainer.getModel(), mavContainer.getStatus());
        return mav;
    }

    /**
     * 处理返回值
     */
    public void handleReturnValueObject(Object returnValue, Method handlerMethod, ModelAndViewContainer mavContainer, HttpServletResponse response) throws IOException {
        HandlerMethodReturnValueHandler handler = selectHandler(handlerMethod);
        if (handler == null) {
            throw new IllegalArgumentException("Unknown return value type: " + handlerMethod.getReturnType().getTypeName());
        }
        handler.handleReturnValue(returnValue, handlerMethod, mavContainer, response);
    }

    /**
     * 选择返回值处理器
     * @return
     */
    private HandlerMethodReturnValueHandler selectHandler(Method handlerMethod) {
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            if (handler.supportsReturnType(handlerMethod)) {
                return handler;
            }
        }
        return null;

    }


    /**
     * 将字符串数组转换成给定的类型
     *
     * @param strArray
     * @param type
     */
    private Object converter(String[] strArray, Class<?> type) {

        if (strArray == null || strArray.length == 0) {
            return null;
        }

        if (type == String.class) {
            return Joiner.on(",").join(strArray);
        }
        String first = strArray[0];
        if (type == Integer.class) {
            return Integer.valueOf(first);
        }
        if (type == Long.class) {
            return Long.valueOf(first);
        }
        if (type == Double.class) {
            return Double.valueOf(first);
        }
        // ....其他转换

        return null;
    }
}
