package oops.saturn.learning.t1025;

import javax.persistence.Column;
import javax.persistence.Entity;

import oops.saturn.model.BaseEntity;

@Entity
public class Teacher extends BaseEntity {
	private String teacherName;
	
	@Column
	public String getTeacherName() {
		return teacherName;
	}
	
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
	
}
