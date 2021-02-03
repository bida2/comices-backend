package com.vuetify.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.Comic;

@Repository
public interface ComicRepository extends JpaRepository<Comic,Long> {
	// Can be one of three strings total -> 'upcoming' , 'released' and 'classic'
	 List<Comic> findByStatus(String status);
	 Comic findByComicId(Long comicId);
	 Comic findByComicName(String comicName);
}
