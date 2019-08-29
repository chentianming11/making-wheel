/*
 * Copyright 2002-2017 the original author or authors.
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

package com.chen.making.wheel.framework.spring.beans.factory.config;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {


	/**
	 * bean实例化之前回调
	 * @param beanClass
	 * @param beanName
	 * @return
	 */
	default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName)  {
		return null;
	}


	/**
	 * bean实例化之后回调
	 * @param bean
	 * @param beanName
	 * @return
	 */
	default boolean postProcessAfterInstantiation(Object bean, String beanName) {
		return true;
	}


}
