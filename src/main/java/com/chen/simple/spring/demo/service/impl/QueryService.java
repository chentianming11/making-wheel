package com.chen.simple.spring.demo.service.impl;

import com.chen.simple.spring.demo.service.IQueryService;
import com.chen.simple.spring.framework.annotation.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 查询业务
 * @author Tom
 *
 */
@Service
public class QueryService implements IQueryService {

	/**
	 * 查询
	 */
	@Override
	public Map<String, Object> query(String name) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("data", "查出来的数据");
		map.put("token", UUID.randomUUID().toString());
		return map;
	}

}
