//package application.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//
//@Entity
//@Table(name="request")
//public class Request extends BaseEntity {
//
//	private static final long serialVersionUID = 9014169812363387062L;
//	@Column(name="term", nullable = false, length = 32)
//	private String term;
//	@Column(name="money", nullable = true, length = 8)
//	private String money;
//	private Date startDate;
//	private Date endDate;
//	@Column(name="payment", nullable = true, length = 16)
//	private String payment;
//	@Column(name="delivery", nullable = false, length = 16)
//	private String delivery;
//	@Column(name="status", nullable = false, length = 16)
//	private String status;
//	@ManyToOne
//	@JoinColumn(name="user_id")	
//	private User user; // is being set
//	@ManyToOne
//	@JoinColumn(name="offer_id")	 //created here, cause is set in the view
//	private Offer offer;
//	
//	
//	
//	public User getUser() {
//		return user;
//	}
//	public Offer getOffer() {
//		return offer;
//	}
//	public void setUser(User user) {
//		this.user = user;
//	}
//	public void setOffer(Offer offer) {
//		this.offer = offer;
//	}
//	public String getTerm() {
//		return term;
//	}
//	public String getMoney() {
//		return money;
//	}
//	public Date getStartDate() {
//		return startDate;
//	}
//	public Date getEndDate() {
//		return endDate;
//	}
//	public String getPayment() {
//		return payment;
//	}
//	public String getDelivery() {
//		return delivery;
//	}
//	public void setTerm(String term) {
//		this.term = term;
//	}
//	public void setMoney(String money) {
//		this.money = money;
//	}
//	public void setStartDate(Date startDate) {
//		this.startDate = startDate;
//	}
//	public void setEndDate(Date endDate) {
//		this.endDate = endDate;
//	}
//	public void setPayment(String payment) {
//		this.payment = payment;
//	}
//	public void setDelivery(String delivery) {
//		this.delivery = delivery;
//	}
//	public String getStatus() {
//		return status;
//	}
//	public void setStatus(String status) {
//		this.status = status;
//	}
//	
//	
//	
//}
