package com.chen.yugong.framework.mybatis.type;

import com.chen.yugong.framework.mybatis.util.ParamMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类型处理器注册中心
 * java类型、Jdbc类型与类型处理器之间的映射关系
 * @author 陈添明
 * @date 2019/7/28
 */
public class TypeHandlerRegistry {

    /**
     * jdbc类型处理器映射
     */
    private final Map<JdbcType, TypeHandler<?>> jdbcTypeHandlerMap = new EnumMap<>(JdbcType.class);

    /**
     * 类型处理器映射
     */
    private final Map<Type, Map<JdbcType, TypeHandler<?>>> typeHandlerMap = new ConcurrentHashMap<>();


    private final Map<Class<?>, TypeHandler<?>> allTypeHandlersMap = new HashMap<>();

    private static final Map<JdbcType, TypeHandler<?>> NULL_TYPE_HANDLER_MAP = Collections.emptyMap();

    public TypeHandlerRegistry() {
        register(Integer.class, new IntegerTypeHandler());
        register(int.class, new IntegerTypeHandler());
        register(JdbcType.INTEGER, new IntegerTypeHandler());

        register(Long.class, new LongTypeHandler());
        register(long.class, new LongTypeHandler());

        register(String.class, new StringTypeHandler());
        register(JdbcType.VARCHAR, new StringTypeHandler());
    }





    public void register(JdbcType jdbcType, TypeHandler<?> handler) {
        jdbcTypeHandlerMap.put(jdbcType, handler);
    }

    // java type + handler

    public <T> void register(Class<T> javaType, TypeHandler<? extends T> typeHandler) {
        register((Type) javaType, typeHandler);
    }

    private <T> void register(Type javaType, TypeHandler<? extends T> typeHandler) {
        MappedJdbcTypes mappedJdbcTypes = typeHandler.getClass().getAnnotation(MappedJdbcTypes.class);
        if (mappedJdbcTypes != null) {
            for (JdbcType handledJdbcType : mappedJdbcTypes.value()) {
                register(javaType, handledJdbcType, typeHandler);
            }
            if (mappedJdbcTypes.includeNullJdbcType()) {
                register(javaType, null, typeHandler);
            }
        } else {
            register(javaType, null, typeHandler);
        }
    }


    private void register(Type javaType, JdbcType jdbcType, TypeHandler<?> handler) {
        if (javaType != null) {
            Map<JdbcType, TypeHandler<?>> map = typeHandlerMap.get(javaType);
            if (map == null || map == NULL_TYPE_HANDLER_MAP) {
                map = new HashMap<>();
                typeHandlerMap.put(javaType, map);
            }
            map.put(jdbcType, handler);
        }
        allTypeHandlersMap.put(handler.getClass(), handler);
    }


    /**
     * 获取javaTypeClass对应的类型处理器实例
     * @param javaTypeClass
     * @param typeHandlerClass
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> TypeHandler<T> getInstance(Class<?> javaTypeClass, Class<?> typeHandlerClass) {
        if (javaTypeClass != null) {
            try {
                Constructor<?> c = typeHandlerClass.getConstructor(Class.class);
                return (TypeHandler<T>) c.newInstance(javaTypeClass);
            } catch (NoSuchMethodException ignored) {
                // ignored
            } catch (Exception e) {
                throw new TypeException("Failed invoking constructor for handler " + typeHandlerClass, e);
            }
        }
        try {
            Constructor<?> c = typeHandlerClass.getConstructor();
            return (TypeHandler<T>) c.newInstance();
        } catch (Exception e) {
            throw new TypeException("Unable to find a usable constructor for " + typeHandlerClass, e);
        }
    }


    public <T> TypeHandler<T> getTypeHandler(Class<T> type) {
        return getTypeHandler(type, null);
    }

    public TypeHandler<?> getTypeHandler(JdbcType jdbcType) {
        return jdbcTypeHandlerMap.get(jdbcType);
    }

    public <T> TypeHandler<T> getTypeHandler(Class<T> type, JdbcType jdbcType) {
        return getTypeHandler((Type) type, jdbcType);
    }


    @SuppressWarnings("unchecked")
    private <T> TypeHandler<T> getTypeHandler(Type type, JdbcType jdbcType) {
        if (ParamMap.class.equals(type)) {
            return null;
        }
        Map<JdbcType, TypeHandler<?>> jdbcTypeTypeHandlerMap = typeHandlerMap.get(type);
        if (jdbcTypeTypeHandlerMap == null) {
            return null;
        }
        return (TypeHandler<T>) jdbcTypeTypeHandlerMap.get(jdbcType);
    }

}
