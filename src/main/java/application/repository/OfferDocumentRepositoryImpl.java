package application.repository;



import javax.inject.Inject;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.facet.FacetRequest;
import org.springframework.data.elasticsearch.core.facet.request.TermFacetRequestBuilder;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import application.document.OfferDocument;



public class OfferDocumentRepositoryImpl implements OfferDocumentRepositoryCustom {
	
	@Inject
	OfferDocumentRepository elasticRepo;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	
	
	@Override
	public SearchResponse findOfferByTitleCustom(String title, Pageable pageable) {
		
		SearchResponse searchResponse = null;
		
		TermQueryBuilder termQuery = QueryBuilders.termQuery("title",title);
		TermsBuilder categorys = AggregationBuilders.terms("categorys").field("category");
		TermsBuilder citys = AggregationBuilders.terms("citys").field("city");

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(termQuery)
				.addAggregation(categorys)
				.addAggregation(citys)
				.withPageable(pageable)
				.build();
		
		searchResponse = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<SearchResponse>() {
            @Override
            public SearchResponse extract(SearchResponse response) {
                return response;
            }
        });
		
		return searchResponse;

	}



	
	@Override
	public FacetedPage<OfferDocument> findOfferByTitleCustom2(String title,Pageable pageable) {
		
		FacetedPage<OfferDocument> offerList = null;
		
		TermQueryBuilder termQuery = QueryBuilders.termQuery("title",title);
		FacetRequest categorys = new TermFacetRequestBuilder("categorys").fields("category").build();
		FacetRequest citys = new TermFacetRequestBuilder("citys").fields("city").build();
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(termQuery)
				.withFacet(categorys)
				.withFacet(citys)
				.withPageable(pageable)
				.build();
		
	
		offerList = elasticRepo.search(searchQuery);
		
		
		return offerList;
	}




	@Override
	public FacetedPage<OfferDocument> findOfferByCategoryCustom(String category, Pageable pageable) {
		
		FacetedPage<OfferDocument> offerList = null;
		
		TermQueryBuilder termQuery = QueryBuilders.termQuery("category",category);
		//TermFilterBuilder termFilter = FilterBuilders.termFilter("category", category);
		FacetRequest citys = new TermFacetRequestBuilder("citys").fields("city").build();
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(termQuery)
				.withFacet(citys)
				.withPageable(pageable)
				.build();
		
		offerList = elasticRepo.search(searchQuery);
		return offerList;
	}




	@Override
	public Page<OfferDocument> findOfferByCategoryAndCityCustom(String category, String city, Pageable pageable) {
		Page<OfferDocument> offerList = null;
		city = city.toLowerCase();
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchAllQuery())
				.withFilter(FilterBuilders.boolFilter().
						must(FilterBuilders.termFilter("category", category),FilterBuilders.termFilter("city", city)))
				.withPageable(pageable)
				.build();
		
		offerList = elasticRepo.search(searchQuery);
		return offerList;
	}




	@Override
	public Page<OfferDocument> findOfferByTitleAndCategoryCustom(String title, String category, Pageable pageable) {
		Page<OfferDocument> offerList = null;
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchAllQuery())
				.withFilter(FilterBuilders.boolFilter()
						.must(FilterBuilders.termFilter("title", title),FilterBuilders.termFilter("category", category)))
				.withPageable(pageable)
				.build();

		offerList = elasticRepo.search(searchQuery);
		return offerList;
	}




	@Override
	public Page<OfferDocument> findOfferByTitleAndCityCustom(String title, String city, Pageable pageable) {
		Page<OfferDocument> offerList = null;
		city = city.toLowerCase();
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchAllQuery())
				.withFilter(FilterBuilders.boolFilter()
						.must(FilterBuilders.termFilter("title", title),FilterBuilders.termFilter("city", city)))
				.withPageable(pageable)
				.build();

		offerList = elasticRepo.search(searchQuery);
		return offerList;
	}




	@Override
	public Page<OfferDocument> findOfferByUserId(Long userId, Pageable pageable) {
		Page<OfferDocument> offerList = null;
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchAllQuery())
				.withFilter(FilterBuilders.termFilter("user_id", userId))
				.withPageable(pageable)
				.build();

		offerList = elasticRepo.search(searchQuery);
		return offerList;
	}




	@Override
	public Page<OfferDocument> findOfferByTitleAndZipCustom(String title, String zip, Pageable pageable) {
		Page<OfferDocument> offerList = null;
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchAllQuery())
				.withFilter(FilterBuilders.boolFilter()
						.must(FilterBuilders.termFilter("title", title),FilterBuilders.termFilter("zip", zip)))
				.withPageable(pageable)
				.build();

		offerList = elasticRepo.search(searchQuery);
		return offerList;
	}




	@Override
	public Page<OfferDocument> findByGeoPointWithinRadius(GeoPoint geoPoint, String radiusInKM) {
		Page<OfferDocument> offerList = null;
		
		
			// radius miles or km depending on country not implemented yet
			radiusInKM = radiusInKM + "km";
			CriteriaQuery geoLocationCriteriaQuery = new CriteriaQuery(
                new Criteria("geoPoint").within(geoPoint, radiusInKM));
			offerList = elasticsearchTemplate.queryForPage(geoLocationCriteriaQuery, OfferDocument.class);
			return offerList;
		
	}
	
	
	
}
