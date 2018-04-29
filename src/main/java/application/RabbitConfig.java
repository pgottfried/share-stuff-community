package application;


import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import application.helper.Receiver;
import application.service.MyRabbitConfirmCallback;
import application.service.MyRabbitReturnCallback;
import application.service.MyRabbitListener;

@Configuration
@EnableRabbit
class RabbitConfig{

	final static String chatQueueName = "queue_messages";
	final static String exchangeOut = "exchange_outgoing";
	final static String exchangeIn = "exchange_incoming";
	

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		connectionFactory.setPort(5672);
		connectionFactory.setPublisherReturns(true);
		connectionFactory.setPublisherConfirms(true);
		return connectionFactory;
}
	
	@Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
	
//	@Bean
//	public void configureRabbitTemplate(RabbitTemplate rabbitTemplate) {
//	    rabbitTemplate.setExchange(exchangeIn);
//	}
	
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMandatory(true);
		template.setReturnCallback(new MyRabbitReturnCallback());
		template.setConfirmCallback(new MyRabbitConfirmCallback());
		return template;
	}
	
	
	
	@Bean
	FanoutExchange exchangeOut(){
		return new FanoutExchange(exchangeOut);
	}
	
	@Bean
	Queue queue(){
		return new Queue(chatQueueName,false);
	}
	
	@Bean
	TopicExchange exchangeIn(){
		return new TopicExchange(exchangeIn);
	}
	
	@Bean
	Binding binding(FanoutExchange exchangeOut,Queue queue,TopicExchange exchangeIn){
		return BindingBuilder.bind(queue).to(exchangeOut);
	}
	
	
	@Bean
	public SimpleRabbitListenerContainerFactory myRabbitListenerContainerFactory() {
	       SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
	       factory.setConnectionFactory(connectionFactory());
	       factory.setMaxConcurrentConsumers(5);
	       return factory;
	     }
	
	@Bean
	public MyRabbitListener rabbitService(){
		return new MyRabbitListener();
	}
	
	
//	@Bean
//	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(chatQueueName);
//        container.setMessageListener(listenerAdapter(receiver()));
//        return container;
//	}
//	
//	
//	
//	@Bean
//    public RabbitTemplate rabbitTemplate() {
//        return new RabbitTemplate(connectionFactory());
//    }
//	
//	@Bean
//    Receiver receiver() {
//        return new Receiver();
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "processMessage");
//    }
    
    
	
	
	
//    @Bean
//	public ScheduledProducer scheduledProducer() {
//		return new ScheduledProducer();
//	}
//
//	@Bean
//	public BeanPostProcessor postProcessor() {
//		return new ScheduledAnnotationBeanPostProcessor();
//	}
//
//
//	static class ScheduledProducer {
//
//		@Autowired
//		private volatile RabbitTemplate rabbitTemplate;
//
//		private final AtomicInteger counter = new AtomicInteger();
//
//		@Scheduled(fixedRate = 3000)
//		public void sendMessage() {
//			String text = "some text";
//			byte[] textInByte = null;
//			try {
//				textInByte = text.getBytes("UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			MessageProperties props = new MessageProperties();
//			props.setReceivedRoutingKey("conv1");
//			Message message = new Message(textInByte,props);
//			rabbitTemplate.send(exchangeOut, "conv1", message );
//			rabbitTemplate.convertAndSend(exchangeOut, "#", "äüö" + counter.incrementAndGet());
//		}
//}
	
	
	
	
	
}