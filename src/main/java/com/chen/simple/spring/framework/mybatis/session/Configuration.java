package com.chen.simple.spring.framework.mybatis.session;

import com.chen.simple.spring.framework.mybatis.executor.*;
import com.chen.simple.spring.framework.mybatis.executor.result.DefaultResultSetHandler;
import com.chen.simple.spring.framework.mybatis.executor.result.ResultSetHandler;
import com.chen.simple.spring.framework.mybatis.executor.statement.PreparedStatementHandler;
import com.chen.simple.spring.framework.mybatis.executor.statement.StatementHandler;
import com.chen.simple.spring.framework.mybatis.mapping.MappedStatement;
import com.chen.simple.spring.framework.mybatis.util.PropertiesUtils;
import com.google.common.base.Splitter;
import lombok.Data;

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
     * 根据statement ID获取SQL
     * @param id
     * @return
     */
    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }


    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds) {
        StatementHandler statementHandler = new PreparedStatementHandler(executor, mappedStatement, parameterObject, rowBounds);
        // TODO: 2019/7/27 织入插件
//        statementHandler = (StatementHandler) interceptorChain.pluginAll(statementHandler);
        return statementHandler;
    }



    /**
     * 创建Configuration对象
     * @param properties mybatis配置属性
     */
    public Configuration(Properties properties) {
        this.properties = properties;
        parseMappedStatement();
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
        // TODO: 2019/7/27 拦截器
//        executor = (Executor) interceptorChain.pluginAll(executor);
        return executor;
    }

    /**
     * 创建参数处理器
     * @param mappedStatement
     * @param parameterObject
     * @return
     */
    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject) {
        DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject);
        // TODO: 2019/7/27 参数处理插件
//        parameterHandler = (ParameterHandler) interceptorChain.pluginAll(parameterHandler);
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
        // TODO: 2019/7/27 结果集处理插件
//        resultSetHandler = (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
        return resultSetHandler;
    }
}
