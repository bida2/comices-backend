package com.vuetify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.ForumThread;

@Repository
public interface ForumThreadRepository extends JpaRepository<ForumThread, Long>{

}
