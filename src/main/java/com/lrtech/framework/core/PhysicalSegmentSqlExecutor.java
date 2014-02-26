package com.lrtech.framework.core;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.lrtech.framework.core.dialect.PhysicalSegmentDialect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 实现了物理分页的SQL执行器
 *
 * @author leizhimin 11-12-15 上午2:58
 */
public class PhysicalSegmentSqlExecutor extends SqlExecutor {
    private static final Log logger = LogFactory.getLog(PhysicalSegmentSqlExecutor.class);

    private PhysicalSegmentDialect dialect;

    public PhysicalSegmentDialect getDialect() {
        return dialect;
    }

    public void setDialect(PhysicalSegmentDialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public void executeQuery(StatementScope request, Connection conn, String sql,
                             Object[] parameters, int skipResults, int maxResults,
                             RowHandlerCallback callback) throws SQLException {
        if ((skipResults != NO_SKIPPED_RESULTS || maxResults != NO_MAXIMUM_RESULTS)) {
            if (!dialect.isAlreadySegmentSQL(sql)) {
                sql = dialect.getSegmentSQL(sql, skipResults, maxResults);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(sql);
            }
            skipResults = NO_SKIPPED_RESULTS;
            maxResults = NO_MAXIMUM_RESULTS;
        }
        super.executeQuery(request, conn, sql, parameters, skipResults, maxResults, callback);
    }
}
