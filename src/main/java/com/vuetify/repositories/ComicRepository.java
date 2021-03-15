package com.vuetify.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.Comic;
import com.vuetify.enums.ComicType;

@Repository
public interface ComicRepository extends JpaRepository<Comic,Long> {
	 Optional<List<Comic>> findByStatus(ComicType type);
	 Optional<Comic> findByComicId(Long comicId);
	 Optional<Comic> findByComicName(String comicName);
}
