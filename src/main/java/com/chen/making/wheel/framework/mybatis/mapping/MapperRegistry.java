package com.chen.making.wheel.framework.mybatis.mapping;

import com.chen.making.wheel.framework.mybatis.session.Configuration;
import com.chen.making.wheel.framework.mybatis.session.SqlSession;
import com.chen.making.wheel.framework.mybatis.util.BindingException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/7/31
 */
public class MapperRegistry {

    private Configuration config;

    //将已经添加的映射都放入HashMap
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    /**
     * 获取Mapper代理对象
     * @param type
     * @param sqlSession
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public void addMappers(String packageName) {
        addMappers(packageName, Object.class);
    }

    /**
     * 查找包下所有是superType的类 并添加到knownMappers中
     * @since 3.2.2
     */
    public void addMappers(String packageName, Class<?> superType) {
        try {
            URL resource = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
            File scanDir = new File(resource.getPath());
            Collection<File> files = FileUtils.listFiles(scanDir, new String[]{"class"}, true);
            for (File file : files) {
               String className =  packageName + "." + file.getName().replaceAll("\\.class", "");
                Class<?> clz = Class.forName(className);
                if (!superType.isAssignableFrom(clz)) {
                    continue;
                }
                addMapper(clz);

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public <T> void addMapper(Class<T> type) {
        //mapper必须是接口！才会添加
        if (!type.isInterface()) {
            return;
        }
        if (hasMapper(type)) {
            //如果重复添加了，报错
            throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
        }
        knownMappers.put(type, new MapperProxyFactory<>(type));
        // 处理Mapper接口上的注解  用于使用mapper注解书写sql的场景 暂时忽略
    }

    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }
}
