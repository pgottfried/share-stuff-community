package application.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;

public class MyRabbitConfirmCallback implements ConfirmCallback {

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		System.out.println("Rabbit Confirm Callback invoked");
		System.out.println(ack);
		System.out.println(cause);
		
		
	}

}
