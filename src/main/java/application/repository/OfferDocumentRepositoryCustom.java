package application.repository;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import application.document.OfferDocument;


public interface OfferDocumentRepositoryCustom {

	public SearchResponse findOfferByTitleCustom(String title, Pageable pageable);
	public FacetedPage<OfferDocument> findOfferByTitleCustom2(String title, Pageable pageable);
	public FacetedPage<OfferDocument> findOfferByCategoryCustom(String category, Pageable pageable);
	public Page<OfferDocument> findOfferByCategoryAndCityCustom(String category, String city, Pageable pageable);
	public Page<OfferDocument> findOfferByTitleAndCategoryCustom(String title, String category,Pageable pageable);
	public Page<OfferDocument> findOfferByTitleAndCityCustom(String title, String city, Pageable pageable);
	public Page<OfferDocument> findOfferByTitleAndZipCustom(String title, String zip, Pageable pageable);
	public Page<OfferDocument> findOfferByUserId(Long userId, Pageable pageable);
	public Page<OfferDocument> findByGeoPointWithinRadius(GeoPoint geoPoint, String radiusInKM);
}
