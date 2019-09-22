package com.chen.making.wheel.framework.tomcat.netty.servlet;


import com.chen.making.wheel.framework.tomcat.netty.http.Request;
import com.chen.making.wheel.framework.tomcat.netty.http.Response;

public class SecondServlet extends Servlet {

	@Override
	public void doGet(Request request, Response response) throws Exception {
		this.doPost(request, response);
	}

	@Override
	public void doPost(Request request, Response response) throws Exception {
		response.write("This is Second Serlvet");
	}

}
