package application.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rabbitmq.client.Channel;

import application.entity.Conversation;
import application.entity.Offer;
import application.entity.User;
import application.helper.NewMessage;
import application.repository.ConversationRepository;
import application.repository.MessageRepository;
import application.repository.OfferRepository;
//import application.repository.MessageRepository;
import application.repository.UserRepository;

@Controller
public class ChatController {
	
//	@Autowired private CamelContext camelContext;
	@Autowired private UserRepository userRepo;
	@Autowired private ConversationRepository conversationRepo;
	@Autowired private OfferRepository offerRepo;
	@Autowired private MessageRepository messageRepo;
	@Autowired private RabbitTemplate rabbitTemplate;
	
	
	@RequestMapping(value="/request", method=RequestMethod.POST)
	@ResponseBody
	public String sendmsg(@RequestParam("msg") String msg, @RequestParam("toUser") Long toUserId, @RequestParam("offerId") Long offerId, Principal principal){
		
		// check if Conv exists
		
		// else create conversation
	 	Conversation conv = new Conversation();
	 	User user1 =userRepo.findByUserName(principal.getName());
	 	Long userId = user1.getId();
		User user2 = userRepo.findOne(toUserId);
		Set<User> users = new HashSet<User>();
		users.add(user1);
		users.add(user2);
		conv.setUsers(users);
		
		Offer offer = offerRepo.findOne(offerId);
	 	conv.setOffer(offer);
	 	conv.setTitle("Anfrage " + offer.getTitle());
	 	conv = conversationRepo.save(conv);
	 	String convId = conv.getId().toString();
	 	//bind each exchange with conv_id to ex_incoming
	 	
	 	ConnectionFactory factory = rabbitTemplate.getConnectionFactory();
		Connection connection = factory.createConnection();
		Channel channel = connection.createChannel(false);
	 	for(User user : users){
			try {
				channel.exchangeBind(user.getExchange(),"exchange_incoming", convId);
			} catch (IOException e) {
				e.printStackTrace();
				connection.close();
				return "error";
			}
	 	}
	 	connection.close();
	 	
	 	MessageProperties props = new MessageProperties();
	 	props.setContentType("text/plain");
	 	props.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
	 	props.setHeader("userId", userId.toString());

	 	byte[] msgInByte=null;
		try {
			msgInByte = msg.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	Message amqpMsg = new Message(msgInByte,props);
	 
	 	rabbitTemplate.send("exchange_outgoing", convId, amqpMsg);
	 	

		
		return "success";
	}

	
	 
	 @RequestMapping(value="/conversations")
	 public String showconversations(Principal principal, Model model){

		 User user= userRepo.findByUserName(principal.getName());
		 model.addAttribute("userExchange", user.getExchange());
		 model.addAttribute("userId",user.getId());
		 model.addAttribute("userName", user.getUserName());
		 List<Conversation> convList = conversationRepo.findByUsersIdOrderByUpdatedDesc(user.getId());
//		 List<application.entity.Message> messageList = null;
//		 Map<String,String> userNames = null;
		 if(convList != null){
			
			 Map<String,List<application.entity.Message>> convMessageMap = new HashMap<String, List<application.entity.Message>>();
			 for(Conversation conv : convList){
				 List<application.entity.Message> messageList = messageRepo.findTop20ByConversationIdOrderByCreatedDesc(conv.getId());
				 convMessageMap.put(conv.getId().toString(), messageList);
			 }
			 model.addAttribute("convList",convList);
			 model.addAttribute("convMessageMap",convMessageMap);
		 }
			 
			
//			 Long convId = convList.get(0).getId();
//			 model.addAttribute("convId", convId);
//			 
//			 messageList = messageRepo.findLast20Messages(convId);
//			 Map<String,Map<String,String>> convUserMap = new HashMap<String,Map<String,String>>();
//			 for(Conversation conv : convList){
//				 Map<String,String> userMap = new HashMap<String,String>();
//				 for(User u : conv.getUsers()){
//					 userMap.put(u.getId().toString(), u.getUserName());
//				 }
//				 convUserMap.put(conv.getId().toString(), userMap);
//			 }
//			 model.addAttribute("convUserMap", convUserMap);
//			 
//			 Set<User> users = convList.get(0).getUsers();
//			 userNames = new HashMap<String,String>();
//			 for(User u : users){
//				 userNames.put(u.getId().toString(), u.getUserName());
//			 }
//		 }
//		 if(messageList != null){
//			 model.addAttribute("messageList",messageList);
//			 model.addAttribute("userNames", userNames);
//			
//		 }
		 return "conversations";
	 }
	 
	 @RequestMapping(value="/conversation/{id}")
	 @ResponseBody
	 public NewMessage showConversationById(@PathVariable("id") Long convId, Principal principal){
//		 String loggedInUser = principal.getName();
//		 boolean hasPermission = false;
		 Conversation conversation = conversationRepo.findOne(convId);
//		 for(User u : conversation.getUsers()){
//			if( u.getUserName().equals(loggedInUser)){
//				hasPermission = true;
//			}
//				
//		 }
//		 if(hasPermission){
			 List<application.entity.Message> messageList = messageRepo.findTop20ByConversationIdOrderByCreatedDesc(convId);
			 return new NewMessage(conversation,messageList);
//		 }
//		 
//		 else return null;
		
	 }
}
