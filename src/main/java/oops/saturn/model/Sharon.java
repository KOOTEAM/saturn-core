package oops.saturn.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Sharon extends BaseEntity {
	private String sharonName;
	private Integer sharonAge;
	private Long sharonHeight;
	private Long sharonWeight;
	private Position position;
	
	@Column
	public String getSharonName() {
		return sharonName;
	}
	@Column
	public Integer getSharonAge() {
		return sharonAge;
	}
	@Column
	public Long getSharonHeight() {
		return sharonHeight;
	}
	@Column
	public Long getSharonWeight() {
		return sharonWeight;
	}
	@Enumerated(EnumType.ORDINAL)
	public Position getPosition() {
		return position;
	}
	public void setSharonName(String sharonName) {
		this.sharonName = sharonName;
	}
	public void setSharonAge(Integer sharonAge) {
		this.sharonAge = sharonAge;
	}
	public void setSharonHeight(Long sharonHeight) {
		this.sharonHeight = sharonHeight;
	}
	public void setSharonWeight(Long sharonWeight) {
		this.sharonWeight = sharonWeight;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	
	
	
	
}
