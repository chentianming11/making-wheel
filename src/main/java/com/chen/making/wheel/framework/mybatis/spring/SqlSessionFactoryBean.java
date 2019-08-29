package com.chen.making.wheel.framework.mybatis.spring;

import com.chen.making.wheel.framework.mybatis.session.SqlSessionFactory;
import com.chen.making.wheel.framework.mybatis.session.SqlSessionFactoryBuilder;
import com.chen.making.wheel.framework.mybatis.util.PropertiesUtils;
import com.chen.making.wheel.framework.spring.annotation.Service;
import com.chen.making.wheel.framework.spring.beans.FactoryBean;
import com.chen.making.wheel.framework.spring.beans.factory.InitializingBean;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author 陈添明
 * @date 2019/8/21
 */
@Service
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean {

    private SqlSessionFactory sqlSessionFactory;

    @Override
    public SqlSessionFactory getObject() throws Exception {
        if (this.sqlSessionFactory == null) {
            afterPropertiesSet();
        }

        return this.sqlSessionFactory;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.sqlSessionFactory = buildSqlSessionFactory();
    }


    /**
     * 构建SqlSessionFactory, 简化处理，直接合并配置属性
     * @return
     * @throws IOException
     */
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        // 合并配置数据
        Properties properties = PropertiesUtils.load("mybatis/config/mybatis.properties");
        Properties appProperties = PropertiesUtils.load("application.properties");
        String jdbcDriver = appProperties.getProperty("jdbc.driver");
        if (StringUtils.isNotBlank(jdbcDriver)) {
            properties.setProperty("jdbc.driver", jdbcDriver);
        }
        String jdbcUrl = appProperties.getProperty("jdbc.url");
        if (StringUtils.isNotBlank(jdbcUrl)) {
            properties.setProperty("jdbc.url", jdbcUrl);
        }
        String jdbcUsername = appProperties.getProperty("jdbc.username");
        if (StringUtils.isNotBlank(jdbcUsername)) {
            properties.setProperty("jdbc.username", jdbcUsername);
        }
        String jdbcPassword = appProperties.getProperty("jdbc.password");
        if (StringUtils.isNotBlank(jdbcPassword)) {
            properties.setProperty("jdbc.password", jdbcPassword);
        }
        String mapperPackage = appProperties.getProperty("mybatis.mapper.package");
        if (StringUtils.isNotBlank(mapperPackage)) {
            properties.setProperty("mapper.package", mapperPackage);
        }
        String mappers = appProperties.getProperty("mybatis.mappers");
        if (StringUtils.isNotBlank(mappers)) {
            properties.setProperty("mappers", mappers);
        }
        String plugins = appProperties.getProperty("mybatis.plugins");
        if (StringUtils.isNotBlank(plugins)) {
            properties.setProperty("plugins", plugins);
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(properties);
        return sqlSessionFactory;

    }
}
