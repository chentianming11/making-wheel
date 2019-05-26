package com.chen.simple.spring.demo.aspect;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public class LogAspect {

    public void before(){
        //往对象里面记录调用的开始时间
    }

    public void after(){
        //系统当前时间-之前记录的开始时间=方法的调用所消耗的时间
        //就能够监测出方法执行性能
    }

    public void afterThrowing(){
        //异常监测，我可以拿到异常的信息
    }

}
