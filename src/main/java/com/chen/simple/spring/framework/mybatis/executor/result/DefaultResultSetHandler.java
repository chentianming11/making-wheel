package com.chen.simple.spring.framework.mybatis.executor.result;

import com.chen.simple.spring.framework.mybatis.executor.Executor;
import com.chen.simple.spring.framework.mybatis.executor.ParameterHandler;
import com.chen.simple.spring.framework.mybatis.mapping.MappedStatement;
import com.chen.simple.spring.framework.mybatis.session.Configuration;
import com.chen.simple.spring.framework.mybatis.session.RowBounds;
import com.chen.simple.spring.framework.mybatis.type.TypeHandler;
import com.chen.simple.spring.framework.mybatis.type.TypeHandlerRegistry;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import strman.Strman;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈添明
 * @date 2019/7/27
 */
@Data
public class DefaultResultSetHandler implements ResultSetHandler {


    private final Executor executor;
    private final Configuration configuration;
    private final MappedStatement mappedStatement;
    private final RowBounds rowBounds;
    private final ParameterHandler parameterHandler;
    private final TypeHandlerRegistry typeHandlerRegistry;
//    private final ObjectFactory objectFactory;
//    private final ReflectorFactory reflectorFactory;

    public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler, RowBounds rowBounds) {
        this.executor = executor;
        this.configuration = mappedStatement.getConfiguration();
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;
        this.parameterHandler = parameterHandler;
        this.typeHandlerRegistry = mappedStatement.getTypeHandlerRegistry();
    }


    /**
     * 处理结果集
     * 简单处理，全部按照返回类型进行处理，假设字段完全匹配
     * @param stmt
     * @param <E>
     * @return
     * @throws SQLException
     */
    @Override
    @SneakyThrows
    public <E> List<E> handleResultSets(Statement stmt) throws SQLException {
        List<E> list = new ArrayList<>();
        ResultSet resultSet = stmt.getResultSet();
        Class<?> returnType = mappedStatement.getReturnType();
        while (resultSet.next()) {
            Object instance = returnType.newInstance();
            Field[] allFields = FieldUtils.getAllFields(returnType);
            for (Field field : allFields) {
                TypeHandler typeHandler = typeHandlerRegistry.getTypeHandler(field.getType());
                int column = 0;
                try {
                    column = resultSet.findColumn(Strman.toSnakeCase(field.getName()));
                } catch (SQLException e) {
                    continue;
                }
                Object result = typeHandler.getResult(resultSet, column);
                if (result == null) {
                    continue;
                }
                FieldUtils.writeField(field, instance, result, true);
            }
            list.add((E) instance);
        }


        return list;
    }
}
