package com.chen.making.wheel.framework.tomcat.bio;

import com.chen.making.wheel.framework.tomcat.bio.http.Request;
import com.chen.making.wheel.framework.tomcat.bio.http.Response;
import com.chen.making.wheel.framework.tomcat.bio.servlet.Servlet;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author 陈添明
 * @date 2019/9/22
 */
public class TomcatBIO {


    private int port;

    private Map<String, Servlet> servletMapping = new HashMap<>();

    private Properties webxml = new Properties();

    public TomcatBIO(int port) {
        this.port = port;
        init();
    }

    private void init() {
        // 加载web.xml文件,同时初始化 ServletMapping对象
        try {
            String WEB_INF = this.getClass().getResource("/").getPath();
            FileInputStream fis = new FileInputStream(WEB_INF + "/tomcat/web.properties");
            webxml.load(fis);
            for (Object k : webxml.keySet()) {
                String key = k.toString();
                if (key.endsWith(".url")) {
                    String servletName = key.replaceAll("\\.url$", "");
                    String url = webxml.getProperty(key);
                    String className = webxml.getProperty(servletName + ".className");
                    Servlet obj = (Servlet) Class.forName(className).newInstance();
                    servletMapping.put(url, obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("tomcat已经启动，启动端口：" + port);
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("有一个新的客户端连接了。。。");
                process(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(Socket client) throws Exception {
        InputStream inputStream = client.getInputStream();
        Request request = new Request(inputStream);
        OutputStream outputStream = client.getOutputStream();
        Response response = new Response(outputStream);
        // 根据url匹配servlet
        String url =  request.getUrl();
        if (servletMapping.containsKey(url)) {
            Servlet servlet = servletMapping.get(url);
            servlet.service(request, response);
        } else {
            response.write("404 not found!");
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
        client.close();
    }


    public static void main(String[] args) {
        new TomcatBIO(8089).start();
    }
}
