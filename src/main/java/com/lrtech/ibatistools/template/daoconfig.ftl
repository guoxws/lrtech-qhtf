<#list tableNameList as tabName>
    <bean id="${tabName}DAO" class="${daoPackage}.${tabName?cap_first}DAO" parent="baseDao"/>
</#list>

<#list tableNameList as tabName>
    <sqlMap resource="${sqlmapPackage?replace(".","/")}/${tabName?cap_first}.xml"/>
</#list>

