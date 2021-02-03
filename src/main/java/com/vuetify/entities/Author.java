package com.vuetify.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Author {
	@Id
	@GeneratedValue
	private Long authorId;
	@Column(name="author_name",nullable=false)
	private String authorName;
	@Column(name="author_birth_date",nullable=true)
	private LocalDate authorBirthDate;
	@Column(name="author_residence",nullable=true)
	private String authorPlace;
	@Column(name="author_email",nullable=false)
	private String authorEmail;
	//Constructors
	public Author(String authorName, LocalDate authorBirthDate, String authorPlace, String authorEmail) {
		this.authorName = authorName;
		this.authorBirthDate = authorBirthDate;
		this.authorPlace = authorPlace;
		this.authorEmail = authorEmail;
	}
	public Author() {}
	// Getters and Setters
	public Long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public LocalDate getAuthorBirthDate() {
		return authorBirthDate;
	}
	public void setAuthorBirthDate(LocalDate authorBirthDate) {
		this.authorBirthDate = authorBirthDate;
	}
	public String getAuthorPlace() {
		return authorPlace;
	}
	public void setAuthorPlace(String authorPlace) {
		this.authorPlace = authorPlace;
	}
	public String getAuthorEmail() {
		return authorEmail;
	}
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	
}
