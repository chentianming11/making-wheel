package com.chen.yugong.framework.mybatis.executor;

import com.chen.yugong.framework.mybatis.executor.statement.StatementHandler;
import com.chen.yugong.framework.mybatis.mapping.MappedStatement;
import com.chen.yugong.framework.mybatis.session.Configuration;
import com.chen.yugong.framework.mybatis.session.RowBounds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author 陈添明
 * @date 2019/7/27
 */
public class SimpleExecutor implements Executor {

    protected Executor wrapper;

    protected Configuration configuration;

    public SimpleExecutor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
        Statement stmt = null;
        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(wrapper, ms, parameter, rowBounds);
            stmt = prepareStatement(handler);
            return handler.query(stmt);
        } finally {
            closeStatement(stmt);
        }
    }

    /**
     * 预处理语句
     * @param handler
     * @return
     */
    private Statement prepareStatement(StatementHandler handler) throws SQLException {
        Statement stmt;
        Connection connection = getConnection();
        stmt = handler.prepare(connection);
        handler.parameterize(stmt);
        return stmt;
    }


    protected void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }


    /**
     * 获取连接
     * @return
     * @throws SQLException
     */
    private Connection getConnection() {
        String driver = configuration.properties.getProperty("jdbc.driver");
        String url =  configuration.properties.getProperty("jdbc.url");
        String username = configuration.properties.getProperty("jdbc.username");
        String password = configuration.properties.getProperty("jdbc.password");
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
