package oops.saturn.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 实体基类
 *
 */
@MappedSuperclass
public class BaseEntity {
	protected Long id; // 数据库编号
	protected Map<String,Object> cp=new HashMap<String,Object>() ; // 自定义属性容器

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
	public void setId(Long id) {
		this.id = id;
	}
    @Transient
    public Map<String, Object> getCp() {
        return cp;
    }

    public void setCp(Map<String, Object> cp) {
	if(cp==null)
	    return;
        this.cp = cp;
    }

    
}
