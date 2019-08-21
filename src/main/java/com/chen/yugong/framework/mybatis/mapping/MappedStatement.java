/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.chen.yugong.framework.mybatis.mapping;

import com.chen.yugong.framework.mybatis.session.Configuration;
import com.chen.yugong.framework.mybatis.type.TypeHandlerRegistry;
import lombok.Builder;
import lombok.Getter;

/**
 * @author Clinton Begin
 */
@Getter
@Builder
public final class MappedStatement {

  private String resource;
  private Configuration configuration;
  private String id;
  private String sql;
  protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
  private Class<?> returnType;

//  private StatementType statementType;
//  private ResultSetType resultSetType;

//  private ParameterMap parameterMap;
//  private List<ResultMap> resultMaps;
//  private boolean flushCacheRequired;
  private boolean useCache;



}
