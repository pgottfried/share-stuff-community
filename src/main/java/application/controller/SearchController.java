package application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import application.document.OfferDocument;
import application.helper.MyBucket;
import application.helper.Pagination;
import application.repository.OfferDocumentRepository;
import application.service.ElasticsearchService;


@Controller
public class SearchController {
	
	@Autowired private OfferDocumentRepository elasticRepo;
	@Autowired private MessageSource messageSource;
	@Autowired private ElasticsearchService elasticService;
	
	@RequestMapping(value="/search/all",method=RequestMethod.GET)
	public String searchAll(Model model){
		Iterable<OfferDocument> offerList = elasticRepo.findAll();
		if(offerList != null)
			model.addAttribute("offerList",offerList);
		return "serach";
	}
	

	
	@RequestMapping(value="/search/title", method=RequestMethod.GET)
	public String search(@RequestParam(value="term",required=true) String term,@RequestParam(value="cityOrZip",required=false) String cityOrZip, Model model, Pageable pageable) {
		if(cityOrZip.isEmpty()){
			 FacetedPage<OfferDocument> offerList = elasticRepo.findOfferByTitleCustom2(term,pageable);
			if(offerList != null){
				List<MyBucket> categoryAggr = elasticService.extractBucketList(offerList, "categorys");
				List<MyBucket> cityAggr = elasticService.extractBucketList(offerList, "citys");
				model.addAttribute("offerList", offerList);
				model.addAttribute("categoryAggr",categoryAggr);
				model.addAttribute("cityAggr",cityAggr);
				model.addAttribute("page",new Pagination<OfferDocument>(offerList,"/search/title",""));
			}
		}
		else if (cityOrZip.matches("\\d+")){
			Page<OfferDocument> offerList = elasticRepo.findOfferByTitleAndZipCustom(term, cityOrZip, pageable);
			if(offerList != null){
				model.addAttribute("offerList", offerList);
				model.addAttribute("page",new Pagination<OfferDocument>(offerList,"/search/title","&cityOrZip="+cityOrZip));
			}
		}
		else {
			Page<OfferDocument> offerList = elasticRepo.findOfferByTitleAndCityCustom(term, cityOrZip, pageable);
			if(offerList != null){
				model.addAttribute("offerList", offerList);
				model.addAttribute("page",new Pagination<OfferDocument>(offerList,"/search/title","&cityOrZip="+cityOrZip));
			}
		}
		
		model.addAttribute("term",term);
		model.addAttribute("cityOrZip",cityOrZip);
		return "search";
	}
	
	@RequestMapping(value="/search/{term}", method=RequestMethod.GET)
	public String searchTermAndCategory(@PathVariable("term") String term,@RequestParam(value="category",required=false) String category,@RequestParam(value="city",required=false) String city, Model model, Pageable pageable) {
		Page<OfferDocument> offerList = null;
		Pagination<OfferDocument> pagination = null;
		String aggrKey = null;
		if(category != null){
			offerList = elasticRepo.findOfferByTitleAndCategoryCustom(term,category,pageable);
			pagination = new Pagination<OfferDocument>(offerList,"/search/"+term,"&category="+category);
			aggrKey = category;
		}
		if(city != null){
			offerList = elasticRepo.findOfferByTitleAndCityCustom(term,city,pageable);
			pagination = new Pagination<OfferDocument>(offerList,"/search/"+term,"&city="+city);
			aggrKey = city;
		}
		if(offerList != null){
			model.addAttribute("offerList", offerList);
			model.addAttribute("page",pagination);
			model.addAttribute("aggrKey",aggrKey);
		}
		model.addAttribute("term",term);
		return "search";
		
	}
	
//	@RequestMapping(value="/search/{term}", method=RequestMethod.GET)
//	public String searchTermAndCity(@PathVariable("term") String term,@RequestParam("city") String city, Model model, Pageable pageable) {
//		Page<OfferDocument> offerList = null;
//		
//		return "search";
//	}
	
	@RequestMapping("/category/{categoryName}")
	public String category(@PathVariable("categoryName") String categoryName, Model model, Pageable pageable){
		FacetedPage<OfferDocument> offerList = elasticRepo.findOfferByCategoryCustom(categoryName, pageable);
		List<MyBucket> cityAggr = elasticService.extractBucketList(offerList, "citys");
		String categoryNamei18 = messageSource.getMessage("category."+categoryName , null, LocaleContextHolder.getLocale());
				
		if(offerList != null){
			model.addAttribute("cityAggr",cityAggr); 
			model.addAttribute("categoryNamei18",categoryNamei18);
			model.addAttribute("categoryName", categoryName);
			model.addAttribute("offerList",offerList);
			model.addAttribute("page",new Pagination<OfferDocument>(offerList,"/category/"+categoryName,""));
		}
		return "category";
	}
	
	@RequestMapping("/category/{categoryName}/{city}")
	public String offersByCategoryAggrCity(@PathVariable("categoryName") String categoryName,@PathVariable("city") String city, Model model, Pageable pageable){
		Page<OfferDocument> offerList = elasticRepo.findOfferByCategoryAndCityCustom(categoryName,city, pageable);
		String categoryNamei18 = messageSource.getMessage("category."+categoryName , null, LocaleContextHolder.getLocale());
		String aggrKey = null;
		if(offerList != null){ 
			aggrKey = categoryName+" "+city;
			model.addAttribute("categoryNamei18",categoryNamei18);
			model.addAttribute("city", city);
			model.addAttribute("offerList",offerList);
			model.addAttribute("page",new Pagination<OfferDocument>(offerList,"/category/"+categoryName,""));
			model.addAttribute("aggrKey",aggrKey);
		}
		return "category";
	}
	
//	@RequestMapping(value="/search", method=RequestMethod.GET)
//	public String search(Model model,@RequestParam(value="term") String term,
//			@RequestParam(value="cityOrZip",required=false) String cityOrZip){
//		
//		List<OfferDocument> offerList = null;
//		if(cityOrZip.isEmpty()){
//			offerList = elasticRepo.findByTitleFuzzyQuery(term);
//		}
//		
//		else {
//			//if (only digits)
//			if (cityOrZip.chars().allMatch(x -> Character.isDigit(x))){
//				offerList = elasticRepo.findByZip(cityOrZip);
//			}
//			else if(cityOrZip.chars().allMatch(x -> Character.isLetter(x))){
//				offerList = elasticRepo.findByCity(cityOrZip);
//			}
//			else{
//				//TODO
//				return "A Entry can only contain digits or Letter ->'yourchrap'";
//			}
//		}
//		model.addAttribute("term",term);
//		if(offerList != null){
//			model.addAttribute("offerList",offerList);
//		}
//		else
//			return "redirect:search?termNotFound";
//
//		return "search";
//	}

//Radiussearch attemt
//	OSMService osmService = new OSMService();
//	OSMPoint osmPoint = osmService.getCoordFromCityOrZip(cityOrZip, "de");
//	if(osmPoint.isPointFound()){
//		offerList=elasticRepo.findByCityOrPostalWithinRadius(cityOrZip, radius);
//	}
	
	
}
