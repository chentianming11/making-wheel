package com.chen.making.wheel.framework.tomcat.bio.http;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author 陈添明
 * @date 2019/9/22
 */
public class Request {

    private String method;

    private String url;

    public Request(InputStream inputStream) {
        try {
            String content = IOUtils.toString(inputStream, Charset.defaultCharset());
            System.out.println("--------------------------");
            System.out.println(content);
            System.out.println("--------------------------");
            String line = content.split("\\n")[0];
            String[] arr = line.split("\\s");
            method = arr[0];
            url = arr[1].split("\\?")[0];
        } catch (IOException e) {
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
