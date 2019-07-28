package com.chen.simple.spring.framework.mybatis.type;

/**
 * @author 陈添明
 * @date 2019/7/28
 */
public class ResultMapException extends RuntimeException {


    public ResultMapException(String s, Exception e) {
        super(s, e);
    }
}
