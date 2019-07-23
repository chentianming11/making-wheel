package com.chen.simple.spring.demo.service.impl;

import com.chen.simple.spring.demo.service.IModifyService;
import com.chen.simple.spring.framework.spring.annotation.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
@Service
public class ModifyService implements IModifyService {

	/**
	 * 增加
	 */
	@Override
	public Map<String, Object> add(String name, String addr) {
//		throw new RuntimeException("故意抛的异常！！");
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("addr",addr);
		return map;
	}


	
}
