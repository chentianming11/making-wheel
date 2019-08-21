package com.chen.yugong.framework.mybatis.executor.statement;

import com.chen.yugong.framework.mybatis.executor.Executor;
import com.chen.yugong.framework.mybatis.executor.ParameterHandler;
import com.chen.yugong.framework.mybatis.executor.result.ResultSetHandler;
import com.chen.yugong.framework.mybatis.mapping.MappedStatement;
import com.chen.yugong.framework.mybatis.session.Configuration;
import com.chen.yugong.framework.mybatis.session.RowBounds;
import com.chen.yugong.framework.mybatis.type.TypeHandlerRegistry;
import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author 陈添明
 * @date 2019/7/27
 */
@Data
public class PreparedStatementHandler implements StatementHandler {

    protected final Configuration configuration;
//    protected final ObjectFactory objectFactory;
    protected final TypeHandlerRegistry typeHandlerRegistry;
    protected final ResultSetHandler resultSetHandler;
    protected final ParameterHandler parameterHandler;

    protected final Executor executor;
    protected final MappedStatement mappedStatement;
    protected final RowBounds rowBounds;

    protected final String sql;



    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds) {
        this.executor = executor;
        this.configuration = mappedStatement.getConfiguration();
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;
        this.typeHandlerRegistry = mappedStatement.getTypeHandlerRegistry();
        this.parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject);
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, rowBounds, parameterHandler);
        this.sql = mappedStatement.getSql();
    }

    @Override
    public <E> List<E> query(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.handleResultSets(ps);
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = instantiateStatement(connection);
            return statement;
        } catch (SQLException e) {
            closeStatement(statement);
            throw e;
        } catch (Exception e) {
            closeStatement(statement);
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }

    /**
     * 实例化Statement对象
     * @param connection
     * @return
     * @throws SQLException
     */
    private Statement instantiateStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(mappedStatement.getSql());
        return preparedStatement;
    }

    protected void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            //ignore
        }
    }

    /**
     * 参数化处理
     * @param statement
     */
    @Override
    public void parameterize(Statement statement) throws SQLException {
        parameterHandler.setParameters((PreparedStatement) statement);
    }

    @Override
    public String getSql() {
        return sql;
    }
}
