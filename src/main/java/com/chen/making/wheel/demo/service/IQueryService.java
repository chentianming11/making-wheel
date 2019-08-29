package com.chen.making.wheel.demo.service;

import com.chen.making.wheel.demo.entity.Blog;

import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public interface IQueryService {
	
	/**
	 * 查询
	 */
	Map<String, Object> query(String name);

	Blog queryBlog(Integer id);
}
