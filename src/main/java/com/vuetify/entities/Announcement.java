package com.vuetify.entities;

import java.time.LocalDateTime;
import java.util.HashMap;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Announcement {
	@Id
	@GeneratedValue
	private long annId;
	@Column(name="announcement_title", nullable=false)
	private String annTitle;
	@Column(name="announcement_content", nullable=false, columnDefinition="TEXT")
	private String annContent;
	@Column(name="announcement_post_date", nullable=true)
	private LocalDateTime annPostDate;
	@Column(name="announcement_formatted_date", nullable=true)
	private String annFormattedDate;
	@Column(name="thumbs_up", nullable=true)
	private int thumbsUp;
	@Column(name="thumbs_down", nullable=true)
	private int thumbsDown;
	// Contains all the usernames of the users that have rated an announcement
	// And their latest rating type choice for the announcement (can be "Up" or "Down")
	@ElementCollection
	@Column(name="rated_by_users_list", nullable=true)
	private Map<String,String> ratedByUsers = new HashMap<String,String>();
	
	public Announcement() {}

	public Announcement(String annTitle, String annContent, LocalDateTime annPostDate, String annFormattedDate) {
		this.annTitle = annTitle;
		this.annContent = annContent;
		this.annPostDate = annPostDate;
		this.annFormattedDate = annFormattedDate;
	}

	public long getAnnId() {
		return annId;
	}

	public void setAnnId(long annId) {
		this.annId = annId;
	}

	public String getAnnTitle() {
		return annTitle;
	}

	public void setAnnTitle(String annTitle) {
		this.annTitle = annTitle;
	}

	public String getAnnContent() {
		return annContent;
	}

	public void setAnnContent(String annContent) {
		this.annContent = annContent;
	}

	public LocalDateTime getAnnPostDate() {
		return annPostDate;
	}

	public void setAnnPostDate(LocalDateTime annPostDate) {
		this.annPostDate = annPostDate;
	}

	public String getAnnFormattedDate() {
		return annFormattedDate;
	}

	public void setAnnFormattedDate(String annFormattedDate) {
		this.annFormattedDate = annFormattedDate;
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

	@Override
	public String toString() {
		return "Announcement [annId=" + annId + ", annTitle=" + annTitle + ", annContent=" + annContent
				+ ", annPostDate=" + annPostDate + ", annFormattedDate=" + annFormattedDate + ", thumbsUp=" + thumbsUp
				+ ", thumbsDown=" + thumbsDown + ", ratedByUsers=" + ratedByUsers + "]";
	}

	

	
	
	
}
