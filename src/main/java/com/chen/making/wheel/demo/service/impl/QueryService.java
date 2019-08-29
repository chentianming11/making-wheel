package com.chen.making.wheel.demo.service.impl;

import com.chen.making.wheel.demo.entity.Blog;
import com.chen.making.wheel.demo.mapper.BlogMapper;
import com.chen.making.wheel.demo.service.IQueryService;
import com.chen.making.wheel.framework.spring.annotation.Autowired;
import com.chen.making.wheel.framework.spring.annotation.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class QueryService implements IQueryService {

	@Autowired
	TestServiceA testServiceA;

	@Autowired
    BlogMapper blogMapper;

	/**
	 * 查询
	 */
	@Override
	public Map<String, Object> query(String name) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("data", "查出来的数据");
		map.put("token", UUID.randomUUID().toString());
		System.out.println("A  B 互相注入测试");
		testServiceA.methodA();
		return map;
	}

	@Override
	public Blog queryBlog(Integer id) {
		Blog blog = blogMapper.selectBlogById(id);
		return blog;
	}
}
