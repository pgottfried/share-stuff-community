package application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;


@Entity
@Table(name="category")
public class Category extends BaseEntity{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	@Field
	@Column(name="name", nullable = false, length = 32)
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
