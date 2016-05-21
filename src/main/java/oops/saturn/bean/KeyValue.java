package oops.saturn.bean;

/**
 * 该类用于封装键值对
 *
 * @param <M> 键名称类型
 * @param <N> 键值类型
 */
public class KeyValue<M,N> {
	private M name; // 键名称
	private N value; // 键值
	public KeyValue(){
		
	}
	public KeyValue(M name,N value){
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
}
