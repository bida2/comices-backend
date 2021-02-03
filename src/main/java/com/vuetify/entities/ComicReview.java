package com.vuetify.entities;


import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class ComicReview {
	@Id
	@GeneratedValue
	private Long reviewId;
	@Column(name="review_title",nullable=false)
	private String reviewTitle;
	@Column(name="review_content",nullable=false, columnDefinition="TEXT")
	private String reviewContent;
	@Column(name="review_post_date",nullable=true)
	private LocalDateTime reviewDateTime;
	@Column(name="review_post_date_formatted", nullable=true)
	private String reviewDateTimeFormatted;
	@Column(name="video_material", nullable=true)
	private URL videoURL;
	@Min(1)
	@Max(5)
	@Column(name="reviewer_score", nullable=false)
	private short revScore;
	@Min(0)
	@Column(name="thumbs_up", nullable=true)
	private int thumbsUp;
	@Min(0)
	@Column(name="thumbs_down", nullable=true)
	private int thumbsDown;
	@ElementCollection
	@Column(name="rated_by_users_list", nullable=true)
	private Map<String,String> ratedByUsers = new HashMap<String,String>();
	
	// Constructors
	public ComicReview() {}
	
	public ComicReview(String reviewTitle, String reviewContent, LocalDateTime reviewDate, short score) {
		this.reviewTitle = reviewTitle;
		this.reviewContent = reviewContent;
		this.reviewDateTime = reviewDate;
		this.reviewDateTimeFormatted = reviewDate.format(DateTimeFormatter.ofPattern("MMM. dd, uuuu 'at' HH:mm:ss a"));
		this.revScore = score;
	}
	
	public ComicReview(String reviewTitle, String reviewContent, LocalDateTime reviewDate,short score, URL videoMaterial) {
		this.reviewTitle = reviewTitle;
		this.reviewContent = reviewContent;
		this.reviewDateTime = reviewDate;
		this.reviewDateTimeFormatted = reviewDate.format(DateTimeFormatter.ofPattern("MMM. dd, uuuu 'at' HH:mm:ss a"));
		this.videoURL = videoMaterial;
		this.revScore = score;
	}

	// Getters and Setters
	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}

	public String getReviewContent() {
		return reviewContent;
	}

	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}

	public LocalDateTime getReviewDateTime() {
		return reviewDateTime;
	}

	public void setReviewDateTime(LocalDateTime reviewDateTime) {
		this.reviewDateTime = reviewDateTime;
	}

	public String getReviewDateTimeFormatted() {
		return reviewDateTimeFormatted;
	}

	public void setReviewDateTimeFormatted(String reviewDateTimeFormatted) {
		this.reviewDateTimeFormatted = reviewDateTimeFormatted;
	}

	public int getThumbsUp() {
		return thumbsUp;
	}

	public void setThumbsUp(int thumbsUp) {
		this.thumbsUp = thumbsUp;
	}

	public int getThumbsDown() {
		return thumbsDown;
	}

	public void setThumbsDown(int thumbsDown) {
		this.thumbsDown = thumbsDown;
	}

	public Map<String, String> getRatedByUsers() {
		return ratedByUsers;
	}

	public void setRatedByUsers(Map<String, String> ratedByUsers) {
		this.ratedByUsers = ratedByUsers;
	}

	public URL getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(URL videoURL) {
		this.videoURL = videoURL;
	}

	public short getRevScore() {
		return revScore;
	}

	public void setRevScore(short revScore) {
		this.revScore = revScore;
	}

	
	
	
	
}
