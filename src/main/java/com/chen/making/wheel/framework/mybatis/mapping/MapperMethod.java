/*
 *    Copyright 2009-2013 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.chen.making.wheel.framework.mybatis.mapping;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 * @author Lasse Voss
 */


import com.chen.making.wheel.framework.mybatis.session.Configuration;
import com.chen.making.wheel.framework.mybatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * 映射器方法
 */
public class MapperMethod {

    private final Class<?> mapperInterface;
    private final Method method;
    private final Configuration configuration;


    public <T> MapperMethod(Class<T> mapperInterface, Method method, Configuration configuration) {
        this.mapperInterface = mapperInterface;
        this.method = method;
        this.configuration = configuration;
    }

    /**
     * 实际逻辑是根据和sql类型和方法返回值类型调用sqlSession对应的CRUD方法
     * 根据方法签名形参进行sql参数转换
     * 这里简化处理
     * @param sqlSession
     * @param args
     * @return
     */
    public Object execute(SqlSession sqlSession, Object[] args) {
        String statement = mapperInterface.getName() + "." + method.getName();
        return sqlSession.selectOne(statement, args[0]);
    }
}
