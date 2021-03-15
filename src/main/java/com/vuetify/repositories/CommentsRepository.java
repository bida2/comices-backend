package com.vuetify.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.Comic;
import com.vuetify.entities.ComicComments;

@Repository
public interface CommentsRepository extends JpaRepository<ComicComments,Long> {
	Optional<List<ComicComments>> findAllByCommentedComic(Comic commentedComic);
}
