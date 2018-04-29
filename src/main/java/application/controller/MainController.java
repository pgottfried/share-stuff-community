package application.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import java.util.List;

import java.util.UUID;

import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.rabbitmq.client.Channel;

import application.document.OfferDocument;
import application.entity.Address;
//import application.entity.Chat;
import application.entity.Image;
import application.entity.Offer;
import application.entity.Rating;
import application.entity.User;
import application.helper.OSMPoint;
import application.helper.Pagination;
import application.repository.AddressRepository;
//import application.repository.ChatRepository;
import application.repository.ImageRepository;
import application.repository.OfferDocumentRepository;
import application.repository.OfferRepository;
import application.repository.RatingRepository;
import application.repository.UserRepository;
//import application.service.ChatService;
import application.service.EmailService;
import application.service.OSMService;



@Controller
public class MainController {
	
	@Autowired private OfferDocumentRepository elasticRepo;
	@Autowired private OfferRepository offerRepo;
	@Autowired private ImageRepository imageRepo;
	@Autowired private UserRepository userRepo;
	@Autowired private AddressRepository addressRepo;
	@Autowired private RatingRepository ratingRepo;
	@Autowired private EmailService mailOut;
//	@Autowired private ChatRepository chatRepo;
	
	@Autowired 
//	@Qualifier("rbForward")
	private RabbitTemplate rabbitTemplate;
//	@Autowired private ChatService chatService;
	//@Autowired private RatingRepository ratingRepo;
	//@Autowired private LuceneSearch indexedSearch;
//	private List<String> shippingOptions = new ArrayList<String>(
//			Arrays.asList("Selbstabholung", "DHL", "UPS", "Hermes"));	
	
	@RequestMapping(value="/")
	public String index(Principal principal, Model model){
		if(principal != null)
			return "redirect:/nearHere";
		else
			return "index";
	}
	
	
	@RequestMapping("/login")
	public String login(){
		return "forms/login";
	}
	
	@RequestMapping("/register")
	public String registerGet(Model model){
		model.addAttribute("user", new User());
		model.addAttribute("address", new Address());
		return "forms/register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST )
	public String registerPost(@ModelAttribute User user,@ModelAttribute Address address, Model model){
		//TODO some Validation for User and Address
		OSMService osmService = new OSMService();
		OSMPoint osmPoint = osmService.getCoordFromAddress(address, "de");
		if(osmPoint.isPointFound() == false)
			return "redirect:/register?error=AddressNotFound";
		
		if(user.getPassword() != null){
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(user.getPassword()));
		}
		try {
			//prepare email UUID for Email authentication
			user.setUuid(UUID.randomUUID().toString());
			user.setConfirmed(false);
			//save User (user.id generation by mysql)
			userRepo.saveAndFlush(user);
			//save Address 
			User userWithId = userRepo.findByUserName(user.getUserName());
			address.setUser(userWithId);
			address.setGeoPoint(osmPoint.getLon()+","+osmPoint.getLat());
			addressRepo.saveAndFlush(address);
			//send confirmationEmail
			model.addAttribute("email",user.getEmail());
			mailOut.sendConfirmationEmail(user);
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/register?error";
			
		}
		
		return "tittles/confirmRegistration";
	}
	
