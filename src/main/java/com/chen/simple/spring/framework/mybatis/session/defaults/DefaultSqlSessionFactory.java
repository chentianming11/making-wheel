package com.chen.simple.spring.framework.mybatis.session.defaults;

import com.chen.simple.spring.framework.mybatis.executor.Executor;
import com.chen.simple.spring.framework.mybatis.session.Configuration;
import com.chen.simple.spring.framework.mybatis.session.SqlSession;
import com.chen.simple.spring.framework.mybatis.session.SqlSessionFactory;

/**
 * @author 陈添明
 * @date 2019/7/27
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        final Executor executor = configuration.newExecutor();
        return new DefaultSqlSession(configuration, executor);
    }
}
