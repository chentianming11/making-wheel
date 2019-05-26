package com.chen.simple.spring.framework.web.method;

import com.chen.simple.spring.framework.web.mvc.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 处理模型视图
 * @author 陈添明
 * @date 2019/5/26
 */
public class ModelAndViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(Method handlerMethod) {
        return handlerMethod.getReturnType().equals(ModelAndView.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, Method handlerMethod, ModelAndViewContainer mavContainer, HttpServletResponse response) {
        if (returnValue == null) {
            mavContainer.setRequestHandled(true);
            return;
        }
        ModelAndView mav = (ModelAndView) returnValue;
        mavContainer.setView(mav.getViewName());
        mavContainer.setStatus(mav.getStatus());
        mavContainer.setModel(mav.getModel());
    }
}
