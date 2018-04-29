package application.controller;

import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import application.document.OfferDocument;
import application.entity.Address;
import application.entity.Category;
import application.entity.Image;
import application.entity.Offer;
import application.entity.User;
import application.formInputObject.AddOfferInput;
import application.helper.Categoryi18;
import application.repository.AddressRepository;
import application.repository.CategoryRepository;
import application.repository.OfferDocumentRepository;
import application.repository.ImageRepository;
import application.repository.OfferRepository;
import application.repository.UserRepository;
import application.service.GeoConverterService;
import application.service.OfferService;



@Controller
public class AddOfferController {
	
	@Autowired private OfferDocumentRepository elasticRepo;
	@Autowired private MessageSource messageSource;
	@Autowired private CategoryRepository categoryRepo;
	@Autowired private OfferRepository offerRepo;
	@Autowired private UserRepository userRepo;
	@Autowired private ImageRepository imageRepo;
	@Autowired private AddressRepository addressRepo;
	@Autowired private OfferService offerService;
	private static final Integer MAX_NUMBER_OF_IMAGES = 5;
	
	private final String tempPicPath = "C:/Users/Paule/Pictures/webapp uploads/temporary";
//	private List<String> shippingOptions = new ArrayList<String>(
//			Arrays.asList("Selbstabholung", "DHL", "UPS", "Hermes"));	
//	private List<String> paymentOptions = new ArrayList<String>(
//			Arrays.asList("Bar", "Paypal", "Überweisung"));	
	private List<String> offerStates = new ArrayList<String>(
			Arrays.asList("available","in use"));
	
	
	
	@RequestMapping(method = RequestMethod.GET, value ="/test")
	public String addOfferGet() {
		return "test";
	}
	
	@RequestMapping(method = RequestMethod.GET, value ="/addOffer")
	public String addOfferGet(Model model) {
		model.addAttribute("offerInput",new AddOfferInput());
		List<Categoryi18> categorysi18 = new ArrayList<Categoryi18>();
		List<Category> categorys = categoryRepo.findAll();
		for(Category category : categorys){
			String categoryNamei18 = messageSource.getMessage("category."+category.getName().toLowerCase() , null, LocaleContextHolder.getLocale() );
			Categoryi18 categoryi18 = new Categoryi18(category.getId(),category.getName(),categoryNamei18);
			categorysi18.add(categoryi18);
		}
		model.addAttribute("categorysi18",categorysi18);
		return "forms/addOffer";
	}
	
	
	
