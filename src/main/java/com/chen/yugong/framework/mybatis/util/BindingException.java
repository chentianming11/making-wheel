package com.chen.yugong.framework.mybatis.util;

/**
 * @author 陈添明
 * @date 2019/7/28
 */
public class BindingException extends RuntimeException {
    public BindingException(String s) {
        super(s);
    }

    public BindingException(String s, Exception e) {
        super(s, e);
    }
}
