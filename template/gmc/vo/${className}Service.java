package com.gm.gmportal.mygm.${mvoPackage}.service;

<#assign className= table.className>    
<#assign classNameFirstLower= table.classNameFirstLower>   

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import com.gm.soa.common.vo.PaginationVO;
import com.gm.soa.vo.common.PaginationCondition;
import com.gm.soa.vo.${soaCorePackage}.${className}VO;
public interface ${className}Service{
    
    <#if gh.fromTable>
    /**
     * 新增记录
     */
    public Long add${className}(<#list table.notPkColumns as column>${column.javaType} ${column.columnNameLower}<#if column_has_next>,</#if></#list>,Long currentCompId,Long currentContactId,Long currentUserId)throws Exception;
    
    /**
     * 删除记录
     */
    public boolean delete${className}ById(<#list table.pkColumns as column>${column.javaType} ${column.columnNameLower}</#list>,Long currentCompId,Long currentContactId,Long currentUserId)throws Exception;
    
    /**
     * 更新记录
     */
    public boolean update${className}ById(<#list table.pkColumns as column>${column.javaType} ${column.columnNameLower}</#list>,<#list table.updateColumns as column>${column.javaType} ${column.columnNameLower}<#if column_has_next>,</#if></#list>,Long currentCompId,Long currentContactId,Long currentUserId)throws Exception;
    </#if>
    
    /**
     * 根据id查询记录
     */
    public ${className}VO get${className}ById(<#list table.pkColumns as column>${column.javaType} ${column.columnNameLower}<#if column_has_next>,</#if></#list>,Long currentCompId,Long currentContactId,Long currentUserId)throws Exception;
    
    /**
     * 查询记录
     */
    public PaginationVO<${className}VO> getPagination${className}ByParam(<#list table.queryColumns as column>${column.javaType} ${column.columnNameLower},</#list>PaginationCondition pc,Long currentCompId,Long currentContactId,Long currentUserId)throws Exception;
    /**
     * 查询总数
     */
    public int getTotal${className}ByParam(<#list table.queryColumns as column>${column.javaType} ${column.columnNameLower},</#list>Long currentCompId,Long currentContactId,Long currentUserId)throws Exception;
}