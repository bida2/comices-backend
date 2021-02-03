package com.vuetify.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class ComicComments {
	@Id
	@GeneratedValue
	private Long commentId;
	@Column(name="commenter_name",nullable=false)
	private String commenterName;
	@Column(name="comment_content",nullable=false,columnDefinition="TEXT")
	private String commentContent;
	@Column(name="comment_date_time",nullable=true)
	private LocalDateTime commentDateTime;
	@Column(name="formatted_comment_date", nullable=true)
	private String formattedCommentDate;
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Comic commentedComic;
	
	
	// Constructors 
	public ComicComments() {}
	
	public ComicComments(String commenterName, String commentContent, LocalDateTime commentDate, Comic commentedComic) {
		this.commenterName = commenterName;
		this.commentContent = commentContent;
		this.commentDateTime = commentDate;
		this.commentedComic = commentedComic;
		this.formattedCommentDate = commentDate.format(DateTimeFormatter.ofPattern("MMM. dd, uuuu 'at' HH:mm:ss a"));
	}
	
	// Getters and Setters

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getCommenterName() {
		return commenterName;
	}

	public void setCommenterName(String commenterName) {
		this.commenterName = commenterName;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public LocalDateTime getCommentDateTime() {
		return commentDateTime;
	}

	public void setCommentDateTime(LocalDateTime commentDateT) {
		this.commentDateTime = commentDateT;
	}

	public Comic getCommentedComic() {
		return commentedComic;
	}

	public void setCommentedComic(Comic commentedComic) {
		this.commentedComic = commentedComic;
	}

	public String getFormattedCommentDate() {
		return formattedCommentDate;
	}

	public void setFormattedCommentDate(String formattedCommentDate) {
		this.formattedCommentDate = formattedCommentDate;
	}
	
	
	
	
}
