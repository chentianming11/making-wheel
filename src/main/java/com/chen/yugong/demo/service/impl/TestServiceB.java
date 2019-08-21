package com.chen.yugong.demo.service.impl;

import com.chen.yugong.framework.spring.annotation.Autowired;
import com.chen.yugong.framework.spring.annotation.Service;

/**
 * @author 陈添明
 * @date 2019/6/4
 */
@Service
public class TestServiceB {


    @Autowired
    TestServiceA testServiceA;
    

    public void methodA() {
        System.out.println("TestServiceB ==> methodA");
        testServiceA.methodB();
    }

    public void methodB() {
        System.out.println("TestServiceB ===> methodB");
    }
}
