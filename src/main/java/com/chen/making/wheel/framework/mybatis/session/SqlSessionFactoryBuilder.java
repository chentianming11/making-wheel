package com.chen.making.wheel.framework.mybatis.session;

import com.chen.making.wheel.framework.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.chen.making.wheel.framework.mybatis.util.PropertiesUtils;
import lombok.SneakyThrows;

import java.util.Properties;

/**
 * @author 陈添明
 * @date 2019/7/27
 */
public class SqlSessionFactoryBuilder {


    /**
     * 构建SqlSessionFactory
     * @param config
     * @return
     */
    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }

    /**
     *
     * @param configPath 配置文件路径
     * @return
     */
    @SneakyThrows
    public SqlSessionFactory build(String configPath) {
        // 加载配置文件
        Properties properties = PropertiesUtils.load(configPath);
        // 根据配置文件创建Configuration对象
        Configuration configuration = new Configuration(properties);
            return build(configuration);
    }

    /**
     *
     * @return
     */
    @SneakyThrows
    public SqlSessionFactory build(Properties properties) {
        // 根据配置文件创建Configuration对象
        Configuration configuration = new Configuration(properties);
        return build(configuration);
    }
}
