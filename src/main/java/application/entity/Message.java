package application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import application.helper.MessageSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = MessageSerializer.class)
@Entity
@Table(name="message")
public class Message extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="msg", nullable = false)
	private String msg;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	
	@ManyToOne
	@JoinColumn(name = "conv_id")
	private Conversation conversation;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	

	
	
	
	

	
}
