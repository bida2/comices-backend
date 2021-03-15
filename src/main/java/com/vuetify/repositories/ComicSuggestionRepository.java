package com.vuetify.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.ComicSuggestion;

@Repository
public interface ComicSuggestionRepository extends JpaRepository<ComicSuggestion, Long> {
	Optional<List<ComicSuggestion>> findBySuggesterUsername(String suggesterUsername);
	Optional<ComicSuggestion> findBySuggestedComicName(String comicName);
}
