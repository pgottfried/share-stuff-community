package application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="address")
public class Address extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="street", nullable = false, length = 64)
	private String street;
	@Column(name="houseNumber", nullable = false, length = 5)
	private String houseNumber;
	@Column(name="city", nullable = false, length = 32)
	private String city;
	@Column(name="postalCode", nullable = false, length = 8)
	private String postalCode;
	//format = latitude,longitude e.g. "48.1502526,11.5507634"
	@Column(name="geoPoint",nullable= false,length=21)
	private String geoPoint;
	@ManyToOne
	@JoinColumn(name="user_id")	
	private User user;
	

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getCity() {
		return city;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getStreet() {
		return street;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getGeoPoint() {
		return geoPoint;
	}
	public void setGeoPoint(String geoPoint) {
		this.geoPoint = geoPoint;
	}
}
