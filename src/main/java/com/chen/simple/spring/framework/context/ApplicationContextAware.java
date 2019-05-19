/*
 * Copyright 2002-2012 the original author or authors.
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

package com.chen.simple.spring.framework.context;

public interface ApplicationContextAware {


	/**
	 * 通过解耦方式获得 IOC 容器的顶层设计
	 * 后面将通过一个监听器去扫描所有的类，只要实现了此接口，
	 * 将自动调用 setApplicationContext()方法，从而将 IOC 容器注入到目标类中
	 * @param applicationContext
	 */
	void setApplicationContext(ApplicationContext applicationContext);

}
