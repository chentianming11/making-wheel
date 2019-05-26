package com.chen.simple.spring.framework.web.method;

import com.alibaba.fastjson.JSON;
import com.chen.simple.spring.framework.annotation.ResponseBody;
import com.chen.simple.spring.framework.http.HttpStatus;
import com.chen.simple.spring.framework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 处理响应体
 * @author 陈添明
 * @date 2019/5/26
 */
public class RequestResponseBodyMethodProcessor implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(Method handlerMethod) {
        return handlerMethod.isAnnotationPresent(ResponseBody.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, Method handlerMethod, ModelAndViewContainer mavContainer, HttpServletResponse response) throws IOException {
        mavContainer.setRequestHandled(true);
        String result = JSON.toJSONString(returnValue);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(result);
    }
}
