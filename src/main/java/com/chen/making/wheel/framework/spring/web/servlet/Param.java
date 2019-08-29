package com.chen.making.wheel.framework.spring.web.servlet;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 陈添明
 * @date 2019/4/21
 */
@Data
@AllArgsConstructor
public class Param {

    private String name;

    private Integer index;

    private Class<?> type;

}
