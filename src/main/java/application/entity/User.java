package application.entity;

import java.math.BigDecimal;
//import java.util.List;
//import java.util.Set;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
//import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
//import javax.persistence.ManyToMany;
//import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Entity to hold application user data - first name, last name, etc.
 * @author Paule
 */

@Entity
@Table(name="user") //without that line tablename would be the classname "UserEntiy"
public class User extends BaseEntity {
	
	private static final long serialVersionUID = 9014169812363387062L;
	@Column(name="firstName", nullable = false, length = 32)
	private String firstName;
	@Column(name="lastName", nullable = false, length = 32)
	private String lastName;
	@Column(name="userName", nullable = false, length = 32)
	private String userName;
	@Column(name="password", nullable = false, length = 60)
	private String password;
	@Column(name="email", nullable = false, length = 60)
	private String email;
	@Column(name="phone", nullable = true, length = 32)
	private String phone;
	@Column(name="uuid", nullable = true, length = 40)
	private String uuid;
	@Column(name="confirmed")
	private boolean confirmed;
	@Column(name="rating", columnDefinition="default '0.0', precision=2, scale=1")
	private BigDecimal rating;
	@Column(name="ratingCount", columnDefinition="BIGINT(20) default '0'")
	private Long ratingCount;
	@Column(name="exchange",nullable = false, length=40)
	private String exchange;
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=40000, nullable=true)
	private byte[] thumbnail;
	
//	@OneToMany(mappedBy="user")
//	List<Message> messages;
//	
//	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
//	@JoinTable(name = "user_conversation", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "conv_id", referencedColumnName = "id"))
//	Set<Conversation> conversations;
	
	@ManyToMany(mappedBy="users")
	Set<Conversation> conversations;
	
//	
//	
//	
//	public List<Message> getMessages() {
//		return messages;
//	}
//
//	public void setMessages(List<Message> messages) {
//		this.messages = messages;
//	}
//
//	public Set<Chat> getChats() {
//		return chats;
//	}
//
//	public void setChats(Set<Chat> chats) {
//		this.chats = chats;
//	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public boolean getConfirmed(){
		return this.confirmed;
	}
	public void setConfirmed(boolean confirmed){
		this.confirmed = confirmed;
	}

	public Long getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Long ratingCount) {
		this.ratingCount = ratingCount;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}
	
}