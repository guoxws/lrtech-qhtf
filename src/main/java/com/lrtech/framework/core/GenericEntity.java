package com.lrtech.framework.core;

/**
 * 通用id，所有具有id属性的实体，一律继承此类才能执行BaseIBatisDAO所提供的方法.<P/>
 * 在开发中，表的id必须命名为id，对应的类型为Java中long的对等类型，起始的id数从10000开始，
 * 不提倡用超大型的无符号的大数据类型做主键.<p/>
 *
 * @author leizhimin 11-12-13 上午1:05
 */
public class GenericEntity {
    private Long id;

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
