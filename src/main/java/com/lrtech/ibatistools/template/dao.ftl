<#assign package=daoPackage/>
<#assign className=name?cap_first/>
<#assign tableName=name/>
package ${package};

import com.lrtech.framework.core.dao.BaseIBatisDAO;
import ${entityPackage}.${className};

/**
* ${tableName}, ${comment}
*
* @author leizhimin
*/
public class ${className}DAO extends BaseIBatisDAO<${className}, Long> {
}