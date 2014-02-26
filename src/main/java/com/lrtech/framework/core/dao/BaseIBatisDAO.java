package com.lrtech.framework.core.dao;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.sql.stat.StaticSql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.lrtech.framework.common.util.ReflectUtil;
import com.lrtech.framework.core.GenericEntity;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 通用DAO的iBatis实现，这个适用范围相对WebBaseIBatisDAO更为广泛，这个更适合用于纯Java的应用中.
 *
 * @author leizhimin 11-12-12 下午10:42
 */
public abstract class BaseIBatisDAO<E, PK extends Serializable>
        extends SqlMapClientDaoSupport
        implements GenericDAO<E, PK> {

    protected String sqlmapNamespace;   //ibatis sql map的命名空间，即使用实体类的简单名称

    protected Class entityType;         //运行时的实体类型，也对应为SQL的命名空间。

    private SqlExecutor sqlExecutor;    //执行器

    public SqlExecutor getSqlExecutor() {
        return sqlExecutor;
    }

    public void setSqlExecutor(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    /**
     * 通过反射获取传递给超类的泛型
     */
    public BaseIBatisDAO() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        entityType = (Class) pt.getActualTypeArguments()[0];
        sqlmapNamespace = entityType.getSimpleName();
    }

    public void initialize() throws Exception {
        if (sqlExecutor != null) {
            SqlMapClient sqlMapClient = getSqlMapClientTemplate().getSqlMapClient();
            if (sqlMapClient instanceof ExtendedSqlMapClient) {
                ReflectUtil.setFieldValue(((ExtendedSqlMapClient) sqlMapClient)
                        .getDelegate(), "sqlExecutor", SqlExecutor.class, sqlExecutor);
            }
        }
    }

    /**
     * 根据SqlMap的ID串获取运行时静态SQL
     *
     * @param statementId SqlMap的ID
     * @return 运行时静态SQL
     */
    protected String getExecuteSql(String statementId) {
        SqlMapClientImpl sqlmap = (SqlMapClientImpl) this.getSqlMapClient();
        MappedStatement stmt = sqlmap.getMappedStatement(sqlmapNamespace + "." + statementId);
        StaticSql stmtSql = (StaticSql) stmt.getSql();
        String runtimeSQL = stmtSql.getSql(null, null);
        return runtimeSQL;
    }

    public E insert(E entity) {
        Object id = (Long) getSqlMapClientTemplate().insert(sqlmapNamespace + "." + SQLID_INSERT, entity);
        if (id != null && entity instanceof GenericEntity)
            ((GenericEntity) entity).setId((Long) id);
        return entity;
    }

    public E update(E entity) {
        getSqlMapClientTemplate().update(sqlmapNamespace + "." + SQLID_UPDATE, entity);
        return entity;
    }

    public E merage(E entity) {
        if (entity instanceof GenericEntity) {
            GenericEntity e = (GenericEntity) entity;
            if (e.getId() == null) {
                Object id = (Long) getSqlMapClientTemplate().insert(sqlmapNamespace + "." + SQLID_INSERT, entity);
                if (id != null && entity instanceof GenericEntity)
                    ((GenericEntity) entity).setId((Long) id);
                return entity;
            } else {
                update(entity);
                return entity;
            }
        } else {
            throw new RuntimeException("merage方法只适用于GenericEntity类型的实体！请检查你的参数是否符合条件！");
        }
    }

    public void delete(PK pk) {
        getSqlMapClientTemplate().delete(sqlmapNamespace + "." + SQLID_DELETE, pk);
    }

    public int batchInsert(final Collection<E> entitySet) {
        SqlMapClientCallback callback = new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                executor.startBatch();
                for (E entity : entitySet) {
                    executor.insert(sqlmapNamespace + "." + GenericDAO.SQLID_INSERT, entity);
                }
                return executor.executeBatch();
            }
        };
        return (Integer) this.getSqlMapClientTemplate().execute(callback);
    }

    public int batchUpdate(final Collection<E> entitySet) {
        SqlMapClientCallback callback = new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor)
                    throws SQLException {
                executor.startBatch();
                for (E entity : entitySet) {
                    executor.update(sqlmapNamespace + "." + SQLID_UPDATE, entity);
                }
                return executor.executeBatch();
            }
        };
        return (Integer) this.getSqlMapClientTemplate().execute(callback);
    }

    public int batchUpdate(final String stmtId, final Collection<Map<String, Object>> listMap) {
        SqlMapClientCallback callback = new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor)
                    throws SQLException {
                executor.startBatch();
                for (Map<String,Object> map : listMap) {
                    executor.update(sqlmapNamespace + "." + stmtId, map);
                }
                return executor.executeBatch();
            }
        };
        return (Integer) this.getSqlMapClientTemplate().execute(callback);
    }

    public int batchDelete(final Collection<PK> keySet) {
        SqlMapClientCallback callback = new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                executor.startBatch();
                for (PK pk : keySet) {
                    executor.delete(sqlmapNamespace + "." + SQLID_DELETE, pk);
                }
                return executor.executeBatch();
            }
        };
        return (Integer) this.getSqlMapClientTemplate().execute(callback);
    }

    public List<E> query(Map<String, Object> params) {
        return getSqlMapClientTemplate().queryForList(sqlmapNamespace + "." + SQLID_QUERY, params);
    }


    public E load(PK pk) {
        return (E) getSqlMapClientTemplate().queryForObject(sqlmapNamespace + "." + SQLID_LOAD, pk);
    }

    public int count(Map<String, Object> params) {
        return (Integer) getSqlMapClientTemplate().queryForObject(sqlmapNamespace + "." + SQLID_COUNT, params);
    }

    public List<E> query(Map<String, Object> params, int startRow, int rowSize) {
        return getSqlMapClientTemplate().queryForList(sqlmapNamespace + "." + SQLID_QUERY, params, startRow, rowSize);
    }


    public <T> List<T> query(String stmtId, Map<String, Object> params) {
        return getSqlMapClientTemplate().queryForList(sqlmapNamespace + "." + stmtId, params);
    }

    public int count(String stmtId, Map<String, Object> params) {
        return (Integer) getSqlMapClientTemplate().queryForObject(sqlmapNamespace + "." + stmtId, params);
    }

    public <T> List<T> query(String stmtId, Map<String, Object> params, int startRow, int rowSize) {
        return getSqlMapClientTemplate().queryForList(sqlmapNamespace + "." + stmtId, params, startRow, rowSize);
    }

//    @Override
//    public void execute(String sql, Object param) {
//        getSqlMapClientTemplate().getDataSource().getConnection().e
//    }
}
