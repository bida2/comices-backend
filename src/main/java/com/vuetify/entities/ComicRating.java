package com.vuetify.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class ComicRating {
	@Id
	@GeneratedValue
	private Long ratingId;
	@Min(1)
	@Max(5)
	@Column(name="rating", nullable=false)
	private int rating;
	@Column(name="rated_by_user", nullable=false)
	private String user;
	@Column(name="rated_comic_id", nullable=false)
	private long comicId;
	
	public ComicRating() {}
	
	public ComicRating(int rating, String user, long comicId) {
		this.rating = rating;
		this.user = user;
		this.comicId = comicId;
	}

	public Long getRatingId() {
		return ratingId;
	}

	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public long getComicId() {
		return comicId;
	}

	public void setComicId(long comicId) {
		this.comicId = comicId;
	}
	
	
}
