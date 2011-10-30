package com.vridosh.jabber2rss.data;
import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;

@Cached
public class Post {
	@Id
	private Long id;
	private Key<User> userId;
	private Date date;
	private String message;

	public Post() {
	}

	public Post(Key<User> userId, Date date, String message) {
		this.setUserId(userId);
		this.setDate(date);
		this.setMessage(message);
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setUserId(Key<User> userId) {
		this.userId = userId;
	}

	public Key<User> getUserId() {
		return userId;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
