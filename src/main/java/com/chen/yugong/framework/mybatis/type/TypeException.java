package com.chen.yugong.framework.mybatis.type;

/**
 * @author 陈添明
 * @date 2019/7/28
 */
public class TypeException extends RuntimeException {

    public TypeException(String s, Exception e) {
        super(s, e);
    }

    public TypeException(String s) {
        super(s);
    }
}
