package com.vridosh.jabber2rss.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;
import com.vridosh.jabber2rss.data.Post;
import com.vridosh.jabber2rss.data.User;

public class ServletListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// App Engine does not currently invoke this method.
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// This will be invoked as part of a warmup request, or the first user
		// request if no warmup request was invoked.
		ObjectifyService.register(User.class);
		ObjectifyService.register(Post.class);
	}

}
