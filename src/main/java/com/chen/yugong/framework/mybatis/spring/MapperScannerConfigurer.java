package com.chen.yugong.framework.mybatis.spring;

import com.chen.yugong.framework.spring.annotation.Service;
import com.chen.yugong.framework.spring.beans.BeanDefinition;
import com.chen.yugong.framework.spring.beans.BeanDefinitionRegistry;
import com.chen.yugong.framework.spring.beans.BeanDefinitionRegistryPostProcessor;
import com.chen.yugong.framework.spring.beans.factory.DefaultListableBeanFactory;
import com.chen.yugong.framework.spring.util.ScannerUtils;
import lombok.Data;
import strman.Strman;

import java.util.List;

/**
 * @author 陈添明
 * @date 2019/8/21
 */
@Data
@Service
public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    /**
     * MapperScan扫描包 本身配置的，简化处理
     */
    private String basePackage = "com.chen.yugong.demo.mapper";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        // 注册Bean定义
        List<String> beanClassNames = ScannerUtils.scanner(basePackage);
        beanClassNames.forEach(beanClassName -> {
            try {
                Class<?> clz = Class.forName(beanClassName);
                if (!clz.isInterface()) {
                    return;
                }
                String beanName = Strman.lowerFirst(clz.getSimpleName());
                BeanDefinition beanDefinition = new BeanDefinition()
                        .setFactoryBeanName(beanName)
                        .setSourceBeanClass(clz)
                        .setBeanClass(MapperFactoryBean.class);
                registry.registerBeanDefinition(beanName, beanDefinition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    @Override
    public void postProcessBeanFactory(DefaultListableBeanFactory beanFactory) {

    }
}
