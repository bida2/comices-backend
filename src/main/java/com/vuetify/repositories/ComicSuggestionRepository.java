package com.vuetify.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.ComicSuggestion;

@Repository
public interface ComicSuggestionRepository extends JpaRepository<ComicSuggestion, Long> {
	List<ComicSuggestion> findBySuggesterUsername(String suggesterUsername);
	ComicSuggestion findBySuggestedComicName(String comicName);
}
