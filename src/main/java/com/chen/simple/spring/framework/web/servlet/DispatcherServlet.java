package com.chen.simple.spring.framework.web.servlet;

import com.chen.simple.spring.framework.context.ApplicationContext;
import com.chen.simple.spring.framework.web.mvc.HandlerAdapter;
import com.chen.simple.spring.framework.web.mvc.ModelAndView;
import com.chen.simple.spring.framework.web.mvc.RequestMappingHandlerAdapter;
import com.chen.simple.spring.framework.web.mvc.View;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public class DispatcherServlet extends HttpServlet {


    private ApplicationContext applicationContext;

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    private List<ViewResolver> viewResolvers = new ArrayList<>();


    /**
     * 销毁方法
     */
    @Override
    public void destroy() {

    }

    /**
     * get方法处理
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }


    /**
     * post方法处理
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //6、调用，运行阶段
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exection,Detail : " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException {
        HandlerExecutionChain mappedHandler = getHandler(req);
        // 如果handler为空,则返回404
        if (mappedHandler == null) {
            return;
        }
        // 获取处理request的处理器适配器handler adapter
        HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
        // 实际的处理器处理请求,返回结果视图对象
        ModelAndView mv = ha.handle(req, resp, mappedHandler.getHandler());
        // 处理结果
        processDispatchResult(req, resp, mappedHandler, mv);
    }

    @SneakyThrows
    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, HandlerExecutionChain mappedHandler, ModelAndView mv) {
        if (null == mv) {
            return;
        }
        if (this.viewResolvers.isEmpty()) {
            return;
        }
        String viewName = mv.getViewName();
        View view = resolveViewName(viewName, mv.getModelInternal(), req);
        view.render(mv.getModelInternal(), req, resp);
    }

    @SneakyThrows
    private View resolveViewName(String viewName, Map<String, Object> modelInternal, HttpServletRequest req) {
        if (this.viewResolvers != null) {
            for (ViewResolver viewResolver : this.viewResolvers) {
                View view = viewResolver.resolveViewName(viewName);
                if (view != null) {
                    return view;
                }
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter ha : this.handlerAdapters) {
            if (ha.supports(handler)) {
                return ha;
            }
        }
        throw new RuntimeException("HandlerAdapter not found");
    }

    private HandlerExecutionChain getHandler(HttpServletRequest req) {
        for (HandlerMapping hm : this.handlerMappings) {
            HandlerExecutionChain handler = hm.getHandler(req);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }


    /**
     * init方法，Servlet容器启动的入口
     */
    @Override
    public void init() {
        // 加载配置信息
        String contextConfigLocation = getInitParameter("contextConfigLocation");
        // 初始化容器
        applicationContext = new ApplicationContext(contextConfigLocation);
        // 初始化mvc九大组件
        initStrategies(applicationContext);

    }


    //初始化策略
    protected void initStrategies(ApplicationContext context) {
        //多文件上传的组件
        initMultipartResolver(context);
        //初始化本地语言环境
        initLocaleResolver(context);
        //初始化模板处理器
        initThemeResolver(context);
        //handlerMapping
        initHandlerMappings(context);
        //初始化参数适配器
        initHandlerAdapters(context);
        //初始化异常拦截器
        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);
        //初始化视图转换器
        initViewResolvers(context);
        // FlashMap-重定向传值
        initFlashMapManager(context);
    }

    private void initHandlerMappings(ApplicationContext context) {
        /**
         * 源码实际从容器中获取。
         * 这里简化处理，直接放一个实例进去
         */
        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping(context);
        handlerMappings.add(handlerMapping);


    }

    private void initHandlerAdapters(ApplicationContext context) {
        /**
         * 源码实际从容器中获取。
         * 这里简化处理，直接放一个实例进去
         */
        HandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapters.add(handlerAdapter);

    }

    private void initViewResolvers(ApplicationContext context) {
        //在页面敲一个 http://localhost/first.html
        //解决页面名字和模板文件关联的问题
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        this.viewResolvers.add(new CustomViewResolver(templateRootDir));
    }


    private void initFlashMapManager(ApplicationContext context) {

    }

    private void initRequestToViewNameTranslator(ApplicationContext context) {

    }

    private void initHandlerExceptionResolvers(ApplicationContext context) {

    }

    private void initThemeResolver(ApplicationContext context) {

    }

    private void initLocaleResolver(ApplicationContext context) {

    }

    private void initMultipartResolver(ApplicationContext context) {

    }
}
