package oops.saturn.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 该类用来封装数据查询条件，数据查询条件包括要查询的实体类、实体别名、数据项名称列表、数据筛选条件、自定义的
 * 筛选条件、数据项排序规则、页码、每页的数据条数、数据项名称前缀
 * 
 */
public class PageQueryCondition {
	private Class<?> entityClass; // 实体类
	private String entityName; // 实体别名
	private List<String> fields; // 数据项属性名列表
	private List<String> fieldTitles;//数据项显示名称列表
	private Map<String, Object> conditions; // 数据筛选条件
	private String extendedHql; // 自定义的筛选条件
	private Map<String,Boolean> orders; // 数据项排序规则
	private Integer page; // 页码
	private Integer numPerPage; // 每页的数据条数
	private String propertyPrefix; // 数据项名称前缀
	private List<String> exportIncludeColumns; // 导出数据时包含的属性名
	private List<String> exportIgnoreColumns; // 导出数据时忽略的属性名
	private String exportFileName;//导出文件名
	
	public PageQueryCondition from(Class<?> entityClass) {
		setEntityClass(entityClass);
		return this;
	}
	
	public PageQueryCondition as(String alias) {
		setEntityName(alias);
		return this;
	}
	
	public PageQueryCondition select(String... fields) {
		setFields(new ArrayList<String>(Arrays.asList(fields)));
		return this;
	}
	
	public PageQueryCondition where(String key, Object value) {
		if (conditions == null) {
			conditions = new LinkedHashMap<String, Object>();
		}
		if(key!=null){
			conditions.put(key, value);
		}
		return this;
	}
	
	public PageQueryCondition where(Map<String, Object> map) {
		if (conditions == null) {
			conditions = new LinkedHashMap<String, Object>();
		}
		conditions.putAll(map);
		return this;
	}
	
	public PageQueryCondition where(String hql) {
		setExtendedHql(hql);
		return this;
	}
	
	public PageQueryCondition order(String... fields) {
		for (String field : fields) {
			order(field, true);
		}
		return this;
	}
	
	public PageQueryCondition order(String field, boolean asc) {
		if (orders == null) {
			orders = new LinkedHashMap<String, Boolean>();
		}
		if(field!=null)
			orders.put(field, asc);
		return this;
	}
	
	public PageQueryCondition order(Map<String, Boolean> map) {
		if (orders == null) {
			orders = new LinkedHashMap<String, Boolean>();
		}
		orders.putAll(map);
		return this;
	}
	
	public PageQueryCondition limit(int page, int size) {
		setPage(page);
		setNumPerPage(size);
		return this;
	}
	
	public List<String> getFieldTitles() {
		return fieldTitles;
	}

	public void setFieldTitles(List<String> fieldTitles) {
		this.fieldTitles = fieldTitles;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}
	

	public List<String> getExportIncludeColumns() {
		return exportIncludeColumns;
	}

	public List<String> getExportIgnoreColumns() {
		return exportIgnoreColumns;
	}

	public String getExportFileName() {
		return exportFileName;
	}

	public void setExportIncludeColumns(List<String> exportIncludeColumns) {
		this.exportIncludeColumns = exportIncludeColumns;
	}

	public void setExportIgnoreColumns(List<String> exportIgnoreColumns) {
		this.exportIgnoreColumns = exportIgnoreColumns;
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}

	public String getPropertyPrefix() {
		return propertyPrefix;
	}

	public void setPropertyPrefix(String propertyPrefix) {
		this.propertyPrefix = propertyPrefix;
	}

	public String getEntityName() {
	    return entityName;
	}
	public void setEntityName(String entityName) {
	    this.entityName = entityName;
	}
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}
	public List<String> getFields() {
		return fields;
	}
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	public Map<String, Object> getConditions() {
		return conditions;
	}
	public void setConditions(Map<String, Object> conditions) {
		this.conditions = conditions;
	}
	public Map<String, Boolean> getOrders() {
		return orders;
	}
	public void setOrders(Map<String, Boolean> orders) {
		this.orders = orders;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	public String getExtendedHql() {
		return extendedHql;
	}

	public void setExtendedHql(String extendedHql) {
		this.extendedHql = extendedHql;
	}
	
}
