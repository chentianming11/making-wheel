package com.chen.simple.spring.framework.web.mvc;

import com.chen.simple.spring.framework.web.servlet.HandlerMethod;
import com.chen.simple.spring.framework.web.servlet.Param;
import com.google.common.base.Joiner;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {


    @Override
    public boolean supports(Object handler) {
        if (handler instanceof HandlerMethod) {
            return true;
        }
        return false;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return handleInternal(request, response, (HandlerMethod) handler);
    }

    @SneakyThrows
    private ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        List<Param> paramList = handler.getParamList();
        Object[] args = new Object[paramList.size()];
        paramList.forEach(param -> {
            Class<?> type = param.getType();
            Object value;
            // 处理HttpServletRequest和HttpServletResponse参数
            if (type == HttpServletRequest.class) {
                value = request;

            }else if (type == HttpServletResponse.class) {
                value = response;
            } else {
                String[] strArray = parameterMap.get(param.getName());
                value = converter(strArray, param.getType());
            }
            args[param.getIndex()] = value;
        });

        Method method = handler.getMethod();
        Object controller = handler.getController();
        Object result = method.invoke(controller, args);

        if(result == null){ return null; }
        if (method.getReturnType() == ModelAndView.class) {
            return (ModelAndView) result;
        }
        return null;
    }


    /**
     * 将字符串数组转换成给定的类型
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
