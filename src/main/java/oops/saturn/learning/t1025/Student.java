package oops.saturn.learning.t1025;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import oops.saturn.model.BaseEntity;

@Entity
public class Student extends BaseEntity {
	private String studentName;
	private Set<Teacher> teacherSelected;
	
	@Column
	public String getStudentName() {
		return studentName;
	}
	
	

	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, targetEntity=Teacher.class)
	public Set<Teacher> getTeacherSelected() {
		return teacherSelected;
	}




	public void setTeacherSelected(Set<Teacher> teacherSelected) {
		this.teacherSelected = teacherSelected;
	}




	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	
}
