package com.wh.integration.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.CompareToBuilder;



/**
 * Represents some common Twitter related fields.
 *
 * @author Jose TAboada
 * @since  1.0
 *
 */
@Entity
public class TwitterMessage implements Comparable<TwitterMessage>{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long identifier;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;


	@Temporal(TemporalType.TIMESTAMP)
	private Date sentAt;

	
	@Column(length=200)
	private String text;
	@Column(length=200)
	private String fromUser;
	
	@Column(length=200)
	private String profileImageUrl;
	
	private Long id;
	
	
	
	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public Date getCreatedAt() {
		return createdAt;
	}




	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}




	public Long getIdentifier() {
		return identifier;
	}




	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}




	public String getText() {
		return text;
	}




	public void setText(String text) {
		this.text = text;
	}




	public String getFromUser() {
		return fromUser;
	}




	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}




	public String getProfileImageUrl() {
		return profileImageUrl;
	}




	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}




	public Date getSentAt() {
		return sentAt;
	}




	public void setSentAt(Date sentAt) {
		this.sentAt = sentAt;
	}




	@Override
	public int compareTo(TwitterMessage other) {
		return new CompareToBuilder().append(id, other.id).toComparison();
	}
}

