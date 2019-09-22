package com.chen.making.wheel.framework.tomcat.netty.servlet;

import com.chen.making.wheel.framework.tomcat.netty.http.Request;
import com.chen.making.wheel.framework.tomcat.netty.http.Response;

/**
 * @author 陈添明
 * @date 2019/9/22
 */
public abstract class Servlet {

    public void service(Request request, Response response) throws Exception{
        //由service方法来决定，是调用doGet或者调用doPost
        if("GET".equalsIgnoreCase(request.getMethod())){
            doGet(request, response);
        }else{
            doPost(request, response);
        }

    }

    public abstract void doGet(Request request,Response response) throws Exception;

    public abstract void doPost(Request request,Response response) throws Exception;
}
