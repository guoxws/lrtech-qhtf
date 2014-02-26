<?xml version="1.0" encoding="GBK"?>
<!DOCTYPE sqlMap PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN"
        "http://ibatis.apache.org/dtd/sql-map-2.dtd" >
<#assign package=entityPackage/>
<#assign className=name?cap_first/>
<#assign tableName=name/>
<#assign columnSize=columnList?size/>
<!-- ${tableName}: ${comment}-->
<sqlMap namespace="${className}">
    <typeAlias alias="${className}" type="${package}.${className}"/>
    <resultMap id="result_base" class="${className}">
    <#list columnList as column>
        <result property="${column.name}" column="${column.name}"/>
    </#list>
    </resultMap>

    <insert id="insert" parameterClass="${className}">
        insert into ${tableName}(
    <#list columnList as column>
        <#if column.name=="id" && column.pk>
        <#else>
            ${column.name}<#if (column_index<columnSize-1)>,</#if>
        </#if>
    </#list>
        ) values(
    <#list columnList as column>
        <#if column.name=="id" && column.pk>
        <#else>
            #${column.name}#<#if (column_index<columnSize-1)>,</#if>
        </#if>
    </#list>
        )
        <selectKey keyProperty="id" resultClass="long">
            select LAST_INSERT_ID()
        </selectKey>
    </insert>

    <update id="update" parameterClass="${className}">
        update ${tableName} set
    <#list columnList as column>
        <#if column.name=="id" && column.pk>
        <#else>
            ${column.name}=#${column.name}#<#if (column_index<columnSize-1)>,</#if>
        </#if>
    </#list>
        where id = #id#
    </update>

    <delete id="delete" parameterClass="long">
        delete from ${tableName} where id=#value#
    </delete>

    <select id="load" parameterClass="long" resultClass="${className}" resultMap="${className}.result_base">
        select * from ${tableName} where id=#value#
    </select>

    <sql id="sql_query_where">
        <dynamic prepend="where">
        <#list columnList as column>
            <#if column.name=="id" && column.pk>
            <#else>
            <isNotEmpty prepend="and" property="${column.name}">
                ${column.name}=#${column.name}#
            </isNotEmpty>
            </#if>
        </#list>
        </dynamic>
    </sql>

    <select id="query" parameterClass="map" resultMap="${className}.result_base">
        select * from ${tableName}
        <include refid="sql_query_where"/>
        <dynamic prepend="">
            <isNotEmpty property="sortColumns">
                order by $sortColumns$
            </isNotEmpty>
        </dynamic>
    </select>

    <select id="count" parameterClass="map" resultClass="int">
        select count(1) from ${tableName}
        <include refid="sql_query_where"/>
    </select>
</sqlMap>
