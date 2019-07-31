package com.chen.simple.spring;

import com.chen.simple.spring.demo.entity.Blog;
import com.chen.simple.spring.demo.mapper.BlogMapper;
import com.chen.simple.spring.framework.mybatis.session.SqlSession;
import com.chen.simple.spring.framework.mybatis.session.SqlSessionFactory;
import com.chen.simple.spring.framework.mybatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * @author 陈添明
 * @date 2019/7/27
 */
public class MybatisTest {


    @Test
    public void test() throws IOException {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build("mybatis/config/mybatis.properties");
        SqlSession session = sqlSessionFactory.openSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);
        Blog blog = mapper.selectBlogById(1);
        System.out.println(blog);
        System.out.println("=============");
        Blog blog2 = mapper.selectBlogById(1);
        System.out.println(blog2);
    }

    @Test
    public void test2() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build("mybatis/config/mybatis.properties");
        SqlSession session = sqlSessionFactory.openSession();
        Blog blog = session.selectOne("com.chen.simple.spring.demo.mapper.BlogMapper.selectBlogById", 1);
        System.out.println(blog);

        System.out.println("=============");
        SqlSession session2 = sqlSessionFactory.openSession();
        Blog blog2 = session2.selectOne("com.chen.simple.spring.demo.mapper.BlogMapper.selectBlogById", 1);
        System.out.println(blog2);
    }
}
