package com.vridosh.jabber2rss.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

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
import com.vridosh.jabber2rss.util.JabberUtil;

public class Jabber extends HttpServlet {
	private final Logger log = Logger.getLogger(getClass().getName());
	private static final long serialVersionUID = 5845958945737908910L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        Message message = xmpp.parseMessage(req);

        JID jid = message.getFromJid();
        String jidId = JabberUtil.normalizeJid(jid.getId());

        String body = message.getBody();

        Objectify obj = ObjectifyService.begin();
        Key<User> key = User.keyFromId(jidId);

        User user = obj.find(key);
        if (user == null) {
        	user = new User(jidId, jidId);
        	obj.put(user);
        }
        
        log.severe("Post from " + user.getJabberId());

        Post p = new Post(
        		new Key<User>(User.class, user.getJabberId()), 
        		new Date(), 
        		body);
        obj.put(p);
	}

	
}
