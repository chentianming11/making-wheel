package com.chen.making.wheel;

import com.chen.making.wheel.demo.entity.Blog;
import com.chen.making.wheel.demo.mapper.BlogMapper;
import com.chen.making.wheel.framework.mybatis.session.SqlSession;
import com.chen.making.wheel.framework.mybatis.session.SqlSessionFactory;
import com.chen.making.wheel.framework.mybatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * @author 陈添明
 * @date 2019/7/27
 */
public class MybatisTest {


    @Test
    public void test() throws IOException {
        // 构建sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build("mybatis/config/mybatis.properties");
        // 获取一个SqlSession
        SqlSession session = sqlSessionFactory.openSession();
        // 获取Mapper接口
        BlogMapper mapper = session.getMapper(BlogMapper.class);
        // 调用Mapper接口方法，执行sql
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
        Blog blog = session.selectOne("com.chen.making.wheel.demo.mapper.BlogMapper.selectBlogById", 1);
        System.out.println(blog);

        System.out.println("=============");
        SqlSession session2 = sqlSessionFactory.openSession();
        Blog blog2 = session2.selectOne("com.chen.making.wheel.demo.mapper.BlogMapper.selectBlogById", 1);
        System.out.println(blog2);
    }
}
