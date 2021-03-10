package com.chen.making.wheel.demo.aspect;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public class LogAspect {


    public void before(){
        //往对象里面记录调用的开始时间
        System.out.println("开始执行：start => " + System.currentTimeMillis());

        // 测试github提交代码
    }

    public void afterReturning(){
        System.out.println("执行结束！！！");
    }



}
