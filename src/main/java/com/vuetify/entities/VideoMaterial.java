package com.vuetify.entities;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class VideoMaterial {
	@Id
	@GeneratedValue
	private Long vMaterialId;
	@Column(name="video_embed_url", nullable=false)
	private URL embedUrl;
	@Column(name="subtitle_header", nullable=false)
	private String subtitleHeader;
	@Column(name="time_posted", nullable=false)
	private LocalDateTime timePosted;
	@Column(name="time_posted_formatted", nullable=true)
	private String timePostedFormatted;
	@Column(name="short_description", nullable=true, columnDefinition="TEXT")
	private String description;
	@Column(name="thumbnail", nullable=true)
	private URL thumbnail;
	
	public VideoMaterial() {}
	
	public VideoMaterial(URL embedUrl, String subtitle_header,String desc, LocalDateTime timePosted) {
		this.embedUrl = embedUrl;
		this.subtitleHeader = subtitle_header;
		this.timePosted = timePosted;
		this.description = desc;
		this.timePostedFormatted = timePosted.format(DateTimeFormatter.ofPattern("MMM. dd, uuuu 'at' HH:mm:ss a"));
	}
	
	public VideoMaterial(URL embedUrl, URL thumb, String subtitle_header,String desc, LocalDateTime timePosted) {
		this.embedUrl = embedUrl;
		this.subtitleHeader = subtitle_header;
		this.timePosted = timePosted;
		this.description = desc;
		this.thumbnail = thumb;
		this.timePostedFormatted = timePosted.format(DateTimeFormatter.ofPattern("MMM. dd, uuuu 'at' HH:mm:ss a"));
	}



	public Long getvMaterialId() {
		return vMaterialId;
	}

	public void setvMaterialId(Long vMaterialId) {
		this.vMaterialId = vMaterialId;
	}

	public URL getEmbedUrl() {
		return embedUrl;
	}

	public void setEmbedUrl(URL embedUrl) {
		this.embedUrl = embedUrl;
	}



	public LocalDateTime getTimePosted() {
		return timePosted;
	}

	public void setTimePosted(LocalDateTime timePosted) {
		this.timePosted = timePosted;
	}

	public String getTimePostedFormatted() {
		return timePostedFormatted;
	}

	public void setTimePostedFormatted(String timePostedFormatted) {
		this.timePostedFormatted = timePostedFormatted;
	}


	public String getSubtitleHeader() {
		return subtitleHeader;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public void setSubtitleHeader(String subtitleHeader) {
		this.subtitleHeader = subtitleHeader;
	}


	public URL getThumbnail() {
		return thumbnail;
	}


	public void setThumbnail(URL thumbnail) {
		this.thumbnail = thumbnail;
	}
	
}
