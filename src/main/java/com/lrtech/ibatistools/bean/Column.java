package com.lrtech.ibatistools.bean;

/**
 * 表的列
 *
 * @author leizhimin 11-12-14 下午6:00
 */
public class Column {
    private String name;        //名称
    private String comment;     //注释
    private String type;        //数据类型
    private String fullType;    //完整数据类型
    private String javaType;    //对应的java数据类型
    private int length;         //长度
    private boolean notNull;    //是否可空
    private boolean pk;         //是否主键字段

    public Column() {
    }

    public Column(String name) {
        this.name = name;
    }

    public Column(String name, String comment) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullType() {
        return fullType;
    }

    public void setFullType(String fullType) {
        this.fullType = fullType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", type='" + type + '\'' +
                ", length=" + length +
                ", notNull=" + notNull +
                ", pk=" + pk +
                '}';
    }
}