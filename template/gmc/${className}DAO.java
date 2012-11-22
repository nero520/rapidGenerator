package com.gm.soa.dao.${soaSrcPackage};
<#assign className= table.className>    
<#assign classNameFirstLower= table.classNameFirstLower>  

import com.gm.soa.vo.${soaCorePackage}.${className}VO;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.gm.soa.common.params.PaginationParams;
import com.gm.soa.dao.BaseDao;
import com.gm.soa.util.StringUtil;
import com.gm.soa.vo.buyinglead.PaginationCondition;

@Repository
public class ${className}DAO {
	
	private static Logger logger = LoggerFactory.getLogger(${className}DAO.class); 
    @Autowired
    private BaseDao baseDao;
    <#if gh.fromTable>
    /**
     * 获取新的id
     */
    private Long getNew${className}Id(){
    	
        StringBuilder sql = new StringBuilder("SELECT ${sequenceName}.nextval FROM DUAL");
        Long result = baseDao.jdbcTemplate.queryForLong(sql.toString());
        return result;
    }
    
    private void putUpdateParam(${className}VO ${classNameFirstLower}VO,StringBuilder sql, Map<String, Object> paramMap){
    	if(${classNameFirstLower}VO!=null){  
        	<#list table.columns as column>
        	<#if column.javaType=="String">if(StringUtil.isNotEmpty(${classNameFirstLower}VO.get${column.columnName}()))<#else>if(${classNameFirstLower}VO.get${column.columnName}()!=null)</#if>{
                sql.append(" ${gh.lower(column.sqlName)}=:new${column.columnNameLower}<#if column_has_next>,</#if> "); 
                paramMap.put("new${column.columnNameLower}",${classNameFirstLower}VO.get${column.columnName}());
            }
        	</#list>
        }
    }
    
    
    /**
     * 新增${className}记录
     */
    public Long add${className}(${className}VO ${classNameFirstLower}VO) throws Exception{
    	
        logger.info("In function : add${className} ${classNameFirstLower}VO={}", new Object[]{${classNameFirstLower}VO});
        
        StringBuilder sql = new StringBuilder();
        Long id = this.getNew${className}Id();
        
    	<#list table.columns as column>
    	<#if column.pk>
    	${classNameFirstLower}VO.set${column.columnName}(id);
    	</#if>
    	</#list>
    	
        sql.append("INSERT INTO ${gh.lower(table.sqlName)}(<#list table.columns as column>${gh.lower(column.sqlName)}<#if column_has_next>,</#if></#list>)");
        sql.append(" VALUES (<#list table.columns as column>:${column.columnNameLower}<#if column_has_next>,</#if></#list>)");

        Map<String, Object> paramMap = new HashMap<String, Object>();
        
    	<#list table.columns as column>
    	paramMap.put("${column.columnNameLower}", ${classNameFirstLower}VO.get${column.columnName}());
    	</#list>

        logger.info(" About to execute sql={} paramMap={}", new Object[]{sql.toString(), paramMap});
        
        boolean result=baseDao.namedParameterJdbcTemplate.update(sql.toString(), paramMap)>0?true:false;

        logger.info("End function : add${className}");
        if(!result){
        	return null;
        }
        return id;
    }
    /**
     * 删除${className}记录
     */
    public boolean delete${className}ById(<#list table.pkColumns as column>${column.javaType} ${column.columnNameLower}<#if column_has_next>,</#if></#list>)throws Exception{
    	
    	logger.info("In function : delete${className}ById: <#list table.pkColumns as column>${column.columnNameLower}={}<#if column_has_next>,</#if></#list>", new Object[]{<#list table.pkColumns as column>${column.columnNameLower}<#if column_has_next>,</#if></#list>});
        
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ${gh.lower(table.sqlName)} WHERE <#list table.columns as column><#if column.pk>${gh.lower(column.sqlName)}=:${column.columnNameLower}</#if></#list>");
    
        Map<String, Object> paramMap = new HashMap<String, Object>();

        <#list table.pkColumns as column>
        paramMap.put("${column.columnNameLower}", ${column.columnNameLower});
        </#list>
        
        logger.info(" About to execute sql={} paramMap={}", new Object[]{sql.toString(), paramMap});
        
        boolean result=baseDao.namedParameterJdbcTemplate.update(sql.toString(), paramMap)>0?true:false;

        logger.info("End function : delete${className}ById");
        return result;
    }
    /**
     * 修改${className}记录
     */
    public boolean update${className}ByParam(${className}VO setParam,${className}VO whereParam) throws Exception{
    	
        logger.info("In function : update${className}ByParam: setParam={},whereParam={}", new Object[]{setParam,whereParam});
        
        StringBuilder sql = new StringBuilder();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        
        sql.append("UPDATE ${gh.lower(table.sqlName)} SET ");
        putUpdateParam(setParam,sql,paramMap);
    	sql.append(" WHERE 1=1 ");	
        putWhereParam(whereParam,sql,paramMap);

        logger.info(" About to execute sql={} setParam={},whereParam={}", new Object[]{sql.toString(), paramMap,whereParam});
        
        boolean result=baseDao.namedParameterJdbcTemplate.update(sql.toString(), paramMap)>0?true:false;

        logger.info("End function : update${className}ByParam");
        return result;
    }
    	
    /**
     * 更新${className}记录
     */
    public boolean update${className}ById(<#list table.pkColumns as column>${column.javaType} ${column.columnNameLower}</#list>,${className}VO setParam) throws Exception{
    	
        logger.info("In function : update${className}ById: <#list table.pkColumns as column>${column.columnNameLower}={}</#list>", new Object[]{<#list table.pkColumns as column>${column.columnNameLower}</#list>});
        
        StringBuilder sql = new StringBuilder();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        
        sql.append("UPDATE ${gh.lower(table.sqlName)} SET ");
        putUpdateParam(setParam,sql,paramMap);
        sql.append(" WHERE <#list table.columns as column><#if column.pk>${gh.lower(column.sqlName)}=:${column.columnNameLower}</#if></#list>");
        <#list table.pkColumns as column>
        paramMap.put("${column.columnNameLower}", ${column.columnNameLower});
        </#list>
        logger.info(" About to execute sql={} paramMap={}", new Object[]{sql.toString(), paramMap});
        
        boolean result=baseDao.namedParameterJdbcTemplate.update(sql.toString(), paramMap)>0?true:false;

        logger.info("End function : update${className}ById");
        return result;
    }   
	</#if>
    
    private void putWhereParam(${className}VO ${classNameFirstLower}VO,StringBuilder sql, Map<String, Object> paramMap){
    	if(${classNameFirstLower}VO!=null){  
        	<#list table.columns as column>
        	<#if column.javaType=="String">if(StringUtil.isNotEmpty(${classNameFirstLower}VO.get${column.columnName}()))<#else>if(${classNameFirstLower}VO.get${column.columnName}()!=null)</#if>{
	            sql.append("   AND ${gh.lower(column.sqlName)}=:${column.columnNameLower} ");
	            paramMap.put("${column.columnNameLower}",${classNameFirstLower}VO.get${column.columnName}());
	        }
        	</#list>
        }
    }

    
    private String getSelectSQL(){
        <#if gh.fromTable>
    	String sql="SELECT <#list table.columns as column>${gh.lower(column.sqlName)}<#if column_has_next>,</#if></#list> FROM ${gh.lower(table.sqlName)} WHERE 1=1 ";
    	<#else>
        String sql="${table.sourceSql}";
        </#if>
    	return sql;
    }
  
    /**
     * 查询${className}记录
     */
    public List<${className}VO> get${className}ListByParam(${className}VO ${classNameFirstLower}VO,PaginationCondition pc)throws Exception{
    	
        logger.info("In function : get${className}ListByParam: ${classNameFirstLower}VO={},pc={}", new Object[]{${classNameFirstLower}VO,pc});
        Map<String, Object> paramMap = new HashMap<String, Object>();
          
        StringBuilder sql = new StringBuilder();
        sql.append(getSelectSQL());
        
        putWhereParam(${classNameFirstLower}VO,sql,paramMap);
        String sqlstr=PaginationParams.convertSqlStatement(sql.toString(), pc, paramMap);
        logger.info(" About to execute sql={} paramMap={}", new Object[]{sqlstr, paramMap});
        List<${className}VO> result = baseDao.namedParameterJdbcTemplate.query(sqlstr, paramMap, new BeanPropertyRowMapper<${className}VO>(${className}VO.class));  
        
        logger.info("End function : get${className}ListByParam");
        return result;
    }
    
    
    /**
     * 查询总数
     */
    public int getTotal${className}ByParam(${className}VO ${classNameFirstLower}VO) throws Exception{
    	logger.info("In function : getTotal${className}ByParam: ${classNameFirstLower}VO={}", new Object[]{${classNameFirstLower}VO});
    	StringBuilder sql=new StringBuilder();
    	 <#if gh.fromTable>
    	sql.append("SELECT COUNT(1) FROM ${gh.lower(table.sqlName)} WHERE 1=1 ");
    	<#else>
    	sql.append("${gh.getConfigSingleValue('countSql')}");
    	</#if>
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	putWhereParam(${classNameFirstLower}VO,sql,paramMap);
    	//查询记录总数
        logger.info(" About to execute sql={} paramMap={}",new Object[]{sql.toString(), paramMap});
        int result = baseDao.namedParameterJdbcTemplate.queryForInt(sql.toString(), paramMap);
        
        logger.info(" End Function: getTotal${className}ByParam");
        return result;
    }

    /**
     * 根据id查询${className}记录
     */
    public ${className}VO get${className}ById(<#list table.pkColumns as column>${column.javaType} ${column.columnNameLower}<#if column_has_next>,</#if></#list>)throws Exception{
    	
        logger.info("In function : get${className}ById: <#list table.pkColumns as column>${column.columnNameLower}={}<#if column_has_next>,</#if></#list>", new Object[]{<#list table.pkColumns as column>${column.columnNameLower}<#if column_has_next>,</#if></#list>});
        
        StringBuilder sql = new StringBuilder();
        sql.append(getSelectSQL());
        sql.append(" AND <#list table.pkColumns as column>${gh.lower(column.sqlName)}=:${column.columnNameLower}</#list>");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        <#list table.pkColumns as column>
        paramMap.put("${column.columnNameLower}", ${column.columnNameLower});
        </#list>
        logger.info(" About to execute sql={} paramMap={}", new Object[]{sql.toString(), paramMap});
        ${className}VO result = baseDao.namedParameterJdbcTemplate.queryForObject(sql.toString(), paramMap, new BeanPropertyRowMapper<${className}VO>(${className}VO.class));   
        logger.info("End function : get${className}ById");
        return result;
    }
}