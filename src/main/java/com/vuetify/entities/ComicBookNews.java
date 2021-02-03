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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class ComicBookNews {
	@Id
	@GeneratedValue
	private Long newsId;
	@NotNull
	@Column(name="news_article_title", nullable=false)
	private String newsArticleTitle;
	@NotNull
	@Column(name="news_article_content", nullable=false,columnDefinition="TEXT")
	private String newsArticleContent;
	@Column(name="news_article_posted_date", nullable=true)
	private LocalDateTime newsArticlePostedDate;
	@Column(name="news_article_formatted_date", nullable=true)
	private String newsArticleFormattedDate;
	@Min(0)
	@Column(name="thumbs_up", nullable=true)
	private int thumbsUp;
	@Min(0)
	@Column(name="thumbs_down", nullable=true)
	private int thumbsDown;
	// Contains all the usernames of the users that have rated an announcement
	// And their latest rating type choice for the announcement (can be "Up" or "Down")
	@ElementCollection
	@Column(name="rated_by_users_list", nullable=true)
	private Map<String,String> ratedByUsers = new HashMap<String,String>();
	@Column(name="video_material", nullable=true)
	private URL videoMaterialURL;
	
	public ComicBookNews() {}
	
	public ComicBookNews(String title, String content, LocalDateTime postDate, URL video) {
		this.newsArticleTitle = title;
		this.newsArticleContent = content;
		this.newsArticlePostedDate = postDate;
		this.newsArticleFormattedDate = postDate.format(DateTimeFormatter.ofPattern("MMM. dd, uuuu 'at' HH:mm:ss a"));
		this.videoMaterialURL = video;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public String getNewsArticleTitle() {
		return newsArticleTitle;
	}

	public void setNewsArticleTitle(String newsArticleTitle) {
		this.newsArticleTitle = newsArticleTitle;
	}

	public String getNewsArticleContent() {
		return newsArticleContent;
	}

	public void setNewsArticleContent(String newsArticleContent) {
		this.newsArticleContent = newsArticleContent;
	}

	public LocalDateTime getNewsArticlePostedDate() {
		return newsArticlePostedDate;
	}

	public void setNewsArticlePostedDate(LocalDateTime newsArticlePostedDate) {
		this.newsArticlePostedDate = newsArticlePostedDate;
	}

	public String getNewsArticleFormattedDate() {
		return newsArticleFormattedDate;
	}

	public void setNewsArticleFormattedDate(String newsArticleFormattedDate) {
		this.newsArticleFormattedDate = newsArticleFormattedDate;
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

	public URL getVideoMaterialURL() {
		return videoMaterialURL;
	}

	public void setVideoMaterialURL(URL videoMaterialURL) {
		this.videoMaterialURL = videoMaterialURL;
	}

	
	
	
}
