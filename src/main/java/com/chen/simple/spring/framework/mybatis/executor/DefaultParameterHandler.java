package com.chen.simple.spring.framework.mybatis.executor;

import com.chen.simple.spring.framework.mybatis.mapping.MappedStatement;
import com.chen.simple.spring.framework.mybatis.session.Configuration;
import com.chen.simple.spring.framework.mybatis.type.TypeHandler;
import com.chen.simple.spring.framework.mybatis.type.TypeHandlerRegistry;
import lombok.Data;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/7/27
 */
@Data
public class DefaultParameterHandler implements ParameterHandler {


    private final TypeHandlerRegistry typeHandlerRegistry;

    private final MappedStatement mappedStatement;
    private final Object parameterObject;
    private final Configuration configuration;

    public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject) {
        this.mappedStatement = mappedStatement;
        this.parameterObject = parameterObject;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getTypeHandlerRegistry();
    }


    @Override
    public Object getParameterObject() {
        return null;
    }

    /**
     * 设置预处理语句参数
     * 这里简单处理，sql参数和parameterObject对象对应好了
     * @throws SQLException
     */
    @Override
    public void setParameters(PreparedStatement preparedStatement) throws SQLException {
        Class<?> aClass = parameterObject.getClass();
        if (aClass.equals(String.class) || aClass.equals(Integer.class) || aClass.equals(Long.class)) {
            // 基本数据类型或者string
            TypeHandler typeHandler = typeHandlerRegistry.getTypeHandler(aClass);
            typeHandler.setParameter(preparedStatement, 1, parameterObject, null);
        } else if (aClass.equals(Map.class)) {
            Map map = (Map) parameterObject;
            // 简单随便处理一下
            map.forEach((key, value) -> {
                int i = 0;
                ++i;
                TypeHandler typeHandler = typeHandlerRegistry.getTypeHandler(value.getClass());
                try {
                    typeHandler.setParameter(preparedStatement, i, value, null);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            // 其他对象
            Field[] allFields = FieldUtils.getAllFields(parameterObject.getClass());
            for (int i = 0; i < allFields.length; i++) {
                Field field = allFields[i];
                TypeHandler typeHandler = typeHandlerRegistry.getTypeHandler(field.getType());
                Object value = null;
                try {
                    value = FieldUtils.readField(field, parameterObject);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                typeHandler.setParameter(preparedStatement, i+1, value, null);
            }
        }
    }

}
