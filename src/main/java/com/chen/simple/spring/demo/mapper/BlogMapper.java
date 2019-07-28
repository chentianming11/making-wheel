package com.chen.simple.spring.demo.mapper;


import com.chen.simple.spring.demo.entity.Blog;

/**
 * @Author: qingshan
 * @Date: 2019/2/23 17:54
 * @Description: 咕泡学院，只为更好的你
 */
public interface BlogMapper {
    /**
     * 根据主键查询文章
     * @param bid
     * @return
     */
    public Blog selectBlogById(Integer bid);
}
