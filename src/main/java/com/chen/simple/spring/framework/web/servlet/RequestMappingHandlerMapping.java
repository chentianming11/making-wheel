package com.chen.simple.spring.framework.web.servlet;

import com.chen.simple.spring.framework.annotation.Controller;
import com.chen.simple.spring.framework.annotation.RequestMapping;
import com.chen.simple.spring.framework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public class RequestMappingHandlerMapping implements HandlerMapping {

    private ApplicationContext context;

    private List<HandlerMethod> handlerMethodList = new ArrayList<>();

    public RequestMappingHandlerMapping(ApplicationContext context) {
        this.context = context;
        init();
    }

    private void init() {
        //首先从容器中取到所有的实例
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object controller = context.getBean(beanName);
            Class<?> clazz = controller.getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }
            String baseUrl = "/";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = requestMapping.value() + "/";
            }
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                    String url = (baseUrl + methodRequestMapping.value()).replaceAll("/+", "/");
                    handlerMethodList.add(new HandlerMethod(controller, method, url));
                    System.out.println("Mapped :" + url + "," + method);
                }
            }
        }
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) {
        HandlerMethod handler = getHandlerInternal(request);
        HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain(handler);
        return handlerExecutionChain;
    }

    private HandlerMethod getHandlerInternal(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestURI.replaceAll(contextPath, "").replaceAll("/+", "/");
        for (HandlerMethod handlerMethod : handlerMethodList) {
            if (Objects.equals(url, handlerMethod.getUrl())) {
                return handlerMethod;
            }
        }
        return null;
    }
}
