package application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.facet.result.Term;
import org.springframework.data.elasticsearch.core.facet.result.TermResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import application.document.OfferDocument;
import application.helper.MyBucket;
import application.helper.MyObjectMapper;

@Service
public class ElasticsearchService {
	
	@Autowired private MessageSource messageSource;
	private MyObjectMapper mapper = new MyObjectMapper();
	
	public List<OfferDocument> extractOfferList(SearchResponse response){
		final ObjectMapper mapper = new ObjectMapper();
		List<OfferDocument> offerList = new ArrayList<OfferDocument>();
		if(response.getHits().getHits() != null){
			for(SearchHit hit : response.getHits().getHits()){
				Map<String, Object> source = hit.getSource();
				OfferDocument offer = mapper.convertValue(source, OfferDocument.class);
				offerList.add(offer);
			}
		}
		return offerList;
	}
	
	public List<MyBucket> extractBucketList(SearchResponse response, String aggrName){
		List<MyBucket> bucketList = null;
		Map<String,Aggregation> aggrMap = response.getAggregations().asMap();
		StringTerms aggr = (StringTerms) aggrMap.get(aggrName);
		if(aggr != null){
			 bucketList = aggr.getBuckets().stream()
										    .map(mapper.mapBucketToMyBucket)
											.collect(Collectors.<MyBucket> toList());
		}
		return bucketList;
		
	}
	
	public List<MyBucket> extractBucketList(FacetedPage<OfferDocument> response, String facetName){
		
		TermResult facet = (TermResult) response.getFacet(facetName);
		List<Term> terms = facet.getTerms();
		List<MyBucket> buckets = terms.stream()
				.map(mapper.mapTermToMyBucket)
				.collect(Collectors.<MyBucket> toList());
		
		//translate from english to german using messageproperties
		if(facetName == "categorys"){
			buckets.forEach(bucket -> {bucket.setKeyi18(messageSource.getMessage("category."+bucket.getKey() , null, LocaleContextHolder.getLocale()));});
			return buckets;
		}
		
		//capitalize first letter of key 
		buckets.forEach(bucket -> {bucket.setKey(bucket.getKey().substring(0, 1).toUpperCase() + bucket.getKey().substring(1) );});
		
		return buckets;
	}
		
	
}
