package com.chen.yugong.framework.spring.aop;

/**
 * @author 陈添明
 * @date 2019/6/1
 */
public class ProxyFactory extends AdvisedSupport {


    /**
     * 获取代理对象
     * 源码会有判断使用jdk还是cglib
     * 这里简化，直接写死使用jdk
     * @return
     */
    public Object getProxy() {
        return new JdkDynamicAopProxy(this).getProxy();
    }
}
