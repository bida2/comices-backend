package com.vuetify.entities;

import java.net.URL;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ComicSuggestion {
	@Id
	@GeneratedValue
	private Long suggestionId;
	@Column(name="suggested_by_username", nullable=false)
	private String suggesterUsername;
	@Column(name="suggested_comic_name", nullable=false)
	private String suggestedComicName;
	@Column(name="suggested_author_name", nullable=false)
	private String suggestedAuthorName;
	@Column(name="suggested_comic_release_date", nullable=false)
	private LocalDate suggestedReleaseDate;
	@Column(name="price", nullable=false)
	private float price;
	@Column(name="suggested_comic_buy_url", nullable=false)
	private URL buyComicURL;
	@Column(name="suggested_comic_cover_url", nullable=false)
	private URL comicCoverURL;
	@Column(name="suggested_series_url", nullable=false)
	private URL seriesURL;
	
	ComicSuggestion() {}
	public ComicSuggestion(String suggesterUsername, String suggestedComicName,
			String suggestedAuthorName, LocalDate suggestedReleaseDate, URL buyURL, URL coverURL, URL seriesURL, float price) {
		this.suggesterUsername = suggesterUsername;
		this.suggestedComicName = suggestedComicName;
		this.suggestedAuthorName = suggestedAuthorName;
		this.suggestedReleaseDate = suggestedReleaseDate;
		this.buyComicURL = buyURL;
		this.comicCoverURL = coverURL;
		this.seriesURL = seriesURL;
		this.price = price;
	}
	
	
	public Long getSuggestionId() {
		return suggestionId;
	}
	public void setSuggestionId(Long suggestionId) {
		this.suggestionId = suggestionId;
	}
	public String getSuggesterUsername() {
		return suggesterUsername;
	}
	public void setSuggesterUsername(String suggesterUsername) {
		this.suggesterUsername = suggesterUsername;
	}
	public String getSuggestedComicName() {
		return suggestedComicName;
	}
	public void setSuggestedComicName(String suggestedComicName) {
		this.suggestedComicName = suggestedComicName;
	}
	public String getSuggestedAuthorName() {
		return suggestedAuthorName;
	}
	public void setSuggestedAuthorName(String suggestedAuthorName) {
		this.suggestedAuthorName = suggestedAuthorName;
	}
	public LocalDate getSuggestedReleaseDate() {
		return suggestedReleaseDate;
	}
	public void setSuggestedReleaseDate(LocalDate suggestedReleaseDate) {
		this.suggestedReleaseDate = suggestedReleaseDate;
	}
	public URL getBuyComicURL() {
		return buyComicURL;
	}
	public void setBuyComicURL(URL buyComicURL) {
		this.buyComicURL = buyComicURL;
	}
	public URL getComicCoverURL() {
		return comicCoverURL;
	}
	public void setComicCoverURL(URL comicCoverURL) {
		this.comicCoverURL = comicCoverURL;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public URL getSeriesURL() {
		return seriesURL;
	}
	public void setSeriesURL(URL seriesURL) {
		this.seriesURL = seriesURL;
	}
	
	
}
