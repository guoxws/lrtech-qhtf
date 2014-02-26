package com.lrtech.ibatistools.metadata;

import com.lrtech.ibatistools.bean.Column;
import com.lrtech.ibatistools.bean.Table;
import com.lrtech.ibatistools.common.DBTools;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQL
 *
 * @author leizhimin 11-12-20 下午3:03
 */
public class MySQLDataSourceMetaData implements DataSourceMetaData {
    private static DataSourceMetaData instance;
    private static Map<String, String> regList = new HashMap<String, String>();

    static {
        regList.put("(?i)bit\\(1\\)\\z", "boolean");
        regList.put("(?i)bit\\((\\d{2,}|[2-9])\\)\\z", "byte[]");
        regList.put("(?i)tinyint.*", "int");
        regList.put("(?i)bool(ean){0,1}", "boolean");
        regList.put("(?i)smallint.*", "int");
        regList.put("(?i)mediumint.*", "int");
        regList.put("(?i)int(eger)?(\\(\\d+\\))?\\z", "int");
        regList.put("(?i)int(eger)?(\\(\\d+\\))? +unsigned", "long");
        regList.put("(?i)bigint(\\(\\d+\\))?\\z", "long");
        regList.put("(?i)bigint(\\(\\d+\\))? +unsigned", "java.math.BigInteger");
        regList.put("(?i)float.*", "float");
        regList.put("(?i)double.*", "double");
        regList.put("(?i)decimal.*", "java.math.BigDecimal");
        regList.put("(?i)date\\z", "java.sql.Date");
        regList.put("(?i)datetime\\z", "java.sql.Timestamp");
        regList.put("(?i)timestamp.*", "java.sql.Timestamp");
        regList.put("(?i)time\\z", "java.sql.Time");
        regList.put("(?i)year\\(\\d+\\)\\z", "java.sql.Date");
        regList.put("(?i)char\\(\\d+\\)\\z", "String");
        regList.put("(?i)varchar(\\(\\d+\\))?\\z", "String");
        regList.put("(?i)varchar(\\(\\d+\\))? +binary", "byte[]");
        regList.put("(?i)binary(\\(\\d+\\))?\\z", "byte[]");
        regList.put("(?i)varbinary(\\(\\d+\\))?\\z", "byte[]");
        regList.put("(?i)tinyblob\\z", "byte[]");
        regList.put("(?i)tinytext\\z", "String");
        regList.put("(?i)blob\\z", "byte[]");
        regList.put("(?i)text\\z", "String");
        regList.put("(?i)mediumblob\\z", "byte[]");
        regList.put("(?i)mediumtext\\z", "String");
        regList.put("(?i)longblob\\z", "byte[]");
        regList.put("(?i)longtext\\z", "String");
        regList.put("(?i)enum.*\\z", "String");
        regList.put("(?i)set.*\\z", "String");
    }

    private static String all_table_sql = "" +
            "SELECT t.table_name, t.table_comment, t.create_time\n" +
            "  FROM information_schema.tables t\n" +
            " WHERE t.table_schema = SCHEMA()";

    private static String one_table_sql = "" +
            "SELECT t.column_name,\n" +
            "       t.data_type,\n" +
            "       CAST(SUBSTR(t.column_type, INSTR(t.column_type, '(') + 1, INSTR(t.column_type,')') - INSTR(t.column_type, '(') - 1) AS CHAR(20)) data_length,\n" +
            "       CAST(t.column_type AS CHAR(20)) column_type,\n" +
            "       t.column_comment,\n" +
            "       IF (t.is_nullable='YES',1,0) is_nullable,\n" +
            "       IF (t.column_key = 'PRI', 1, 0) is_key\n" +
            "FROM information_schema.columns t\n" +
            "WHERE t.table_schema = SCHEMA() AND\n" +
            "      t.table_name = ?\n" +
            "ORDER BY t.ordinal_position;";

    private MySQLDataSourceMetaData() {
    }

    public static DataSourceMetaData instatnce() {
        if (instance == null) instance = new MySQLDataSourceMetaData();
        return instance;
    }

