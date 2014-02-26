package com.lrtech.ibatistools.metadata;

import com.lrtech.ibatistools.bean.Table;

import java.sql.Connection;
import java.util.List;

/**
 * 数据库元信息接口
 *
 * @author leizhimin 11-12-20 下午2:30
 */
public interface DataSourceMetaData {

    /**
     * 获取整个数据库表元信息
     *
     * @param conn 数据库连接
     * @return 整个数据库表元信息
     */
    List<Table> getAllTableMetaData(Connection conn);

    /**
     * 获取指定表的元信息
     *
     * @param conn      数据库连接
     * @param tableName 表名称
     * @return 指定表的元信息
     */
    Table getOneTableMetaData(Connection conn, String tableName);

    /**
     * 获取指定一系列表的元信息
     *
     * @param conn           数据库连接
     * @param tableNameArray 表名称，可以连续写多个，也可以传递一个String[]
     * @return 一系列表的元信息
     */
    List<Table> getSomeTableMetaData(Connection conn, String... tableNameArray);

}
