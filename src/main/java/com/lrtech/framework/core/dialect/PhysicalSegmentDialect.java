package com.lrtech.framework.core.dialect;

/**
 * 物理分页方言，不同数据库对应不同的物理分页实现.
 *
 * @author leizhimin 11-12-13 下午9:25
 */
public interface PhysicalSegmentDialect {
    /**
     * 判断一条SQL语句是否已经是分页的SQL
     *
     * @param sql 源SQL
     * @return 是已经分页的SQL时返回ture，否则返回False；
     */
    boolean isAlreadySegmentSQL(String sql);

    /**
     * 根据普通未分页SQL获取分页SQL，从第一条开始，取size条数据
     *
     * @param sql     普通未分页SQL
     * @param rowSize 获取的数据记录数
     * @return 返回物理分页后的SQL
     */
    String getSegmentSQL(String sql, boolean rowSize);

    /**
     * 根据普通未分页SQL获取分页SQL，从第startRow条开始，取size条数据
     *
     * @param sql      普通未分页SQL
     * @param rowSize  获取的数据记录数
     * @param startRow 开始行数
     * @return 返回物理分页后的SQL
     */
    String getSegmentSQL(String sql, int startRow, int rowSize);

}
