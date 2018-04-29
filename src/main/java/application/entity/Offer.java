package application.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;






@Entity
@Table(name="offer")
//@Indexed
public class Offer extends BaseEntity {

	private static final long serialVersionUID = 9014169812363387062L;
	@Column(name="title", nullable = false, length = 64)
	private String title;
	@Column(name="description", nullable = false, length = 255)
	private String description;
	@Column(name="status", nullable = false, length = 32)
	private String status;
	//Image
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=400000)
	private byte[] thumbnail;
	//Payment
//	private boolean paypal;
//	private boolean cash;
//	private boolean transfer;
//	//Shipment
//	private boolean dhl;
//	private boolean ups;
//	private boolean hermes;
//	private boolean cashAndCarry;
	@Column(name="geoPoint",nullable= false,length=21)
	private String geoPoint;
	
	@ManyToOne
	@JoinColumn(name="user_id")	
	private User user; // is being set

	@ManyToOne
	@JoinColumn(name="category_id")	 //created here, cause is set in the view
	private Category category = new Category();
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public byte[] getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getGeoPoint() {
		return geoPoint;
	}
	public void setGeoPoint(String geoPoint) {
		this.geoPoint = geoPoint;
	}
}
