package com.vuetify.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.ComicReview;

@Repository
public interface ReviewRepository extends JpaRepository<ComicReview,Long> {
	Optional<ComicReview> findByReviewId(Long reviewId);
}
