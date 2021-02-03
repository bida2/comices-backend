package com.vuetify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.ForumPost;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost,Long>{
}
