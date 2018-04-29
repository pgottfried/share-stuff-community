//package application.entity;
//
//import java.util.List;
//import java.util.Set;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//
//
//@Entity
//@Table(name="chat")
//public class Chat extends BaseEntity{
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	@Column(name="name", nullable = true, length = 64)
//	private String name;
//	
//	@ManyToOne
//	@JoinColumn(name="offer_id")	
//	private Offer offer;
//	
//	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
//	@JoinTable(name = "user_chat", joinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
//	Set<User> users;
//	
//	@OneToMany(mappedBy="chat")
//	List<Message> messages;
//
//	public Set<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(Set<User> users) {
//		this.users = users;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public Offer getOffer() {
//		return offer;
//	}
//
//	public void setOffer(Offer offer) {
//		this.offer = offer;
//	}
//	
//
//	
//}
