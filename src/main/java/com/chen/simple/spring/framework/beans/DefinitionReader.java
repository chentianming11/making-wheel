package com.chen.simple.spring.framework.beans;

import com.chen.simple.spring.framework.annotation.Controller;
import com.chen.simple.spring.framework.annotation.Service;
import lombok.Data;
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

    private Properties config = new Properties();

    //固定配置文件中的 key，相对于 xml 的规范
    private final String SCAN_PACKAGE = "scanPackage";

    public DefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }


    public int loadBeanDefinitions(String location) {
        // 加载配置文件
        try (InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream(location.replace("classpath:", ""))) {
            config.load(is);
            // 扫描包
            doScanner(config.getProperty(SCAN_PACKAGE));
            // 加载Bean定义
            int count = doLoadBeanDefinitions();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 加载Bean定义
     */
    private int doLoadBeanDefinitions() {
        try {
            int count = registry.getBeanDefinitionCount();
            for (String className : classNames) {
                Class<?> clz = Class.forName(className);
                if (clz.isInterface()) {
                    continue;
                }
                if (!(clz.isAnnotationPresent(Controller.class) || clz.isAnnotationPresent(Service.class))) {
                    continue;
                }
                String beanName = Strman.lowerFirst(clz.getSimpleName());

                BeanDefinition beanDefinition = new BeanDefinition()
                        .setFactoryBeanName(beanName)
                        .setBeanClassName(className);
                // 所有的接口类型设置别名
                Class<?>[] interfaces = clz.getInterfaces();
                if (interfaces.length > 0) {
                    List<String> alias = new ArrayList<>();
                    for (Class<?> anInterface : interfaces) {
                        alias.add(Strman.lowerFirst(anInterface.getSimpleName()));
                    }
                    beanDefinition.setAlias(alias);
                }
                registry.registerBeanDefinition(beanName, beanDefinition);
            }
            return registry.getBeanDefinitionCount() - count;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 包扫描
     *
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
