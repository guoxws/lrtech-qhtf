<#assign rootPackage="com.lavasoft.dxbk"/>
<#assign package=rootPackage+".entity"/>
<#assign className=name?cap_first/>
<#assign tableName=name/>
package ${package};

import com.lrtech.framework.core.GenericEntity;

import java.io.Serializable;

/**
* ${comment}
*
* @author leizhimin
*/
public class ${className} extends GenericEntity implements Serializable{
<#list columnList as column>
    <#if column.name=="id" && column.pk>
    <#else>
    private ${column.javaType} ${column.name};          //${column.comment}
    </#if>
</#list>

    public ${className}() {
    }
<#list columnList as column>
    <#if column.name=="id" && column.pk>
    <#else>
    public ${column.javaType} get${column.name?cap_first}(){
        return this.${column.name};
    }

    public void set${column.name?cap_first}(${column.javaType} ${column.name}){
        this.${column.name}=${column.name};
    }
    </#if>
</#list>
}