package com.chen.simple.spring.demo.aspect;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
//@Service
//@Aspect
public class LogAspect {


    public void before(){
        //往对象里面记录调用的开始时间
        System.out.println("开始执行：start => " + System.currentTimeMillis());
    }

    public void afterReturning(){
        System.out.println("执行结束！！！");
    }



}
