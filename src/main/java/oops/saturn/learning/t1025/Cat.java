package oops.saturn.learning.t1025;

import javax.persistence.Embeddable;

@Embeddable		//复合类需标注该注解
public class Cat {
	public String name;
	public String color;
}
