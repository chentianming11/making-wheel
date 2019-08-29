package com.chen.making.wheel.framework.spring.beans;

import com.chen.making.wheel.framework.spring.annotation.Controller;
import com.chen.making.wheel.framework.spring.annotation.Service;
import com.chen.making.wheel.framework.spring.util.ScannerUtils;
import lombok.Data;
import org.apache.commons.lang3.ClassUtils;
import strman.Strman;

import java.io.InputStream;
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
            classNames = ScannerUtils.scanner(config.getProperty(SCAN_PACKAGE));
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
                        .setSourceBeanClass(clz)
                        .setBeanClass(clz);
                // 所有接口类型名称设置为别名
                List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(clz);
                List<String> alias = new ArrayList<>();
                if (!allInterfaces.isEmpty()) {
                    for (Class<?> interClz : allInterfaces) {
                        alias.add(Strman.lowerFirst(interClz.getSimpleName()));
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


}
