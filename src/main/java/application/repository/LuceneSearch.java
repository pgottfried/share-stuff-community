package application.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;


import application.entity.Offer;


@Repository
@Transactional
public class LuceneSearch {
	
	// Spring will inject here the entity manager object
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Offer> searchOffers(String searchString){
		
		
		FullTextEntityManager fullTextEntityManager =
			    org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
		
		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity( Offer.class ).get();
		
		try{	
		org.apache.lucene.search.Query query = queryBuilder
			  .keyword()
			  .onFields("title", "shortDescription")
			  .matching(searchString)
			  .createQuery();
		
	
		// wrap Lucene query in a javax.persistence.Query
		org.hibernate.search.jpa.FullTextQuery jpaQuery =
		        fullTextEntityManager.createFullTextQuery(query, Offer.class);
		

		// execute search
		@SuppressWarnings("unchecked")
		List<Offer> offers = (List<Offer>) jpaQuery.getResultList();
		
		return offers;
		
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
public List<Offer> searchOffersByCategory(String category){
		
		
		FullTextEntityManager fullTextEntityManager =
			    org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
		
		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity( Offer.class ).get();
		
		try{	
		org.apache.lucene.search.Query query = queryBuilder
			  .keyword()
			  .onFields("category.name")
			  .matching(category)
			  .createQuery();
		
	
		// wrap Lucene query in a javax.persistence.Query
		org.hibernate.search.jpa.FullTextQuery jpaQuery =
		        fullTextEntityManager.createFullTextQuery(query, Offer.class);
		

		// execute search
		@SuppressWarnings("unchecked")
		List<Offer> offers = (List<Offer>) jpaQuery.getResultList();
		
		return offers;
		
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
}
