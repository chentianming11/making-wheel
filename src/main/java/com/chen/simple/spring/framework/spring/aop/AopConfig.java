package com.chen.simple.spring.framework.spring.aop;

import lombok.Data;


@Data
public class AopConfig {

    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;

}
