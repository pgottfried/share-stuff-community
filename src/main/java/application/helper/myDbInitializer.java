//package application.helper;
//
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//import javax.imageio.ImageIO;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//import org.hibernate.search.jpa.FullTextEntityManager;
//import org.hibernate.search.jpa.Search;
//import org.imgscalr.Scalr;
//import org.springframework.amqp.rabbit.connection.Connection;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import com.rabbitmq.client.Channel;
//
//import application.document.OfferDocument;
//import application.entity.Address;
//import application.entity.Category;
//import application.entity.Offer;
//import application.entity.OfferState;
//import application.entity.User;
//import application.entity.Image;
//import application.repository.AddressRepository;
//import application.repository.CategoryRepository;
//import application.repository.ImageRepository;
//import application.repository.OfferDocumentRepository;
//import application.repository.OfferRepository;
//import application.repository.OfferStateRepository;
//import application.repository.UserRepository;
//import application.service.GeoConverterService;
//import application.service.OSMService;
//
//@Component
//public class myDbInitializer implements ApplicationListener<ApplicationReadyEvent>{
//	
//	@PersistenceContext private EntityManager entityManager;
//	@Autowired private OfferDocumentRepository elasticRepo;
//	@Autowired private CategoryRepository categoryRepo;
//	@Autowired private OfferRepository offerRepo;
//	@Autowired private OfferStateRepository offerStateRepo;
//	@Autowired private ImageRepository imageRepo;
//	@Autowired private UserRepository userRepo;
//	@Autowired private AddressRepository addressRepo;
//	@Autowired private RabbitTemplate rabbitTemplate;
//	
//	private List<String> categoryNames = new ArrayList<String>(
//			Arrays.asList("Books", "Electronics", "Home", "Industrial", "Others"));
//	private List<String> offerStates = new ArrayList<String>(
//			Arrays.asList("available", "in use"));
//	private String pathToCategoryDir = "C:/Users/Paule/Pictures/Bilder/offers";
//	private final String description = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
//	
//	@Override
//	public void onApplicationEvent(ApplicationReadyEvent event) {
//		System.out.println("OnApplicationEvent Started");
//		createUsers();//Paul,Peter,Christian,Tobias,Raphael
//		initDBCategorys();
//		generateOffersFromCategoryFoldersWithIndexingToElasticSearch();
//		//generateOffersFromCategoryFolders();
//		//indexOffersLuceneLocal();
//		System.out.println("OnApplicationEvent Ended");
//		
//	}
//	
//	public void initDBofferStates(){
//		for(String offerState : offerStates){
//			OfferState stateEntity = new OfferState();
//			stateEntity.setStatus(offerState);
//			offerStateRepo.saveAndFlush(stateEntity);
//		}
//	}
//	
//	public void initDBCategorys(){
//		
//		for(String category : categoryNames){
//			System.out.println("Persisting to Table Category. New Column: " + category);
//			Category categoryEntity = new Category();
//			categoryEntity.setName(category);
//			categoryRepo.saveAndFlush(categoryEntity);
//		}
//	}
//	public void createRabbitExchange(String exchangeName){
//		ConnectionFactory factory = rabbitTemplate.getConnectionFactory();
//		Connection connection = factory.createConnection();
//		Channel channel = connection.createChannel(false);
//	 	
//			try {
//				channel.exchangeDeclare(exchangeName, "fanout",true);
//			} catch (IOException e) {
//				e.printStackTrace();
//				
//			}
//	 	
//	 	connection.close();
//	}
//	
//	public void createUsers(){
//		OSMService osmService = new OSMService();
//		
//		
//		User user = new User();
//		Address address = new Address();
//		user.setFirstName("Paul");
//		user.setLastName("Gottfried");
//		user.setEmail("name@mail.local");
//		user.setUserName("Paul");
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		user.setPassword(encoder.encode("paul"));
//		user.setConfirmed(true);
//		user.setExchange("1" + UUID.randomUUID().toString().substring(0, 10));
//		User thisUser = userRepo.save(user);
//		createRabbitExchange(thisUser.getExchange());
//		address.setCity("München");
//		address.setPostalCode("80335");
//		address.setStreet("Loristraße");
//		address.setHouseNumber("28");
//		address.setUser(thisUser);
//		OSMPoint osmPoint = osmService.getCoordFromAddress(address, "de");
//		address.setGeoPoint(osmPoint.getLat()+","+osmPoint.getLon());
//		addressRepo.save(address);
//		
//		user = new User();
//		address = new Address();
//		user.setFirstName("Peter");
//		user.setLastName("Retzer");
//		user.setEmail("name@mail.local");
//		user.setUserName("Peter");
//		user.setPassword(encoder.encode("peter"));
//		user.setConfirmed(true);
//		user.setExchange("2" + UUID.randomUUID().toString().substring(0, 10));
//		thisUser =userRepo.save(user);
//		createRabbitExchange(thisUser.getExchange());
//		address.setCity("München");
//		address.setPostalCode("80809");
//		address.setStreet("Spiridon-Louis-Ring");
//		address.setHouseNumber("21");
//		address.setUser(thisUser);
//		osmPoint = osmService.getCoordFromAddress(address, "de");
//		address.setGeoPoint(osmPoint.getLat()+","+osmPoint.getLon());
//		addressRepo.save(address);
//		
//		user = new User();
//		address = new Address();
//		user.setFirstName("Christian");
//		user.setLastName("Helbig");
//		user.setEmail("name@mail.local");
//		user.setUserName("Christian");
//		user.setPassword(encoder.encode("christian"));
//		user.setConfirmed(true);
//		user.setExchange("3" + UUID.randomUUID().toString().substring(0, 10));
//		thisUser = userRepo.save(user);
//		createRabbitExchange(thisUser.getExchange());
//		address.setCity("München");
//		address.setPostalCode("80339");
//		address.setStreet("Westendstraße");
//		address.setHouseNumber("10");
//		address.setUser(thisUser);
//		osmPoint = osmService.getCoordFromAddress(address, "de");
//		address.setGeoPoint(osmPoint.getLat()+","+osmPoint.getLon());
//		addressRepo.save(address);
//		
//		user = new User();
//		address = new Address();
//		user.setFirstName("Tobias");
//		user.setLastName("Kleiser");
//		user.setEmail("name@mail.local");
//		user.setUserName("Tobias");
//		user.setPassword(encoder.encode("tobias"));
//		user.setConfirmed(true);
//		user.setExchange("4" + UUID.randomUUID().toString().substring(0, 10));
//		thisUser = userRepo.save(user);
//		createRabbitExchange(thisUser.getExchange());
//		address.setCity("München");
//		address.setPostalCode("80339");
//		address.setStreet("Westendstraße");
//		address.setHouseNumber("20");
//		address.setUser(thisUser);
//		osmPoint = osmService.getCoordFromAddress(address, "de");
//		address.setGeoPoint(osmPoint.getLat()+","+osmPoint.getLon());
//		addressRepo.save(address);
//		
//		user = new User();
//		address = new Address();
//		user.setFirstName("Raphael");
//		user.setLastName("Habermann");
//		user.setEmail("name@mail.local");
//		user.setUserName("Raphael");
//		user.setPassword(encoder.encode("raphael"));
//		user.setConfirmed(true);
//		user.setExchange("5" + UUID.randomUUID().toString().substring(0, 10));
//		thisUser = userRepo.save(user);
//		address.setCity("Freising");
//		createRabbitExchange(thisUser.getExchange());
//		address.setPostalCode("85354");
//		address.setStreet("Wippenhauser Str");
//		address.setHouseNumber("5");
//		address.setUser(thisUser);
//		osmPoint = osmService.getCoordFromAddress(address, "de");
//		address.setGeoPoint(osmPoint.getLat()+","+osmPoint.getLon());
//		addressRepo.save(address);	
//	}
//	
//	public void generateOffersFromCategoryFolders(){
//		User user = new User();
//		long categoryId = 1;
//		try {
//			File[] categoryFolders = new File(pathToCategoryDir+"/").listFiles();
//			for(File categoryFolder : categoryFolders){
//				user.setId(categoryId);
//				File[] categoryPics = new File(pathToCategoryDir + "/" + categoryFolder.getName()).listFiles();
//				
//				for(File categoryPic : categoryPics){
//					//resize image and convert to bytes
//					BufferedImage thumbnail = Scalr.resize(ImageIO.read(categoryPic), 290);
//					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					ImageIO.write( thumbnail, "jpg", baos );
//					baos.flush();
//					byte[] imageInByte = baos.toByteArray();
//					baos.close();
//					
//					Offer offer = new Offer();
//					Category category = new Category();
//					category.setId(categoryId);
//					offer.setCategory(category);
//					offer.setUser(user);
//					offer.setTitle(categoryPic.getName().split("\\.")[0]);
//					offer.setDescription(description);
//					offer.setThumbnail(imageInByte);
//					offer.setStatus("available");
////					offer.setDhl(true);
////					offer.setPaypal(true);
////					offer.setUps(true);
////					offer.setCash(true);
//					
//					offerRepo.saveAndFlush(offer);
//	
//				}
//				
//				categoryId++;	
//			}
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void indexOffersLuceneLocal(){
//		 try {
//		      FullTextEntityManager fullTextEntityManager =
//		        Search.getFullTextEntityManager(entityManager);
//		      fullTextEntityManager.createIndexer().startAndWait();
//		      System.out.println("INDEXING COMPLETED");
//		    }
//		    catch (InterruptedException e) {
//		      System.out.println(
//		        "An error occurred trying to build the serach index: " +
//		         e.toString());
//		    }
//		    return;
//	}
//	
//	public void generateOffersFromCategoryFoldersWithIndexingToElasticSearch(){
//		User user = new User();
//		Address address = new Address();
//		String geoPointString = null;
//		long categoryId = 1;
//		try {
//			File[] categoryFolders = new File(pathToCategoryDir+"/").listFiles();
//			for(File categoryFolder : categoryFolders){
//				user.setId(categoryId);
//				address = addressRepo.findByUserId(user.getId());
//				geoPointString = address.getGeoPoint();
//				File[] categoryPics = new File(pathToCategoryDir + "/" + categoryFolder.getName()).listFiles();
//				
//				for(File categoryPic : categoryPics){
//					//resize image and convert to bytes
//					BufferedImage thumbnail = Scalr.resize(ImageIO.read(categoryPic), 290);
//					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					ImageIO.write( thumbnail, "jpg", baos );
//					baos.flush();
//					byte[] thumbInByte = baos.toByteArray();
//					baos.close();
//					
//					Offer offer = new Offer();
//					Category category = new Category();
//					category.setId(categoryId);
//					offer.setCategory(category);
//					offer.setUser(user);
//					offer.setTitle(categoryPic.getName().split("\\.")[0]);
//					offer.setDescription(description);
//					offer.setThumbnail(thumbInByte);
//					offer.setStatus("available");
//					offer.setGeoPoint(geoPointString);
////					offer.setCash(true);
////					offer.setCashAndCarry(true);
//					
//					offerRepo.save(offer);
//					
//					Offer thisOffer = offerRepo.findFirstByUserIdOrderByIdDesc(categoryId);
//					
//					Image image = new Image();
//					String picName = categoryPic.getName();
//					String suffix = null;
//					String picTitle = null;
//					
//					if(picName.length() > 32){
//						suffix = picName.substring(picName.lastIndexOf("."));
//						picTitle = picName.substring(0, 26);
//						picName = picTitle.concat(suffix);
//					}
//					
//					BufferedImage pic = ImageIO.read(categoryPic);
//					ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
//					ImageIO.write( pic, "jpg", baos2 );
//					baos.flush();
//					byte[] imageInByte = baos2.toByteArray();
//					baos.close();
//					
//					image.setName(picName);
//					image.setOffer(thisOffer);
//					image.setImage(imageInByte);
//					imageRepo.save(image);
//					
//					
//					OfferDocument offerElastic = new OfferDocument();
//					offerElastic.setId(thisOffer.getId());
//					offerElastic.setTitle(thisOffer.getTitle());
//					offerElastic.setCategory(thisOffer.getCategory().getName());
//					offerElastic.setDescription(thisOffer.getDescription());
//					offerElastic.setUser_id(thisOffer.getUser().getId());
//					offerElastic.setStatus(thisOffer.getStatus());
//					GeoConverterService geoConverter = new GeoConverterService();
//					offerElastic.setGeoPoint(geoConverter.convertToGeoPoint(geoPointString));
//					offerElastic.setCity(address.getCity());
//					offerElastic.setZip(address.getPostalCode());
//					elasticRepo.save(offerElastic);
//					System.out.println("Persisted to Elasticsearch: " + offerElastic.getTitle());
//				}
//				
//				categoryId++;	
//			}
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	
//}
