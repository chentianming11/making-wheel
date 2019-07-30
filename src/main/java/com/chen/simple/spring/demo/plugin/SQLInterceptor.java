package com.chen.simple.spring.demo.plugin;


import com.chen.simple.spring.framework.mybatis.executor.statement.StatementHandler;
import com.chen.simple.spring.framework.mybatis.plugin.*;

import java.sql.Statement;
/**
 * @Author: qingshan
 * @Date: 2019/4/2 21:37
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "query", args = { Statement.class}) })
public class SQLInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        String sql = statementHandler.getSql();
        System.out.println("获取到SQL语句："+sql);

        try {
            return invocation.proceed();
        }finally {
            long endTime = System.currentTimeMillis();
            System.out.println("SQL执行耗时：" + (endTime-startTime) +"ms");
        }

    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

}
