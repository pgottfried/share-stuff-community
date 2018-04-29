package application.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import application.document.OfferDocument;

public interface OfferDocumentRepository extends ElasticsearchRepository<OfferDocument,Long>, OfferDocumentRepositoryCustom {
	
	public List<OfferDocument> findByCategory(String category);
	public Page<OfferDocument> findByCategory(String category, Pageable pageable);
	
	//public List<OfferDocument> findByTitle(String searchString);
	public Page<OfferDocument> findByCity(String city, Pageable pageable);
	public List<OfferDocument> findByZip(String zip);
	@Query("{\"fuzzy\":{\"title\":\"?0\"}}}")
	public List<OfferDocument> findByTitleFuzzyQuery(String title);
	
	@Query("{\"filtered\":{\"filter\":{\"geo_distance\":{\"distance\": \"?1\",\"geoPoint\":{\"?0\"}}}}")
	public List<OfferDocument> findByCityOrPostalWithinRadius(String cityOrPostal, String radius);
	
	
	public FacetedPage<OfferDocument> findByTitle(String title,Pageable pageable);

}
