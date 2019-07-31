package com.chen.simple.spring.framework.mybatis.session.defaults;


import com.chen.simple.spring.framework.mybatis.executor.Executor;
import com.chen.simple.spring.framework.mybatis.mapping.MappedStatement;
import com.chen.simple.spring.framework.mybatis.session.Configuration;
import com.chen.simple.spring.framework.mybatis.session.RowBounds;
import com.chen.simple.spring.framework.mybatis.session.SqlSession;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 * MeBatis的API，提供给应用层使用
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }



    @Override
    public <T> T selectOne(String statement) {
        return this.selectOne(statement, null);
    }


    @Override
    public <T> T selectOne(String statement, Object parameter) {
        // Popular vote was to return null on 0 results and throw exception on too many.
        List<T> list = this.selectList(statement, parameter);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return this.selectList(statement, null);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return this.selectList(statement, parameter, RowBounds.DEFAULT);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            return executor.query(ms, wrapCollection(parameter), rowBounds);
        } catch (Exception e) {
            throw new RuntimeException("Error querying database.  Cause: " + e, e);
        }
    }

    private Object wrapCollection(final Object object) {
        if (object instanceof Collection) {
            StrictMap<Object> map = new StrictMap<>();
            map.put("collection", object);
            if (object instanceof List) {
                map.put("list", object);
            }
            return map;
        } else if (object != null && object.getClass().isArray()) {
            StrictMap<Object> map = new StrictMap<>();
            map.put("array", object);
            return map;
        }
        return object;
    }


    @Override
    public <T> T getMapper(Class<T> type) {
        //最后会去调用MapperRegistry.getMapper
        return configuration.<T>getMapper(type, this);
    }

    /**
     * Retrieves current configuration
     * 得到配置
     *
     * @return Configuration
     */
    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    public static class StrictMap<V> extends HashMap<String, V> {

        private static final long serialVersionUID = -5741767162221585340L;

        @Override
        public V get(Object key) {
            if (!super.containsKey(key)) {
                throw new RuntimeException("Parameter '" + key + "' not found. Available parameters are " + this.keySet());
            }
            return super.get(key);
        }

    }
}
