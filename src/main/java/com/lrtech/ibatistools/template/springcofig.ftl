<?xml version="1.0" encoding="GBK"?>

<beans default-autowire="byName"
       xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">


    <bean id="propertyConfig" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="locations">
            <list>
                <value>classpath:jdbc.properties</value>
            </list>
        </property>
    </bean>

    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
        <property name="driverClassName">
            <value>$\{jdbc.driver}</value>
        </property>
        <property name="url">
            <value>$\{jdbc.url}</value>
        </property>
        <property name="username">
            <value>$\{jdbc.username}</value>
        </property>
        <property name="password">
            <value>$\{jdbc.password}</value>
        </property>
    </bean>

    <!--根据dataSource和configLocation创建一个SqlMapClient-->
    <bean id="sqlMapClient"
          class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">
        <property name="configLocation" value="classpath:sqlMapConfig.xml"/>
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <bean id="mysqlDialect" class="com.lavasoft.freamwork.core.dialect.MySQLPhysicalSegmentDialect"/>
    <bean id="sqlExecutor" class="com.lavasoft.freamwork.core.PhysicalSegmentSqlExecutor">
        <property name="dialect" ref="mysqlDialect"/>
    </bean>

    <bean id="baseDao" abstract="true" class="com.lavasoft.freamwork.core.dao.BaseIBatisDAO" init-method="initialize">
        <property name="dataSource">
            <ref bean="dataSource"/>
        </property>
        <property name="sqlMapClient">
            <ref bean="sqlMapClient"/>
        </property>
        <property name="sqlExecutor">
            <ref bean="sqlExecutor"/>
        </property>
    </bean>

<#list tableNameList as tabName>
    <bean id="${tabName}DAO" class="${daoPackage}.${tabName?cap_first}DAO" parent="baseDao"/>
</#list>

<#--<#list tableNameList as tabName>-->
    <#--<sqlMap resource="${daoPackage?replace(".","/")}/${tabName?cap_first}.xml"/>-->
<#--</#list>-->

</beans>