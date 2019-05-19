package com.chen.simple.spring.framework.web.servlet;

import com.chen.simple.spring.framework.web.mvc.View;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public interface ViewResolver {

    View resolveViewName(String viewName) throws Exception;
}