    /**
     * 获取整个数据库表元信息
     *
     * @param conn 数据库连接
     * @return 整个数据库表元信息
     */
    public List<Table> getAllTableMetaData(Connection conn) {
        List<Table> tableList = new ArrayList<Table>();
        Statement stmt = null;
        PreparedStatement pstmt1 = null;
        try {
            stmt = conn.createStatement();
            pstmt1 = conn.prepareStatement(one_table_sql);
            ResultSet rs = stmt.executeQuery(all_table_sql);
            while (rs.next()) {
                Table table = new Table();
                table.setName(rs.getString(1));
                table.setComment(rs.getString(2));
                table.setCreatetime(rs.getTimestamp(3));
                pstmt1.clearParameters();
                pstmt1.setString(1, table.getName());
                ResultSet rs1 = pstmt1.executeQuery();
                while (rs1.next()) {
                    Column column = new Column();
                    column.setName(rs1.getString("column_name"));
                    column.setComment(rs1.getString("column_comment"));
                    column.setType(rs1.getString("data_type"));
                    column.setFullType(rs1.getString("column_type"));
                    column.setJavaType(getJavaClassType(column.getFullType()));
                    column.setLength(rs1.getInt("data_length"));
                    column.setNotNull(rs1.getInt("is_nullable") == 1 ? true : false);
                    column.setPk(rs1.getInt("is_key") == 1 ? true : false);
                    if (column.isPk() && column.getJavaType().equals("long")) column.setJavaType("Long");
                    table.getColumnList().add(column);
                }
                rs1.close();
                ResultSet rs2 = pstmt1.executeQuery("SHOW CREATE TABLE " + table.getName());
                if (rs2.next()) table.setCreateTableSQL(rs2.getString(2));
                rs2.close();
                tableList.add(table);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBTools.closeAll(stmt, pstmt1, conn);
        }
        return tableList;
    }


    /**
     * 获取指定表的元信息
     *
     * @param conn      数据库连接
     * @param tableName 表名称
     * @return 指定表的元信息
     */
    public Table getOneTableMetaData(Connection conn, String tableName) {
        Table table = null;
        Statement stmt = null;
        PreparedStatement pstmt1 = null;
        try {
            pstmt1 = conn.prepareStatement(one_table_sql);
            pstmt1.setString(1, tableName);
            ResultSet rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                table = new Table();
                Column column = new Column();
                column.setName(rs1.getString("column_name"));
                column.setComment(rs1.getString("column_comment"));
                column.setType(rs1.getString("data_type"));
                column.setFullType(rs1.getString("column_type"));
                column.setLength(rs1.getInt("data_length"));
                column.setNotNull(rs1.getInt("is_nullable") == 1 ? true : false);
                column.setPk(rs1.getInt("is_key") == 1 ? true : false);
                table.getColumnList().add(column);
            }
            rs1.close();
            ResultSet rs2 = pstmt1.executeQuery("SHOW CREATE TABLE " + table.getName());
            if (rs2.next()) table.setCreateTableSQL(rs2.getString(2));
            rs2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBTools.closeAll(stmt, pstmt1, conn);
        }
        return table;
    }

    /**
     * 获取指定一系列表的元信息
     *
     * @param conn           数据库连接
     * @param tableNameArray 表名称，可以连续写多个，也可以传递一个String[]
     * @return 一系列表的元信息
     */
    public List<Table> getSomeTableMetaData(Connection conn, String... tableNameArray) {
        List<Table> tableList = new ArrayList<Table>();
        Statement stmt = null;
        PreparedStatement pstmt1 = null;
        try {
            stmt = conn.createStatement();
            pstmt1 = conn.prepareStatement(one_table_sql);
            for (String tableName : tableNameArray) {
                Table table = new Table(tableName);
                pstmt1.clearParameters();
                pstmt1.setString(1, table.getName());
                ResultSet rs1 = pstmt1.executeQuery();
                while (rs1.next()) {
                    Column column = new Column();
                    column.setName(rs1.getString("column_name"));
                    column.setComment(rs1.getString("column_comment"));
                    column.setType(rs1.getString("data_type"));
                    column.setFullType(rs1.getString("column_type"));
                    column.setLength(rs1.getInt("data_length"));
                    column.setNotNull(rs1.getInt("is_nullable") == 1 ? true : false);
                    column.setPk(rs1.getInt("is_key") == 1 ? true : false);
                    table.getColumnList().add(column);
                }
                rs1.close();
                ResultSet rs2 = pstmt1.executeQuery("SHOW CREATE TABLE " + table.getName());
                if (rs2.next()) table.setCreateTableSQL(rs2.getString(2));
                rs2.close();
                tableList.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBTools.closeAll(stmt, pstmt1, conn);
        }
        return tableList;
    }

    public static String getJavaClassType(String columnType) {
        for (Map.Entry<String, String> entry : regList.entrySet()) {
            if (columnType.matches(entry.getKey())) return entry.getValue();
        }
        return "unknown";
    }


    public static void main(String[] args) {
        System.out.println(getJavaClassType("int unsigned"));
        System.out.println(getJavaClassType("integer  unsigned"));
        System.out.println(getJavaClassType("int"));
        System.out.println(getJavaClassType("integer"));
        System.out.println(getJavaClassType("integer(2)"));
        System.out.println(getJavaClassType("int(2)"));
        System.out.println(getJavaClassType("mediumint(2)"));
        System.out.println(getJavaClassType("smallint(2)"));
        System.out.println(getJavaClassType("tinyint(2)"));
        System.out.println(getJavaClassType("bit(1)"));
        System.out.println(getJavaClassType("bit(2)"));
        System.out.println(getJavaClassType("bit(10)"));
        System.out.println(getJavaClassType("bit(222)"));

//        System.out.println(one_table_sql);
//        System.out.println(all_table_sql);
//        System.out.println("SHOW CREATE TABLE foo");
    }
}
