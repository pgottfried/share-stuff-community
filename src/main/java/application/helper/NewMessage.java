package application.helper;

import java.util.List;

import application.entity.Conversation;
import application.entity.Message;
/*
 * Used the Messaging part of the App
 * A New Message /Request comes in and the MessageHistory and the Conversation details are returnd
 * 
 */
public class NewMessage {
	
	private Conversation conversation;
	private List<Message> messageList;
	
	public NewMessage(Conversation conversation, List<Message> messageList) {
		this.conversation = conversation;
		this.messageList = messageList;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}
	
	
}
