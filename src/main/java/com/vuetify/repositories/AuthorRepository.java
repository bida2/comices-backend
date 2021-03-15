package com.vuetify.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {
	
}
