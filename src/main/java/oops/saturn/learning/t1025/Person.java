package oops.saturn.learning.t1025;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="person_table")
public class Person {
	
	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name="first", column=@Column(name="person_first")),
		@AttributeOverride(name="last", column=@Column(name="person_last"))
	})
	private Name name;
	private String email;
	
	public Name getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public void setName(Name name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public static void main(String[] args) {
		Name name1 = new Name();
		Name name2 = new Name();
		name1.setFirst("Sharon");
		name1.setLast("Dou");
		name2 = name1;
		name1.setLast("GangDu");
		System.out.println(name2.getFirst() + " " + name2.getLast());
	}
	
}
