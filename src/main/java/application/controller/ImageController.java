package application.controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import application.entity.Image;
import application.entity.Offer;
import application.entity.User;
import application.repository.ImageRepository;
import application.repository.OfferRepository;
import application.repository.UserRepository;


@Controller
public class ImageController {
	
	@Autowired private OfferRepository offerRepo;
	@Autowired private ImageRepository imageRepo;
	@Autowired private UserRepository userRepo;
	//@Autowired private UserRepository userRepo;
	
	
	//@RequestMapping(value="/offer/thumb/{offerId}", method = RequestMethod.GET, produces=MediaType.IMAGE_JPEG_VALUE)
	@RequestMapping("/offer/thumb/{offerId}")
	@ResponseBody
	public byte[] offerThumbnail(@PathVariable("offerId") Long offerId) throws IOException {
	    Offer offer = offerRepo.findById(offerId);
	    return offer.getThumbnail();
	}
	
	@RequestMapping(value="/offer/images/{imageId}")
	@ResponseBody
	public byte[] offerImage(@PathVariable("imageId") Long imageId) throws IOException {
	    Image offerImage = imageRepo.findOne(imageId);
	    return offerImage.getImage();
	}
	
	@RequestMapping(value="/user/img/{userId}", method = RequestMethod.GET, produces=MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] offerUserImages(@PathVariable("userId") Long userId) throws IOException {
	    User user = userRepo.findOne(userId);
	    if(user.getThumbnail() != null)
	    	return user.getThumbnail();
	    else{
	    	InputStream in = this.getClass().getResourceAsStream("/static/images/profile-img.jpg");
		    return IOUtils.toByteArray(in);
	    }
	}
	
	
	    
//	    @RequestMapping("/test/thumb/{offerId}")
//	    public ResponseEntity<byte[]> testphoto(@PathVariable("offerId") Long offerId) throws IOException {
//	    	Offer offer = offerRepo.findById(offerId);
//
//	        final HttpHeaders headers = new HttpHeaders();
//	        headers.setContentType(MediaType.IMAGE_PNG);
//
//	        return new ResponseEntity<byte[]>(offer.getThumbnail(), headers, HttpStatus.CREATED);
//	    }
//	    
}
