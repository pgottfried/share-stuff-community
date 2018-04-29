package application.controller;

import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
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

import application.entity.Address;
import application.entity.Image;
import application.entity.Offer;
import application.entity.User;
import application.helper.OSMPoint;
import application.repository.AddressRepository;
import application.repository.ImageRepository;
//import application.entity.Request;
import application.repository.OfferRepository;
import application.repository.UserRepository;
//import application.repository.RequestRepository;
import application.service.OSMService;



@Controller
public class AccountController {
	
	@Autowired private OfferRepository offerRepo;
	@Autowired private UserRepository userRepo;
	@Autowired private AddressRepository addressRepo;
	@Autowired private ImageRepository imageRepo;
	

	@RequestMapping("/profile")
	public String accountProfil(Principal principal,Model model) {
		User user = userRepo.findByUserName(principal.getName());
		Address address =addressRepo.findOne(user.getId());
		model.addAttribute("user",user);
		model.addAttribute("address", address);
		return "account/profile";
	}
	
	@RequestMapping(value="/profile/changeUserImage",method=RequestMethod.POST)
	@ResponseBody
	public String changePicture(@RequestParam("file") MultipartFile file, Principal principal){
		
		//resize file, if its no image exception will be thrown
		BufferedImage image = null;
		try {
			 image = Scalr.resize(ImageIO.read(file.getInputStream()), 250);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return "error";
		} catch (ImagingOpException e) {
			e.printStackTrace();
			return "error";
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
		byte[] imageByte =null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", baos);
			baos.flush();
			imageByte = baos.toByteArray();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		//update pic to database
		if(imageByte != null){
			User user = userRepo.findByUserName(principal.getName());
			user.setThumbnail(imageByte);
			userRepo.save(user);
			return "success";
		}
		else return "error";

	}
	
	@RequestMapping(value="/profile/changeUserData",method=RequestMethod.POST)
	@ResponseBody
	public String changeUserData(@RequestBody User newDataUser, Principal principal){
		User user = userRepo.findByUserName(principal.getName());
		String newFirst =newDataUser.getFirstName();
		String newLast = newDataUser.getLastName();
		boolean hasChanged = false;
		if( newFirst.length() > 1 && !(newFirst.equals(user.getFirstName()))){
			newFirst = newFirst.trim();
			user.setFirstName(newFirst);
			hasChanged = true;
		}
		if( newLast.length() > 1 && !(newLast.equals(user.getLastName()))){
			newLast = newLast.trim();
			user.setLastName(newLast);
			hasChanged = true;
		}
		if(hasChanged)
			userRepo.save(user);
		
		return "success";
	}
	
	@RequestMapping(value="/profile/changeAddress",method=RequestMethod.POST)
	@ResponseBody
	public String changeUserAddress(@RequestBody Address newAddress, Principal principal){
		String nr = newAddress.getHouseNumber().trim();
		String street = newAddress.getStreet().trim();
		String city = newAddress.getCity().trim();
		String postalCode = newAddress.getPostalCode().trim();
		Address address = addressRepo.findByUserUserName(principal.getName());
		boolean hasChanged=false;
		if(nr.length()>0 && !(nr.equals(address.getHouseNumber()))){
			hasChanged=true;
		}
		if(street.length()>2 && !(street.equals(address.getStreet()))){
			hasChanged=true;
		}
		if(city.length()>2 && !(city.equals(address.getCity()))){
			hasChanged=true;
		}
		if(postalCode.length()>2 && !(postalCode.equals(address.getPostalCode()))){
			hasChanged=true;
		}
		if(hasChanged){
			OSMService osmService = new OSMService();
			OSMPoint osmPoint = osmService.getCoordFromAddress(newAddress, "de");
			if(osmPoint.isPointFound() == false)
				return "Error address not found, please check your input";
			else{
				address.setHouseNumber(nr);
				address.setPostalCode(postalCode);
				address.setStreet(street);
				address.setCity(city);
				address.setGeoPoint(osmPoint.getLon()+","+osmPoint.getLat());
				addressRepo.save(address);
				return "success";
			}
			
		}
		return "success";
	}
	
	
	@RequestMapping("/myOffers")
	public String offers(Model model, Principal principal){
		String userName = principal.getName();
		List<Offer> offerList = offerRepo.findByUserUserName(userName);
		if(offerList != null){
			model.addAttribute(offerList);
			Map<String,List<Long>> offerImageMap = new HashMap<String,List<Long>>();
			for(Offer offer: offerList){
				List<Image> imgList = imageRepo.findByOfferId(offer.getId());
				List<Long> imgIds = new ArrayList<Long>();
				for(Image image : imgList){
					imgIds.add(image.getId());
				}
				offerImageMap.put(offer.getId().toString(), imgIds);
			}
			model.addAttribute("offerImageMap",offerImageMap);
		}
		return "account/myOffers";
	}
	
	
	
	
	
}