	@RequestMapping(value = "/addOffer", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> handleFormInput(Principal principal,
								  @RequestBody AddOfferInput offerInput){	  
		Map<String,String> jsonResponse = new HashMap<String,String>();
		File storageDirectory = new File(tempPicPath +"/"+ principal.getName());
		if(!storageDirectory.exists() || storageDirectory.listFiles().length > 1){
			String message = messageSource.getMessage("addOffer.noPicture", null, LocaleContextHolder.getLocale());
			jsonResponse.put("error", message);
			return jsonResponse;
			
					//tell user he has to upload a picture
		}
		
		if(!offerInput.getTitle().matches(".*\\w.*")){	
			String message = messageSource.getMessage("addOffer.titleNoLetter", null, LocaleContextHolder.getLocale());	
			jsonResponse.put("error", message);
			return jsonResponse;
		}
		
		if(offerInput.getTitle().trim().length() > 64){
			String message = messageSource.getMessage("addOffer.titleToLong", null, LocaleContextHolder.getLocale());	
			jsonResponse.put("error", message);
			return jsonResponse;
		}
		
		
		
		if(offerInput.getDescription().trim().length() > 255){
			String message = messageSource.getMessage("addOffer.descriptionToLong", null, LocaleContextHolder.getLocale());	
			jsonResponse.put("error", message);
			return jsonResponse;
		}
		
		Offer offer = new Offer();
		offer.setTitle(offerInput.getTitle().trim());
		offer.setDescription(offerInput.getDescription().trim());
		offer.setStatus(offerStates.get(0));
		//Set Category 
		offer.setCategory(categoryRepo.findByName(offerInput.getCategory()));
		User user = userRepo.findByUserName(principal.getName());
		offer.setUser(user);
		Address address = addressRepo.findByUserUserName(principal.getName());
		offer.setGeoPoint(address.getGeoPoint());
		File[] files = storageDirectory.listFiles();
		// at upload time a thumbnail folder is created, avoid this one
		int firstPicIndex=0;
		if(files[0].isDirectory())
			firstPicIndex=1;
		String thumbExtension = files[firstPicIndex].getName().substring(files[firstPicIndex].getName().lastIndexOf(".") + 1);
		BufferedImage offerThumbnail;
			
		try {
			byte[] imageInByte = offerService.createThumbnailFromFile(files[firstPicIndex]);
			offer.setThumbnail(imageInByte);
			// persist Offer with thumb to DB
			offer = offerRepo.save(offer);
		} catch (IllegalArgumentException | ImagingOpException e1) {
			// TODO Auto-generated catch block
			// return some failure message --> please try again later..
			e1.printStackTrace();
		}
		// iterate through all pictures and save them to the database
		//Offer offer = offerRepo.findFirstByUserUserNameOrderByIdDesc(principal.getName());
		for(File file : files){
			if(!file.isDirectory()){
				try {	 //save all pics to image table
						Image image = new Image();
						image.setImage(Files.readAllBytes(file.toPath()));
						String picName = file.getName();
						if(picName.length() > 32){
							String suffix = picName.substring(picName.lastIndexOf("."));
							String picTitle = picName.substring(0, 26);
							picName = picTitle.concat(suffix);
						}
						image.setName(picName);
						image.setOffer(offer);
						imageRepo.save(image);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					// if this fails remove entry from database
					// return some failure page
					e.printStackTrace();	
				}		 
			}
		}
		
		// copy fields from thisOffer
		addressRepo.findByUserUserName(principal.getName());
		OfferDocument offerElastic = new OfferDocument();
		offerElastic.setId(offer.getId());
		offerElastic.setTitle(offer.getTitle());
		offerElastic.setCategory(offer.getCategory().getName());
		offerElastic.setDescription(offer.getDescription());
		offerElastic.setUser_id(offer.getUser().getId());
		offerElastic.setStatus(offer.getStatus());
		GeoConverterService geoConverter = new GeoConverterService();
		offerElastic.setGeoPoint(geoConverter.convertToGeoPoint(address.getGeoPoint()));
		offerElastic.setCity(address.getCity());
		offerElastic.setZip(address.getPostalCode());
		
		// save to elasticsearch
		elasticRepo.save(offerElastic);
		
		// Delete temporary Folder
		try {
			FileUtils.deleteDirectory(storageDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = messageSource.getMessage("addOffer.postSuccess", null, LocaleContextHolder.getLocale());
		jsonResponse.put("success", message);
		return jsonResponse;
	}
	

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> upload(MultipartHttpServletRequest request, Principal principal) {
		Iterator<String> itr = request.getFileNames();
        MultipartFile mpf;
        List<Image> list = new LinkedList<>();
        
        while (itr.hasNext()) {
            mpf = request.getFile(itr.next());
            String filename = mpf.getOriginalFilename();
//            String pat1 = "^[^-\\s][a-zA-Z0-9_\\s-]+$";
//            String pat2 = "([^\\s]+(\\.(?i)(jpg|png|gif))$)";
//            String pat3 = "(^[^-\\s][a-zA-Z0-9_\\s-]+(\\.(?i)(jpg|png|gif))$)";
            String pat4 = "(.+(\\.(?i)(jpg|png|gif))$)";
            if(!filename.matches(pat4)){
            	Map<String,Object> error = new HashMap<>();
                error.put("error", "Could not upload file "+mpf.getOriginalFilename());
                return error;
            }
            //temporary Storage
            String storageDirectory = tempPicPath + "/" + principal.getName();
            File newUserFolder = new File(storageDirectory);
            String storageDirThumbnail = storageDirectory + "/thumbnail";
            File thumbnailFolder = new File(storageDirThumbnail);
	            if(!newUserFolder.exists()){
	            	newUserFolder.mkdir();
	            	thumbnailFolder.mkdir();
	            }
	        
	            
            File newFile = new File(storageDirectory + "/" + filename);
            try {
                mpf.transferTo(newFile);
                
                BufferedImage thumbnail = Scalr.resize(ImageIO.read(newFile), 90);
                String thumbFileExtension = filename.substring(filename.lastIndexOf(".")+1);
                //String thumbnailFilename = filename.substring(0, filename.lastIndexOf(".")) + "-thumbnail." + thumbFileExtension;
                File thumbnailFile = new File(thumbnailFolder + "/" + filename);
                ImageIO.write(thumbnail, thumbFileExtension, thumbnailFile);
                
                Image image = new Image();
                image.setName(filename);
                //image.setUrl("/picture/");
                image.setThumbnailUrl("/thumbnail/" +filename);
                image.setDeleteUrl("/delete/" + filename);
                image.setDeleteType("DELETE");
                
                list.add(image);
                
            } catch(Exception e) {
//                System.out.println("Could not upload file "+mpf.getOriginalFilename());
                Map<String,Object> error = new HashMap<>();
                error.put("error", "Could not upload file "+mpf.getOriginalFilename());
                return error;
//                e.printStackTrace();
            }
        }
		Map<String, Object> files = new HashMap<>();
        files.put("files", list);
        return files;
	}
	
	
	
	
	@RequestMapping(value="/editOffer", method=RequestMethod.GET)
	public String offerEditGet(@RequestParam("id") Long offerId, Model model, Principal principal){
		
		String userName = principal.getName();
		User user = userRepo.findByUserName(userName);
		Offer offer = offerRepo.findOne(offerId);
		if(user.getId().equals(offer.getUser().getId())){
			AddOfferInput offerInput = new AddOfferInput(offer);
			model.addAttribute("offerInput",offerInput);
			List<Categoryi18> categorysi18 = new ArrayList<Categoryi18>();
			List<Category> categorys = categoryRepo.findAll();
			for(Category category : categorys){
				String categoryNamei18 = messageSource.getMessage("category."+category.getName().toLowerCase() , null, LocaleContextHolder.getLocale() );
				Categoryi18 categoryi18 = new Categoryi18(category.getId(),category.getName(),categoryNamei18);
				categorysi18.add(categoryi18);
			}
			model.addAttribute("categorysi18",categorysi18);
			List<Image> imgList = imageRepo.findByOfferId(offerId);
			for(Image img : imgList){
				img.setDeleteType("DELETE");
				img.setDeleteUrl("/delete/intern/"+img.getId());
			}
			
			model.addAttribute("imgList",imgList);
			model.addAttribute("offer",offer);
			return "/forms/editOffer";
		}
		return("redirect:myOffers");
	}
	
	
	@RequestMapping(value="/editOffer", method=RequestMethod.POST)
	public String offerEditPost(@RequestParam("offerId") Long offerId, @ModelAttribute("offerInput") AddOfferInput offerInput, Model model, Principal principal){
		String userName = principal.getName();
		User user = userRepo.findByUserName(userName);
		Offer offer = offerRepo.findOne(offerId);
		if(user.getId().equals(offer.getUser().getId())){
			offer.setDescription(offerInput.getDescription().trim());
			offer.setTitle(offerInput.getTitle().trim());
			offer.setCategory(categoryRepo.findByName(offerInput.getCategory().trim()));
			offer.setUpdated(new Date());
			
			List<Image> imgList = imageRepo.findByOfferId(offerId);
			File storageDirectory = new File(tempPicPath +"/"+ principal.getName());
			File[] files = storageDirectory.listFiles();
			
			// case all pictures are deleted! Backup with Thumbnail
			if(!storageDirectory.exists() || storageDirectory.listFiles() == null && imgList.size()==0){
		       Image imgFromThumb = new Image();
		       imgFromThumb.setImage(offer.getThumbnail());
		       imgFromThumb.setOffer(offer);
		       imageRepo.save(imgFromThumb);		  
			}
			//case all Deleted but new Uploaded
			if( imgList.size() == 0){
				int firstPicIndex=0;
				if(files[0].isDirectory())
					firstPicIndex=1;//+ (files.length-1)) <= MAX_NUMBER_OF_IMGAGES ){
				byte[] thumbnail = offerService.createThumbnailFromFile(files[firstPicIndex]);
				offer.setThumbnail(thumbnail);
				int counterSavedPics = 0;
				for(File file : files){
					if(!file.isDirectory() && counterSavedPics <= MAX_NUMBER_OF_IMAGES){
						Image image = offerService.readImgfromFile(file);
						if(image!=null){
							image.setOffer(offer);
							imageRepo.save(image);
							counterSavedPics++;
						}
					}
				}
			}
			int numberExistingImgs =imgList.size()-1;
			if(numberExistingImgs > 0){
				int counterSavedPics = 0;
				for(File file : files){
					if(!file.isDirectory() && (numberExistingImgs + counterSavedPics) <= MAX_NUMBER_OF_IMAGES){
						Image image = offerService.readImgfromFile(file);
						if(image!=null){
							image.setOffer(offer);
							imageRepo.save(image);
							counterSavedPics++;
						}
					}
				}
			}
			
		}
		return "account/myOffers";
	}
	
	
	@RequestMapping(value="/thumbnail/{name:.+}",method = RequestMethod.GET)
	@ResponseBody
	public void getThumbnail(HttpServletResponse response, Principal principal,@PathVariable("name") String name) throws IOException{
		File file = new File(tempPicPath + "/" + principal.getName() + "/thumbnail/" + name);
		String fileExtension = name.substring(name.lastIndexOf(".")+1);
		String mediaType = getMediaType(fileExtension);
		InputStream is = new FileInputStream(file);
		response.setContentType(mediaType);
	    response.setContentLengthLong(file.length());
	    //response.getOutputStream().write(imageBytes);
        IOUtils.copy(is, response.getOutputStream());
        is.close();
	}
	

	
	//Deletes Pics from DB
	@RequestMapping(value="/delete/intern/{id}")
	@ResponseBody
	public List<Map<String, Object>> deleteFromDB(@PathVariable("id") Long imageId, Principal principal) throws IOException{
		
		Image image = imageRepo.findOne(imageId);
		imageRepo.delete(image);
		
		List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> success = new HashMap<>();
        success.put("success", true);
        results.add(success);
        return results;
	}
	
	//Deletes temporary saved Images
	@RequestMapping(value="/delete/{name:.+}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> delete(@PathVariable("name") String name, Principal principal) throws IOException{
		System.out.println("delete invoked");
		File storageDirectory = new File(tempPicPath +"/"+ principal.getName());
		File[] files = storageDirectory.listFiles();
		
        File imageFile = new File(tempPicPath +"/"+ principal.getName() +"/"+ name);
        imageFile.delete();
        File thumbnailFile = new File(tempPicPath+"/"+ principal.getName() +"/thumbnail/"+ name);
        thumbnailFile.delete();
        
        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> success = new HashMap<>();
        success.put("success", true);
        results.add(success);
        return results;
	}

	
	
	private String getMediaType(String fileExtension){
		String mediaType = null;
		if(fileExtension.toLowerCase() == "png")
			mediaType = MediaType.IMAGE_PNG_VALUE;
		if(fileExtension.toLowerCase() == "jpg")
			mediaType = MediaType.IMAGE_JPEG_VALUE;
		if(fileExtension.toLowerCase() == "gif")
			mediaType = MediaType.IMAGE_GIF_VALUE;
		
		return mediaType;
	}

	public static Integer getMaxNumberOfImages() {
		return MAX_NUMBER_OF_IMAGES;
	}
	
//	@RequestMapping(method = RequestMethod.POST, value = "/addOffer")
//	public String handleFormInput(@RequestParam("name") String name,
//								   RedirectAttributes redirectAttributes) {
//		
//		
//		
//		
//		return "abc";
//		
//	}
//		if (name.contains("/")) {
//			redirectAttributes.addFlashAttribute("message", "Folder separators not allowed");
//			return "redirect:/addOffer";
//		}
//		if (name.contains("/")) {
//			redirectAttributes.addFlashAttribute("message", "Relative pathnames not allowed");
//			return "redirect:/addOffer";
//		}
//		
//
//		if (!file.isEmpty()) {
//			try {
//				BufferedOutputStream stream = new BufferedOutputStream(
//						new FileOutputStream(new File(tempPicPath + name)));
//                FileCopyUtils.copy(file.getInputStream(), stream);
//				stream.close();
//				redirectAttributes.addFlashAttribute("message",
//						"You successfully uploaded " + name + "!");
//			}
//			catch (Exception e) {
//				redirectAttributes.addFlashAttribute("message",
//						"You failed to upload " + name + " => " + e.getMessage());
//			}
//		}
//		else {
//			redirectAttributes.addFlashAttribute("message",
//					"You failed to upload " + name + " because the file was empty");
//		}
//
//		return "redirect:/addOffer";
//	}


//	public List<String> getShippingOptions() {
//		return shippingOptions;
//	}
//
//
//	public void setShippingOptions(List<String> shippingoptions) {
//		this.shippingOptions = shippingoptions;
//	}
	
//	List<CheckboxItem> shippingCheckboxes = new ArrayList<CheckboxItem>();
//	for(String shippingOption : shippingOptions){
//		CheckboxItem shippingCheckbox = new CheckboxItem(shippingOption);
//		shippingCheckboxes.add(shippingCheckbox);
//	}
//	model.addAttribute("shippingList",shippingCheckboxes);
//	
//	List<CheckboxItem> paymentCheckboxes = new ArrayList<CheckboxItem>();
//	for(String paymentOption : paymentOptions){
//		CheckboxItem paymentCheckbox = new CheckboxItem(paymentOption);
//		paymentCheckboxes.add(paymentCheckbox);
//	}
//	model.addAttribute("paymentList",paymentCheckboxes);	
}
