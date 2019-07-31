package com.chen.simple.spring.framework.mybatis.session;

import com.chen.simple.spring.framework.mybatis.executor.*;
import com.chen.simple.spring.framework.mybatis.executor.result.DefaultResultSetHandler;
import com.chen.simple.spring.framework.mybatis.executor.result.ResultSetHandler;
import com.chen.simple.spring.framework.mybatis.executor.statement.PreparedStatementHandler;
import com.chen.simple.spring.framework.mybatis.executor.statement.StatementHandler;
import com.chen.simple.spring.framework.mybatis.mapping.MappedStatement;
import com.chen.simple.spring.framework.mybatis.mapping.MapperRegistry;
import com.chen.simple.spring.framework.mybatis.plugin.Interceptor;
import com.chen.simple.spring.framework.mybatis.plugin.InterceptorChain;
import com.chen.simple.spring.framework.mybatis.util.PropertiesUtils;
import com.google.common.base.Splitter;
import lombok.Data;
import org.apache.commons.lang3.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author 陈添明
 * @date 2019/7/27
 */
@Data
public class Configuration {

    /**
     * 全局配置
     */
    public Properties properties;

    protected boolean cacheEnabled = true;

    /**
     * 映射注册中心
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 拦截器链
     */
    protected final InterceptorChain interceptorChain = new InterceptorChain();


    /**
     * 维护sql映射文件关联关系
     * statementId和sql的映射
     */
    public final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    /**
     * 根据statement判断是否存在映射的SQL
     * @param statementName
     * @return
     */
    public boolean hasStatement(String statementName) {
        return mappedStatements.containsKey(statementName);
    }

    /**
     * 获取拦截器集合
     * @return
     */
    public List<Interceptor> getInterceptors() {
        return interceptorChain.getInterceptors();
    }

    /**
     * 添加拦截器
     * @param interceptor
     */
    public void addInterceptor(Interceptor interceptor) {
        interceptorChain.addInterceptor(interceptor);
    }


    /***
     * 获取Mapper代理对象
     * @param type
     * @param sqlSession
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    /**
     * 将包下的所有映射器接口添加到Mapper注册中心
     * @param packageName
     */
    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    /**
     * 将制定映射器接口添加到Mapper注册中心
     * @param type
     * @param <T>
     */
    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }


    /**
     * 根据statement ID获取SQL
     * @param id
     * @return
     */
    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }


    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds) {
        StatementHandler statementHandler = new PreparedStatementHandler(executor, mappedStatement, parameterObject, rowBounds);
        statementHandler = (StatementHandler) interceptorChain.pluginAll(statementHandler);
        return statementHandler;
    }



    /**
     * 创建Configuration对象
     * @param properties mybatis配置属性
     */
    public Configuration(Properties properties) {
        this.properties = properties;
        // 设置配置项参数 这里省略

        // 解析插件配置 将配置的插件添加到拦截器链
        parsePlugin();
        // 解析映射的Statement
        parseMappedStatement();
        // 解析映射器接口 将mapper接口添加到Mapper注册中心
        parseMappers();
    }

    /**
     * 解析映射器接口  将mapper接口添加到Mapper注册中心
     */
    private void parseMappers() {
        String mapperPackages = properties.getProperty("mapper.package");
        List<String> mapperPackageList = Splitter.on(",").splitToList(mapperPackages);
        mapperPackageList.forEach(this::addMappers);
    }

    /**
     * 解析插件配置  将配置的插件添加到拦截器链
     */
    private void parsePlugin() {
        String plugins = properties.getProperty("plugins");
        List<String> pluginList = Splitter.on(",").splitToList(plugins);
        pluginList.forEach(pluginClassName -> {
            try {
                Class<?> pluginClass = ClassUtils.getClass(pluginClassName);
                Interceptor interceptor = (Interceptor) pluginClass.newInstance();
                addInterceptor(interceptor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 解析映射的Statement
     */
    private void parseMappedStatement() {
        String mappers = properties.getProperty("mappers");
        List<String> mapperList = Splitter.on(",").splitToList(mappers);
        mapperList.forEach(path -> {
            Properties properties = PropertiesUtils.load(path);
            properties.forEach((key, val) -> {
                String id = String.valueOf(key);
                String baseSql = String.valueOf(val);

                List<String> list = Splitter.on("--").splitToList(baseSql);
                String sql = list.get(0);
                String returnTypeName = list.get(1);
                Class<?> returnType = null;
                try {
                    returnType = Class.forName(returnTypeName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                MappedStatement mappedStatement = MappedStatement.builder()
                        .id(id)
                        .sql(sql)
                        .returnType(returnType)
                        .configuration(this)
                        .resource(path)
                        .build();
                mappedStatements.put(id, mappedStatement);
            });
        });
    }


    /**
     * 创建执行器
     * @return
     */
    public Executor newExecutor() {
       Executor executor = new SimpleExecutor(this);
        if (cacheEnabled) {
            executor = new CachingExecutor(executor);
        }
        // 织入插件
        executor = (Executor) interceptorChain.pluginAll(executor);
        return executor;
    }

    /**
     * 创建参数处理器
     * @param mappedStatement
     * @param parameterObject
     * @return
     */
    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject) {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject);
        // 织入插件
        parameterHandler = (ParameterHandler) interceptorChain.pluginAll(parameterHandler);
        return parameterHandler;
    }

    /**
     * 创建结果集处理器
     * @param executor
     * @param mappedStatement
     * @param rowBounds
     * @param parameterHandler
     * @return
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ParameterHandler parameterHandler) {
        ResultSetHandler resultSetHandler = new DefaultResultSetHandler(executor, mappedStatement, parameterHandler, rowBounds);
        // 织入插件
        resultSetHandler = (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
        return resultSetHandler;
    }
}
