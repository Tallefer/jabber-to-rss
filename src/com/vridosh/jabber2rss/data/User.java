package com.vridosh.jabber2rss.data;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;

@Cached
public class User {
	@Id
	private String jabberId;
	private String userName;
	
	public User(String jabberId, String userName) {
		this.setJabberId(jabberId);
		this.setUserName(userName);
	}

	public User() {
		this.setJabberId(null);
		this.setUserName(null);
	}

	public void setJabberId(String jabberId) {
		this.jabberId = jabberId;
	}

	public String getJabberId() {
		return jabberId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public static Key<User> keyFromId(String jidId) {
		return new Key<User>(User.class, jidId);
	}
}
