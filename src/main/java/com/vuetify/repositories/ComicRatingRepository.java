package com.vuetify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.ComicRating;

@Repository
public interface ComicRatingRepository extends JpaRepository<ComicRating, Long> {
	ComicRating findByUserAndComicId(String user, long comicId);
	ComicRating findByUser(String user);
}
