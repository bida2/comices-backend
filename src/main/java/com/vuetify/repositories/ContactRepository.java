package com.vuetify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vuetify.entities.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {

}
