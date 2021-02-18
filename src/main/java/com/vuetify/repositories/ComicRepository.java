package com.vuetify.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.Comic;
import com.vuetify.enums.ComicType;

@Repository
public interface ComicRepository extends JpaRepository<Comic,Long> {
	// Can be one of three strings total -> 'upcoming' , 'released' and 'classic'
	 List<Comic> findByStatus(ComicType type);
	 Comic findByComicId(Long comicId);
	 Comic findByComicName(String comicName);
}
