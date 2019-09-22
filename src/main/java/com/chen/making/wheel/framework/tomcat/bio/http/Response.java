package com.chen.making.wheel.framework.tomcat.bio.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 陈添明
 * @date 2019/9/22
 */
public class Response {

    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String data) {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("HTTP/1.1 200 OK\n")
                    .append("content-type: text/html;\n")
                    .append("\r\n")
                    .append(data);
            outputStream.write(buffer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
