package com.chen.simple.spring.framework.beans;

import lombok.Data;
import lombok.SneakyThrows;
import strman.Strman;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
@Data
public class DefinitionReader {

    private BeanDefinitionRegistry registry;

    private List<String> classNames = new ArrayList<>();

    private Properties config;

    //固定配置文件中的 key，相对于 xml 的规范
    private final String SCAN_PACKAGE = "scanPackage";

    public DefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }


    @SneakyThrows
    public int loadBeanDefinitions(String location) {
        // 加载配置文件
        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream(location.replace("classpath:", ""));
        config.load(is);
        // 扫描包
        doScanner(config.getProperty("SCAN_PACKAGE"));
        // 加载Bean定义
        int count = doLoadBeanDefinitions();
        return count;
    }

    /**
     * 加载Bean定义
     */
    @SneakyThrows
    private int doLoadBeanDefinitions() {
        int count = registry.getBeanDefinitionCount();
        for (String className : classNames) {
            Class<?> clz = Class.forName(className);
            if (clz.isInterface()) { continue; }
            String beanName = Strman.lowerFirst(clz.getSimpleName());
            BeanDefinition beanDefinition = new BeanDefinition().setFactoryBeanName(beanName).setBeanClassName(className);
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
        return registry.getBeanDefinitionCount() - count;
    }

    /**
     * 包扫描
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
        URL resource = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File scanDir = new File(resource.getPath());
        File[] files = scanDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 是目录 递归扫描
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                classNames.add(scanPackage + "." + file.getName().replaceAll("\\.class", ""));
            }
        }
    }
}
