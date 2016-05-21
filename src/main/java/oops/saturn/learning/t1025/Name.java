package oops.saturn.learning.t1025;

import java.io.Serializable;

public class Name implements Serializable 	//复合主键实体类需要实现Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3213710235125925908L;
	private String first;
	private String last;
	
	public String getFirst() {
		return first;
	}
	public String getLast() {
		return last;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public void setLast(String last) {
		this.last = last;
	}
	
	
}


