package com.chen.making.wheel.framework.tomcat.bio.http;

import java.io.InputStream;

/**
 * @author 陈添明
 * @date 2019/9/22
 */
public class Request {

    private String method;

    private String url;

    public Request(InputStream inputStream) {
        try {
            System.out.println("--------------------------");
            byte[] buffer = new byte[1024];
            StringBuilder content = new StringBuilder();
            int len = inputStream.read(buffer);
            content.append(new String(buffer, 0, len));
            System.out.println(content);
            System.out.println("--------------------------");
            String line = content.toString().split("\\n")[0];
            String[] arr = line.split("\\s");
            method = arr[0];
            url = arr[1].split("\\?")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
