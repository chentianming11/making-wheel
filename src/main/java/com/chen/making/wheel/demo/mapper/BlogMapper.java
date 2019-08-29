package com.chen.making.wheel.demo.mapper;


import com.chen.making.wheel.demo.entity.Blog;


public interface BlogMapper {
    /**
     * 根据主键查询文章
     * @param bid
     * @return
     */
    Blog selectBlogById(Integer bid);
}
