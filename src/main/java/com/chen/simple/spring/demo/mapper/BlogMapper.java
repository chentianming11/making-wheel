package com.chen.simple.spring.demo.mapper;


import com.chen.simple.spring.demo.entity.Blog;


public interface BlogMapper {
    /**
     * 根据主键查询文章
     * @param bid
     * @return
     */
    public Blog selectBlogById(Integer bid);
}
