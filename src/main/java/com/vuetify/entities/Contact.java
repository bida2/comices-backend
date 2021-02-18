package com.vuetify.entities;

import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Contact {
	@Id
	@GeneratedValue
	private Long contactId;
	@Column(name="contact_topic", nullable=false, columnDefinition="TEXT")
	private String contactTopic;
	@Column(name="contact_email", nullable=false)
	private String contactEmail;
	@Column(name="contact_content",nullable=false,columnDefinition="TEXT")
	private String contactContent;
	@Column(name="image_contact", nullable=true, columnDefinition="TEXT")
	private URL imageURL;
	@Column(name="orig_url", nullable=true, columnDefinition="TEXT")
	private URL origURL;
	
	// Constructors
	public Contact() {}

	public Contact(String contactTopic, String contactEmail, String contactContent) {
		this.contactTopic = contactTopic;
		this.contactEmail = contactEmail;
		this.contactContent = contactContent;
	}
	
	public Contact(String contactTopic, String contactEmail, String contactContent, URL image, URL origURL) {
		this.contactTopic = contactTopic;
		this.contactEmail = contactEmail;
		this.contactContent = contactContent;
		this.imageURL = image;
		this.origURL = origURL;
	}

	
	// Getters and Setters
	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getContactTopic() {
		return contactTopic;
	}

	public void setContactTopic(String contactTopic) {
		this.contactTopic = contactTopic;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactContent() {
		return contactContent;
	}

	public void setContactContent(String contactContent) {
		this.contactContent = contactContent;
	}

	public URL getImageURL() {
		return imageURL;
	}

	public void setImageURL(URL imageURL) {
		this.imageURL = imageURL;
	}
	
	public URL getOrigURL() {
		return origURL;
	}

	public void setOrigURL(URL origURL) {
		this.origURL = origURL;
	}
	
	
	
}
