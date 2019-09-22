package com.chen.making.wheel.framework.tomcat.bio.servlet;

import com.chen.making.wheel.framework.tomcat.bio.http.Request;
import com.chen.making.wheel.framework.tomcat.bio.http.Response;

public class FirstServlet extends Servlet {

	@Override
	public void doGet(Request request, Response response) throws Exception {
		this.doPost(request, response);
	}

	@Override
	public void doPost(Request request, Response response) throws Exception {
		response.write("This is First Serlvet");
	}

}
