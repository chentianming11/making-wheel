package com.chen.simple.spring.framework.mybatis.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author 陈添明
 * @date 2019/7/27
 */
@UtilityClass
public class PropertiesUtils {


    @SneakyThrows
    public Properties load(String path) {
        try (InputStream inputStream = PropertiesUtils.class.getClassLoader()
                .getResourceAsStream(path)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        }
    }
}
