package oops.saturn.other;

import java.util.List;

/**
 * 该类用来封装数据查询结果，数据查询结果包括数据的总条数、当前页码、数据列表
 * 
 */
public class PageQueryResult {
	private Long records; // 数据的总条数
	private Integer page; // 当前页码
	private List<?> rows; // 数据列表
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getRows(Class<T> klass) {
		return (List<T>) rows;
	}
	
	public int size() {
		return rows.size();
	}
	
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	public Long getRecords() {
	    return records;
	}
	public void setRecords(Long records) {
	    this.records = records;
	}
}
