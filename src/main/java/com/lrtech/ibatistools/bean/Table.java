package com.lrtech.ibatistools.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 表
 *
 * @author leizhimin 11-12-13 下午5:13
 */
public class Table {
    private String name;                    //名称
    private String comment;                 //表注释
    private String createTableSQL;          //建表SQL语句
    private List<Column> columnList = new ArrayList<Column>();      //列字段信息
    private Date createtime;      //创建时间

    public Table() {
    }

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateTableSQL() {
        return createTableSQL;
    }

    public void setCreateTableSQL(String createTableSQL) {
        this.createTableSQL = createTableSQL;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", createTableSQL='" + createTableSQL + '\'' +
                ", columnList=" + columnList +
                ", createtime=" + createtime +
                '}';
    }
}

