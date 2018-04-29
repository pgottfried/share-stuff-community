package application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Rating extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="stars",nullable=false)
	private int stars;
	@Column(name="comment",nullable=false)
	private String comment;
	@ManyToOne
	@JoinColumn(name="onUser_id")
	private User onUser;
	@ManyToOne
	@JoinColumn(name="fromUser_id")
	private User fromUser;
	
	public int getStars() {
		return stars;
	}
	public void setStars(int stars) {
		this.stars = stars;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public User getOnUser() {
		return onUser;
	}
	public void setOnUser(User onUser) {
		this.onUser = onUser;
	}
	public User getFromUser() {
		return fromUser;
	}
	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}
	
	
}
