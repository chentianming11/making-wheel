package com.chen.simple.spring.framework.web.servlet;

import com.chen.simple.spring.framework.web.mvc.View;

import java.io.File;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public class CustomViewResolver implements ViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";
    private File templateRootDir;

    public CustomViewResolver(File templateRootDir) {
        this.templateRootDir = templateRootDir;
    }

    @Override
    public View resolveViewName(String viewName) {
        if(null == viewName || "".equals(viewName.trim())){ return null;}
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName +
                DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+",
                "/"));
        return new View(templateFile);
    }
}