	@RequestMapping("/email/confirmation")
	public String verifyEmail(@RequestParam("code") String uuid){
		User user = userRepo.findByUuid(uuid);
		//case we found the uuid
		if (user!= null){
			String exchangeName = user.getId().toString() + UUID.randomUUID().toString().substring(0, 10);
			ConnectionFactory factory = rabbitTemplate.getConnectionFactory();
			Connection connection = factory.createConnection();
			Channel channel = connection.createChannel(false);
			try {
				channel.exchangeDeclare(exchangeName, "fanout",true);
				connection.close();
				user.setExchange(exchangeName);
				user.setConfirmed(true);
				user.setUuid(null);
				userRepo.save(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error";
			}

			return "tittles/confirmEmail";
		}
		else return "error";
	}
	

	@RequestMapping(value="/offer/{id}",method=RequestMethod.GET)
	public String viewOfferById(@PathVariable Long id, Model model){
		Offer offer = offerRepo.findById(id);
		model.addAttribute("offer",offer);
		List<Image> imgList = imageRepo.findByOfferId(id);
		List<Long> imgIds = new ArrayList<Long>();
		for(Image image : imgList){
			imgIds.add(image.getId());
		}
		model.addAttribute("imageIds",imgIds);
		return "offer";
	}
	
	
	@RequestMapping(value="/user/{id}",method=RequestMethod.GET)
	public String viewUserById(@PathVariable Long id, Model model,Pageable pageable){
		User user = userRepo.findOne(id);
		model.addAttribute("user",user);
		Page<OfferDocument> offerList = elasticRepo.findOfferByUserId(user.getId(), pageable);
		List<Rating> recentRatings = ratingRepo.findTop3ByOnUserIdOrderByIdDesc(id);

		model.addAttribute("recentRatings",recentRatings);
		if(offerList != null){
			model.addAttribute("offerList",offerList);
			Pagination<OfferDocument> pagination = new Pagination<OfferDocument>(offerList,"/user/"+user.getId(),"");
			model.addAttribute("page",pagination);
		}
		return "user";
	}
	
	@RequestMapping(value="/user/{id}/rating",method=RequestMethod.GET)
	public String viewUserRating(@PathVariable Long id,Principal principal,Model model,Pageable pageable){
		User onUser = userRepo.findOne(id);
		User fromUser = userRepo.findByUserName(principal.getName());
		Page<Rating> ratingList = ratingRepo.findByOnUserIdOrderByIdDesc(onUser.getId(), pageable);
		Rating rating = new Rating();
		rating.setOnUser(onUser);
		rating.setFromUser(fromUser);
		if(ratingList != null)
			model.addAttribute("ratingList",ratingList);
		
		model.addAttribute("rating",rating);
		
		return "rating";
	}
	
	@RequestMapping(value="/user/{id}/rating",method=RequestMethod.POST)
	public String newUserRating(@PathVariable("id")Long id,Principal principal,@ModelAttribute("rating") Rating rating){
		if(rating.getStars() >= 1 && rating.getStars() <= 5 && rating.getComment() != null){
			Rating newRating = new Rating();
			newRating.setComment(rating.getComment());
			newRating.setStars(rating.getStars());
			User user = userRepo.findOne(id);
			newRating.setOnUser(user);
			newRating.setFromUser(userRepo.findByUserName(principal.getName()));
			ratingRepo.save(newRating);
			
			user.setRating(ratingRepo.getAvgStars(user));
			System.out.println(ratingRepo.getRatingCount(user));
			user.setRatingCount(ratingRepo.getRatingCount(user));
			userRepo.save(user);
			return "redirect:/user/"+id+"/rating";
		}
		else
			return "dont know yet";
	}
	
	@RequestMapping("/account")
	public String account(Model model, Principal principal){
		return "redirect:/";
	}

//	@RequestMapping(value="/test")
//	public String test(Model model,Principal principal){
////		Chat chat = new Chat();
////		
//		User user =userRepo.findByUserName(principal.getName());
//		model.addAttribute("userExchange",user.getExchange());
////		Set<User> users = new HashSet<User>();
////		users.add(user1);
////		users.add(user2);
////		
////		Offer offer = new Offer();
////		offer.setId((long) 14);
////		
////		chat.setUsers(users);
////		chat.setOffer(offer);
////		chat.setName("Anfrage Controller");
////		
////		chatRepo.save(chat);	
////		
////		
//		
//		return "test";
//	}
//	
//	@RequestMapping(value="/chat2")
//	public String chat(){
//		System.out.println("post invoked");
//		return "chat2";
//	}
//	
//	@RequestMapping(value="/home2")
//	public String testing(){
//		
//		return "account/home2";
//	}
//	@RequestMapping(value="/findAll", method=RequestMethod.GET)
//	public String findAll(Pageable pageable, Model model){
//		//Page<OfferDocument> offerList = elasticRepo.findAll(pageable);
//		List<OfferDocument> offerList = elasticRepo.findByCity("München");
////		Pagination<OfferDocument> pagination = new Pagination<OfferDocument>(offerList,"/findAll","");
////		model.addAttribute("page",pagination);
//		model.addAttribute("offerList", offerList);
//		return "findAll";
//	}
}
