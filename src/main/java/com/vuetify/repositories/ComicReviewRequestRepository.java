package com.vuetify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.ComicReviewRequest;

@Repository
public interface ComicReviewRequestRepository extends JpaRepository<ComicReviewRequest,Long> {
	
}
