package com.chen.yugong.framework.mybatis.spring;

import com.chen.yugong.framework.spring.beans.FactoryBean;
import lombok.Data;

/**
 * @author 陈添明
 * @date 2019/8/21
 */
@Data
public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {

    private Class<T> mapperInterface;

    /**
     * {@inheritDoc}
     */
    @Override
    public T getObject() throws Exception {
        return getSqlSession().getMapper(this.mapperInterface);
    }
}
