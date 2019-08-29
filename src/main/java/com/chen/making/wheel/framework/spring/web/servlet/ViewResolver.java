package com.chen.making.wheel.framework.spring.web.servlet;

import com.chen.making.wheel.framework.spring.web.mvc.View;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public interface ViewResolver {

    View resolveViewName(String viewName) throws Exception;
}
