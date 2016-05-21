package oops.saturn.learning.t1025;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity		//实体类注解
@Table(name="example_table")	//表名
public class Example {
	
	@Id		//实体类主键注解
	@GeneratedValue(strategy=GenerationType.AUTO)	//主键生成策略
	private Long id;
	
	@Column(name="example_title", nullable=false)	//定义字段名，是否非空等
	private String title;
	
	@Column(name="title_2") //若定义table，则跟着table走。并定义字段名
	private String titleInTable2;
	
	@Column(name="content_2")
	private String contentInTable2;
	
	@Transient		//指定不在表中生成对应的字段
	private String content;
	
	@Enumerated(EnumType.ORDINAL) 	//枚举类专属注解
	@Column(name="happy_season")
	private Seasons happySeason;
	
	@Lob		//Blob，Clob类用注解		//Blob:byte[], Byte[]或java.io.Serializable
	@Basic(fetch=FetchType.LAZY)		//可指定延迟加载与是否可选
	private byte[] pic;
	
	@Temporal(TemporalType.TIMESTAMP)	//时间用注解
	private Date birth;
	
	@Embedded	//复合类用注解
	@AttributeOverrides({	//将复合类属性拆分并一一对应到字段
		@AttributeOverride(name="name", column=@Column(name="cat_name", length=25)),
		@AttributeOverride(name="color", column=@Column(name="cat_color"))
	})
	private Cat cat;

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getTitleInTable2() {
		return titleInTable2;
	}

	public String getContentInTable2() {
		return contentInTable2;
	}

	public String getContent() {
		return content;
	}

	public Seasons getHappySeason() {
		return happySeason;
	}

	public byte[] getPic() {
		return pic;
	}

	public Date getBirth() {
		return birth;
	}

	public Cat getCat() {
		return cat;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitleInTable2(String titleInTable2) {
		this.titleInTable2 = titleInTable2;
	}

	public void setContentInTable2(String contentInTable2) {
		this.contentInTable2 = contentInTable2;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setHappySeason(Seasons happySeason) {
		this.happySeason = happySeason;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public void setCat(Cat cat) {
		this.cat = cat;
	}
	
	
}






