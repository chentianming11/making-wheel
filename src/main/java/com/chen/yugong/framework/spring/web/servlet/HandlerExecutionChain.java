package com.chen.yugong.framework.spring.web.servlet;

import lombok.Data;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
@Data
public class HandlerExecutionChain {


    private final Object handler;


    public HandlerExecutionChain(Object handler) {
        this.handler = handler;
    }
}
