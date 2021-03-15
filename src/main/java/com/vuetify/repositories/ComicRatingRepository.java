package com.vuetify.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.ComicRating;

@Repository
public interface ComicRatingRepository extends JpaRepository<ComicRating, Long> {
	Optional<ComicRating> findByUserAndComicId(String user, long comicId);
	Optional<ComicRating> findByUser(String user);
}
