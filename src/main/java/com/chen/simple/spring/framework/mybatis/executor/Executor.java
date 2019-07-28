package com.chen.simple.spring.framework.mybatis.executor;

import com.chen.simple.spring.framework.mybatis.mapping.MappedStatement;
import com.chen.simple.spring.framework.mybatis.session.RowBounds;

import java.sql.SQLException;
import java.util.List;

/**
 * 执行器
 */
public interface Executor {


    /**
     * 查询
     * @param <E>
     * @param ms
     * @param parameter
     * @param rowBounds
     * @return
     * @throws SQLException
     */
    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException;

}
