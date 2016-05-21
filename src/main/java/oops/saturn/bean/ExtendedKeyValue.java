package oops.saturn.bean;

import org.apache.commons.lang.xwork.ObjectUtils;

/**
 * 该类用于封装键值对,当键和值都相同时对象才相同
 *
 * @param <M> 键名称类型
 * @param <N> 键值类型
 */
public class ExtendedKeyValue<M,N> {
	private M name; // 键名称
	private N value; // 键值
	public ExtendedKeyValue(){
		
	}
	public ExtendedKeyValue(M name,N value){
		this.name=name;
		this.value=value;
	}
	public M getName() {
		return name;
	}
	public void setName(M name) {
		this.name = name;
	}
	public N getValue() {
		return value;
	}
	public void setValue(N value) {
		this.value = value;
	}
	
	 public int hashCode(){
		 return ObjectUtils.hashCode(this.name);
	 }
	 
	 public boolean equals(Object obj) {
		 if(!(obj instanceof ExtendedKeyValue))
			 return false;
		 ExtendedKeyValue<?,?> other=(ExtendedKeyValue<?,?>)obj;
		 return ObjectUtils.equals(this.name,other.getName())&&ObjectUtils.equals(this.value,other.getValue());
	 }
}
