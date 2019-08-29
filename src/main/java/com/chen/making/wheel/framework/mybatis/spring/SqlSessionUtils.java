package com.chen.making.wheel.framework.mybatis.spring;

import com.chen.making.wheel.framework.mybatis.session.SqlSession;
import com.chen.making.wheel.framework.mybatis.session.SqlSessionFactory;
import lombok.experimental.UtilityClass;

/**
 * @author 陈添明
 * @date 2019/8/21
 */
@UtilityClass
public class SqlSessionUtils {

    public static ThreadLocal<SqlSession> sqlSessionThreadLocal = new ThreadLocal<>();

    /**
     * 在一个会话级别，返回的sqlSession是同一个；不同会话，返回的实例不同
     */
    public static SqlSession getSqlSession(SqlSessionFactory sqlSessionFactory) {
        // 这里简化处理，假设一个线程就是一个会话
        SqlSession sqlSession = sqlSessionThreadLocal.get();
        if (sqlSession == null) {
            sqlSession = sqlSessionFactory.openSession();
            sqlSessionThreadLocal.set(sqlSession);
        }
        return sqlSession;
    }
}
