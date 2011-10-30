package com.vridosh.jabber2rss.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.vridosh.jabber2rss.data.Post;
import com.vridosh.jabber2rss.data.User;

public class Jabber extends HttpServlet {
	private static final long serialVersionUID = 5845958945737908910L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        Message message = xmpp.parseMessage(req);

        JID jid = message.getFromJid();
        String jidId = jid.getId();
        String body = message.getBody();

        Objectify obj = ObjectifyService.begin();
        Key<User> key = User.keyFromId(jidId);
        
        User user = obj.find(key);
        if (user == null) {
        	user = new User(jidId, jidId.substring(0, jidId.indexOf("/")));
        	obj.put(user);
        }

        Post p = new Post(
        		new Key<User>(User.class, user.getJabberId()), 
        		new Date(), 
        		body);
        obj.put(p);
	}

	
}
