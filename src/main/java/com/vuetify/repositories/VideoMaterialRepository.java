package com.vuetify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.VideoMaterial;

@Repository
public interface VideoMaterialRepository extends JpaRepository<VideoMaterial,Long> {
	
}
