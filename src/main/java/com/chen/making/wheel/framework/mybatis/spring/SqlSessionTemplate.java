package com.chen.making.wheel.framework.mybatis.spring;


import com.chen.making.wheel.framework.mybatis.session.Configuration;
import com.chen.making.wheel.framework.mybatis.session.RowBounds;
import com.chen.making.wheel.framework.mybatis.session.SqlSession;
import com.chen.making.wheel.framework.mybatis.session.SqlSessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * @author 陈添明
 * @date 2019/8/21
 */
public class SqlSessionTemplate implements SqlSession {

    private final SqlSessionFactory sqlSessionFactory;

    private final SqlSession sqlSessionProxy;


    public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.sqlSessionProxy = (SqlSession) newProxyInstance(
                SqlSessionFactory.class.getClassLoader(),
                new Class[] { SqlSession.class },
                new SqlSessionInterceptor());
    }

    private class SqlSessionInterceptor implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 在一个会话级别，返回的sqlSession是同一个；不同会话，返回的实例不同
            SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
            // 调用sqlSession的方法
            Object result = method.invoke(sqlSession, args);
            return result;
        }
    }

    /**
     * Retrieve a single row mapped from the statement key.
     *
     * @param statement
     * @return Mapped object
     */
    @Override
    public <T> T selectOne(String statement) {
        return sqlSessionProxy.selectOne(statement);
    }

    /**
     * Retrieve a single row mapped from the statement key and parameter.
     *
     * @param statement Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @return Mapped object
     */
    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return sqlSessionProxy.selectOne(statement, parameter);
    }

    /**
     * Retrieve a list of mapped objects from the statement key and parameter.
     *
     * @param statement Unique identifier matching the statement to use.
     * @return List of mapped object
     */
    @Override
    public <E> List<E> selectList(String statement) {
        return sqlSessionProxy.selectList(statement);
    }

    /**
     * Retrieve a list of mapped objects from the statement key and parameter.
     *
     * @param statement Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @return List of mapped object
     */
    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return sqlSessionProxy.selectList(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return sqlSessionProxy.selectList(statement, parameter, rowBounds);
    }

    /**
     * Retrieves a mapper.
     *
     * @param type Mapper interface class
     * @return a mapper bound to this SqlSession
     */
    @Override
    public <T> T getMapper(Class<T> type) {
        return getConfiguration().getMapper(type, this);
    }

    /**
     * Retrieves current configuration
     * 得到配置
     * @return Configuration
     */
    @Override
    public Configuration getConfiguration() {
        return sqlSessionFactory.getConfiguration();
    }
}
