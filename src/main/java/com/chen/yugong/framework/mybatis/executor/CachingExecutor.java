package com.chen.yugong.framework.mybatis.executor;

import com.chen.yugong.framework.mybatis.cache.CacheKey;
import com.chen.yugong.framework.mybatis.mapping.MappedStatement;
import com.chen.yugong.framework.mybatis.session.RowBounds;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/7/27
 */
public class CachingExecutor implements Executor {


    private final Executor delegate;

    private static final Map<Integer, Object> cache = new HashMap<>();

    public CachingExecutor(Executor delegate) {
        this.delegate = delegate;
    }


    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
        CacheKey cacheKey = createCacheKey(ms, parameter, rowBounds);
        // 是否拿到缓存
        if (cache.containsKey(cacheKey.getCode())) {
            // 命中缓存
            System.out.println("【命中缓存】");
            return (List<E>) cache.get(cacheKey.getCode());
        }else{
            // 没有的话调用被装饰的SimpleExecutor从数据库查询
            Object obj = delegate.query(ms, parameter, rowBounds);
            cache.put(cacheKey.getCode(), obj);
            return (List<E>) obj;
        }
    }

    private CacheKey createCacheKey(MappedStatement ms, Object parameter, RowBounds rowBounds) {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(ms.getId());
        cacheKey.update(rowBounds.getOffset());
        cacheKey.update(rowBounds.getLimit());
        cacheKey.update(ms.getSql());
        cacheKey.update(parameter);
        return cacheKey;
    }


    public static void remoceCache() {
        cache.clear();
    }
}
