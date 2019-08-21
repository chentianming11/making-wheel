/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chen.yugong.framework.spring.beans.factory.config;

import com.chen.yugong.framework.spring.beans.factory.DefaultListableBeanFactory;

@FunctionalInterface
public interface BeanFactoryPostProcessor {

	/**
	 * ﻿在标准初始化之后修改应用程序上下文的内部bean工厂。 此时所有bean定义都已经加载完成，但尚未实例化任何bean。 这允许覆盖或添加属性，甚至是初始化bean。
	 * @param beanFactory
	 */
	void postProcessBeanFactory(DefaultListableBeanFactory beanFactory);

}
