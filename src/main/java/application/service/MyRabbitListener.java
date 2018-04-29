package application.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import application.entity.Conversation;
import application.entity.User;
import application.repository.ConversationRepository;
import application.repository.MessageRepository;
import application.repository.UserRepository;


public class MyRabbitListener {
	
	@Autowired ConversationRepository conversationRepo;
	@Autowired RabbitTemplate rabbitTemplate;
	@Autowired MessageRepository messageRepo;
	@Autowired UserRepository userRepo;
	
	@RabbitListener(containerFactory="myRabbitListenerContainerFactory", queues="queue_messages")
	public void process(Message msg){
		
		String userId =  (String) msg.getMessageProperties().getHeaders().get("userId");
		String convId = msg.getMessageProperties().getReceivedRoutingKey();
		//if no routingkey provided a String with "null" is returned
		if(!convId.contains("null")){
			String text = null;
			try {
				 text = new String(msg.getBody(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("msg= " + text +" relates to conv: "+ convId);
			
			application.entity.Message msgEntity = new application.entity.Message();
			Conversation conversation = conversationRepo.findOne(Long.valueOf(convId));
			conversation.setUpdated(new Date());
			conversationRepo.save(conversation);
			User user = userRepo.findOne(Long.valueOf(userId));
			msgEntity.setConversation(conversation);
			msgEntity.setMsg(text);
			msgEntity.setUser(user);
			messageRepo.save(msgEntity);
			
			
			rabbitTemplate.send("exchange_incoming", convId, msg);
		}
		
	}
}
