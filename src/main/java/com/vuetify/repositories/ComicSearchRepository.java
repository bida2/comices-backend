package com.vuetify.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.lucene.search.Query;
import org.hibernate.search.exception.EmptyQueryException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.Comic;

@Repository
@Transactional
public class ComicSearchRepository {

	 @PersistenceContext
	  private EntityManager entityManager;
	  
	  @SuppressWarnings("unchecked")
	public List<Comic> searchByComicName(String text) throws InterruptedException, EmptyQueryException {
		  	if (text == null || text == "")
		  		text = "*";
		  	
		    // get the full text entity manager
		    FullTextEntityManager fullTextEntityManager =
		      org.hibernate.search.jpa.Search.
		      getFullTextEntityManager(entityManager);
		    // Test line - purge and optimize on start - used for reindexing
		    // If NoSuchFileException occurs, MANUALLY delete all the directories related to indexing
		    // Examples are com.springboot.studentservices.entities.Stock and com.springboot.studentservices.entities.Companies
		    //fullTextEntityManager.createIndexer(Stock.class).purgeAllOnStart(true).optimizeAfterPurge(true).optimizeOnFinish(true).start();
		    fullTextEntityManager.createIndexer(Comic.class).startAndWait();
		    // The above line of code should NOT BE usually executed, only in cases when you need to completely reindex
		    // As it messes with indexing otherwise
		    QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Comic.class)
		    	    // Here come the assignments of "query" analyzers
		    	    .overridesForField( "comicName", "customanalyzer" )
		    	    .get();
		    // a very basic query by keywords
		   /* org.apache.lucene.search.Query query =
		      queryBuilder
		      	.bool()
		      	.must(queryBuilder.keyword().onFields("word").matching(text).createQuery())
		      	.createQuery(); */
		    Query fuzzyQuery;
		    List<Comic> results = null;
		    org.hibernate.search.jpa.FullTextQuery jpaQuery;
		    try {
		    	fuzzyQuery = queryBuilder
			    		  .keyword()
			    		  .fuzzy()
			    		  .withEditDistanceUpTo(2)
			    		  .withPrefixLength(0)
			    		  .onFields("comicName")
			    		  .matching(text)
			    		  .createQuery();
		    	 jpaQuery =
		   		      fullTextEntityManager.createFullTextQuery(fuzzyQuery, Comic.class);
		    	 
		    	// execute search and return results (sorted by relevance as default)
				    
				    results = jpaQuery.getResultList();
		    } catch (EmptyQueryException e) {
		    	System.out.println("User search query is empty!");
		    }
		    return results;
		  } // method search

	  
	  
}
