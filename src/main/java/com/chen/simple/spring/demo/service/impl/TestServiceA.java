package com.chen.simple.spring.demo.service.impl;

import com.chen.simple.spring.framework.annotation.Autowired;
import com.chen.simple.spring.framework.annotation.Service;

/**
 * @author 陈添明
 * @date 2019/6/4
 */
@Service
public class TestServiceA {


    @Autowired
    TestServiceB testServiceB;


    public void methodA() {
        System.out.println("methodA");
    }

    public void methodB() {
        System.out.println("methodB");
        testServiceB.methodB();
    }
}
