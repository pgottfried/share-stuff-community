package application.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import application.document.OfferDocument;
import application.entity.Address;
import application.entity.Offer;
import application.helper.Pagination;
import application.repository.AddressRepository;
import application.repository.OfferDocumentRepository;
import application.repository.OfferRepository;
import application.service.GeoConverterService;

@Controller
public class NearHereController {
	
	@Autowired private AddressRepository addressRepo;
	private GeoConverterService geoService = new GeoConverterService();
	@Autowired private OfferDocumentRepository elasticRepo;
	@Autowired private OfferRepository offerRepo;
	
	@RequestMapping("/nearHere")
	public String getOfferNearHere(Principal principal, Model model,Pageable pageable, @RequestParam(value="radius",required=false) String radius){
		if(principal.getName() != null){
			Address addr = addressRepo.findByUserUserName(principal.getName());
			String geoPointAsString = addr.getGeoPoint();
			GeoPoint geoPoint = geoService.convertToGeoPoint(geoPointAsString);
			Page<OfferDocument> offerList = null;
			if(radius == null)
				offerList = elasticRepo.findByGeoPointWithinRadius(geoPoint, "1");
				//TODO user Submits radius -> so far net implemented by front end
			if(offerList != null){
				for(OfferDocument offer : offerList){
					Offer thatOffer = offerRepo.findOne(offer.getId());
					offer.setRating(thatOffer.getUser().getRating());
				}
				Pagination<OfferDocument> pagination = new Pagination<OfferDocument>(offerList,"/nearHere/","&radius="+radius);
				model.addAttribute("offerList", offerList);
				model.addAttribute("page",pagination);
				return "account/nearHere";
			}
			else
				return "redirect:/myCity";
		}
		
		else return "redirect:/login";
	}
	
	@RequestMapping("/myCity")
	public String getOfferMyCity(Principal principal, Model model, Pageable pageable){
		if(principal.getName() != null){
			Address addr = addressRepo.findByUserUserName(principal.getName());
			String city = addr.getCity();
			Page<OfferDocument> offerList = elasticRepo.findByCity(city,pageable);
			if(offerList!=null){
				Pagination<OfferDocument> pagination = new Pagination<OfferDocument>(offerList,"/inMyCity/","&city="+city);
				model.addAttribute("offerList", offerList);
				model.addAttribute("page",pagination);
			}
			return "account/myCity";
		}
		
		else return "redirect:/login";
		
	}
}
