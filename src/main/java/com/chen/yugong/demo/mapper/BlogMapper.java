package com.chen.yugong.demo.mapper;


import com.chen.yugong.demo.entity.Blog;


public interface BlogMapper {
    /**
     * 根据主键查询文章
     * @param bid
     * @return
     */
    Blog selectBlogById(Integer bid);
}
