package com.vuetify.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class UserPreferences {
	@Id
	@GeneratedValue
	private Long prefId;
	@Column(name="username", nullable=false, unique=true)
	private String username;
	@OneToMany
	@JoinColumn(name="favourite_comic_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Column(name="favourite_comics", nullable=true)
	private List<Comic> comics;
	
	// Constructors 
	public UserPreferences() {}
	public UserPreferences(String username) {
		this.username = username;
	}
	public UserPreferences(String username, List<Comic> comics) {
		this.username = username;
		this.comics = comics;
	}
	
	// Getters and Setters
	public Long getPrefId() {
		return prefId;
	}
	public void setPrefId(Long prefId) {
		this.prefId = prefId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<Comic> getComics() {
		return comics;
	}
	public void setComics(List<Comic> comics) {
		this.comics = comics;
	}
	
	
	
	
}
