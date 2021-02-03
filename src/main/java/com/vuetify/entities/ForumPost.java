package com.vuetify.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ForumPost {
	@Id
	@GeneratedValue
	private long postId;
	@Column(name="post_title",nullable=true)
	private String postTitle;
	@Column(name="post_content",nullable=false,columnDefinition="TEXT")
	private String postContent;
	@Column(name="post_date",nullable=false)
	private LocalDateTime postDateTime;
	@Column(name="post_date_formatted",nullable=false)
	private String postDateTimeFormatted;
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="owner_thread_id", nullable=false)
	@JsonIgnore
	private ForumThread ownerThread;
	
	public ForumPost() {}
	
	public ForumPost(String postTitle, String postContent, LocalDateTime postDateTime,String postDateTimeFormat, ForumThread thread) {
		this.postTitle = postTitle;
		this.postContent = postContent;
		this.postDateTime = postDateTime;
		this.ownerThread = thread;
		this.postDateTimeFormatted = postDateTimeFormat;
	}
	
	public ForumPost(String postTitle, String postContent, LocalDateTime postDateTime,String postDateTimeFormat) {
		this.postTitle = postTitle;
		this.postContent = postContent;
		this.postDateTime = postDateTime;
		this.postDateTimeFormatted = postDateTimeFormat;
	}

	public long getPostId() {
		return postId;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getPostContent() {
		return postContent;
	}

	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}

	public LocalDateTime getPostDateTime() {
		return postDateTime;
	}

	public void setPostDateTime(LocalDateTime postDateTime) {
		this.postDateTime = postDateTime;
	}

	public String getPostDateTimeFormatted() {
		return postDateTimeFormatted;
	}

	public void setPostDateTimeFormatted(String postDateTimeFormatted) {
		this.postDateTimeFormatted = postDateTimeFormatted;
	}

	public ForumThread getOwnerThread() {
		return ownerThread;
	}

	public void setOwnerThread(ForumThread ownerThread) {
		this.ownerThread = ownerThread;
	}

	
	
}
