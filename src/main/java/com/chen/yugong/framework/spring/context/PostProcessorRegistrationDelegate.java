package com.chen.yugong.framework.spring.context;

import com.chen.yugong.framework.spring.beans.BeanDefinitionRegistry;
import com.chen.yugong.framework.spring.beans.BeanDefinitionRegistryPostProcessor;
import com.chen.yugong.framework.spring.beans.factory.DefaultListableBeanFactory;
import com.chen.yugong.framework.spring.beans.factory.config.BeanFactoryPostProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 陈添明
 * @date 2019/8/21
 */
public class PostProcessorRegistrationDelegate {

    public static void invokeBeanFactoryPostProcessors(
            DefaultListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {
        if (beanFactory instanceof BeanDefinitionRegistry) {
            BeanDefinitionRegistry registry = beanFactory;
            List<BeanFactoryPostProcessor> regularPostProcessors = new LinkedList<>();
            List<BeanDefinitionRegistryPostProcessor> registryProcessors = new LinkedList<>();

            // 执行传入的beanFactoryPostProcessors
            for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
                if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
                    BeanDefinitionRegistryPostProcessor registryProcessor =
                            (BeanDefinitionRegistryPostProcessor) postProcessor;
                    // BeanDefinitionRegistryPostProcessor在beanFactoryPostProcessors里面， 直接执行注册
                    registryProcessor.postProcessBeanDefinitionRegistry(registry);
                    registryProcessors.add(registryProcessor);
                }
                else {
                    regularPostProcessors.add(postProcessor);
                }
            }

            // 从容器里面获取BeanDefinitionRegistryPostProcessor
            List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();
            String[] postProcessorNames =
                    beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true);
            for (String ppName : postProcessorNames) {
                BeanDefinitionRegistryPostProcessor beanDefinitionRegistryPostProcessor = (BeanDefinitionRegistryPostProcessor) beanFactory.getBean(ppName);
                currentRegistryProcessors.add(beanDefinitionRegistryPostProcessor);
                registryProcessors.add(beanDefinitionRegistryPostProcessor);
            }
            invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
            currentRegistryProcessors.clear();

            // Now, invoke the postProcessBeanFactory callback of all processors handled so far.
            invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
            invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
        }
    }


    /**
     * Invoke the given BeanDefinitionRegistryPostProcessor beans.
     */
    private static void invokeBeanDefinitionRegistryPostProcessors(
            Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {

        for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanDefinitionRegistry(registry);
        }
    }

    /**
     * Invoke the given BeanFactoryPostProcessor beans.
     */
    private static void invokeBeanFactoryPostProcessors(
            Collection<? extends BeanFactoryPostProcessor> postProcessors, DefaultListableBeanFactory beanFactory) {

        for (BeanFactoryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanFactory(beanFactory);
        }
    }
}
