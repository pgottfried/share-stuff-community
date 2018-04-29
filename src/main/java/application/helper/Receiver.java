package application.helper;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class Receiver {
	

	@Autowired
	private RabbitTemplate rabbitTemplate;

	
	
	public void processMessage(Message message){
		//check principal or append something...
		//save to db
		String convId = message.getMessageProperties().getReceivedRoutingKey();
		String msg = null;
		try {
			msg = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Message: " + msg);
//		rabbitTemplate.convertAndSend("exchange_incoming", "conv1", message);
		
		
	}
}
