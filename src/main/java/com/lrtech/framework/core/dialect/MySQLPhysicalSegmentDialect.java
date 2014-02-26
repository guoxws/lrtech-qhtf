package com.lrtech.framework.core.dialect;

/**
 * 物理分页方言MySQL的实现，不同数据库对应不同的物理分页实现.
 *
 * @author leizhimin 11-12-13 下午9:23
 */
public class MySQLPhysicalSegmentDialect implements PhysicalSegmentDialect {
    /**
     * 判断一条SQL语句是否已经是分页的SQL
     *
     * @param sql 源SQL
     * @return 是已经分页的SQL时返回ture，否则返回False；
     */
    public boolean isAlreadySegmentSQL(String sql) {
        return sql.replace('\r', ' ').replace('\n', ' ').replaceAll(" {2,}", " ").matches("(?i).+LIMIT [\\d+ *|\\d *, *\\d+].+");
    }

    /**
     * 根据普通未分页SQL获取分页SQL，从第0条开始（不含第0条），取size条数据
     *
     * @param sql     普通未分页SQL
     * @param rowSize 获取的数据记录数
     * @return 返回物理分页后的SQL
     */
    public String getSegmentSQL(String sql, boolean rowSize) {
        StringBuffer sb = new StringBuffer(sql.trim().replace('\r', ' ').replace('\n', ' ').replaceAll(" {2,}", " "));
        return sb.append(" limit ").append(rowSize).append(";").toString();
    }

    /**
     * 根据普通未分页SQL获取分页SQL，从第startRow条开始（不含第startRow条），取size条数据
     *
     * @param sql      普通未分页SQL
     * @param rowSize  获取的数据记录数
     * @param startRow 开始行数
     * @return 返回物理分页后的SQL
     */
    public String getSegmentSQL(String sql, int startRow, int rowSize) {
        StringBuffer sb = new StringBuffer(sql.trim().replace('\r', ' ').replace('\n', ' ').replaceAll(" {2,}", " "));
        return sb.append(" limit ").append(startRow).append(", ").append(rowSize).append(";").toString();
    }

}
