package com.vuetify.entities;

import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ComicReviewRequest {
	@Id
	@GeneratedValue
	private long requestId;
	@Column(name="request_comic_name", nullable=false)
	private String requestComicName;
	@Column(name="request_comic_summary_url", nullable=false)
	private URL requestComicURL;
	@Column(name="request_comic_cover_url", nullable=true)
	private URL requestComicCoverURL;
	
	
	public ComicReviewRequest() {}
	
	public ComicReviewRequest(String requestComicName, URL requestComicURL, URL comicCoverURL) {
		this.requestComicName = requestComicName;
		this.requestComicURL = requestComicURL;
		this.requestComicCoverURL = comicCoverURL;
	}
	
	public long getRequestId() {
		return requestId;
	}
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	public String getRequestComicName() {
		return requestComicName;
	}
	public void setRequestComicName(String requestComicName) {
		this.requestComicName = requestComicName;
	}

	public URL getRequestComicURL() {
		return requestComicURL;
	}

	public void setRequestComicURL(URL requestComicURL) {
		this.requestComicURL = requestComicURL;
	}

	public URL getRequestComicCoverURL() {
		return requestComicCoverURL;
	}

	public void setRequestComicCoverURL(URL requestComicCoverURL) {
		this.requestComicCoverURL = requestComicCoverURL;
	}
	
	
}
