package com.vuetify.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ForumThread {
	@Id
	@GeneratedValue
	private long threadId;
	@Column(name="thread_topic",nullable=false)
	private String threadTopic;
	@Column(name="thread_post_time",nullable=false)
	private LocalDateTime threadDateTime;
	@Column(name="thread_post_time_formatted",nullable=true)
	private String threadDateTimeFormatted;
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, mappedBy="ownerThread")
	private List<ForumPost> threadPosts;
	
	public ForumThread() {}
	
	public ForumThread(String threadTopic, LocalDateTime threadDateTime, String threadDateTimeFormat) {
		this.threadTopic = threadTopic;
		this.threadDateTime = threadDateTime;
		this.threadDateTimeFormatted = threadDateTimeFormat;
	}
	

	public long getThreadId() {
		return threadId;
	}


	public String getThreadTopic() {
		return threadTopic;
	}


	public void setThreadTopic(String threadTopic) {
		this.threadTopic = threadTopic;
	}


	public LocalDateTime getThreadDateTime() {
		return threadDateTime;
	}


	public void setThreadDateTime(LocalDateTime threadDateTime) {
		this.threadDateTime = threadDateTime;
	}

	public String getThreadDateTimeFormatted() {
		return threadDateTimeFormatted;
	}

	public void setThreadDateTimeFormatted(String threadDateTimeFormatted) {
		this.threadDateTimeFormatted = threadDateTimeFormatted;
	}

	public List<ForumPost> getThreadPosts() {
		return threadPosts;
	}

	public void setThreadPosts(List<ForumPost> threadPosts) {
		this.threadPosts = threadPosts;
	}
	
	
	
}
