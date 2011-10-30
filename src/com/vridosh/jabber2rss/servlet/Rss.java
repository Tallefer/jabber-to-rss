package com.vridosh.jabber2rss.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.vridosh.jabber2rss.data.Post;
import com.vridosh.jabber2rss.data.User;

public class Rss extends HttpServlet {
	private static final long serialVersionUID = 8900487471335267348L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		String fromPar = req.getParameter("from");
		Objectify ob = ObjectifyService.begin();
		
		Key<User> userKey = User.keyFromId(fromPar);
		User user = ob.find(userKey);
		if (user == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No such user found");
			return;
		}

		Query<Post> filter = ob.query(Post.class).filter("userId", userKey).order("-date");

		resp.setContentType("application/rss+xml");
		resp.setCharacterEncoding("UTF-8");

		try {
			// Create a XMLOutputFactory
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
	
			// Create XMLEventWriter
			XMLEventWriter eventWriter = outputFactory
					.createXMLEventWriter(resp.getOutputStream(), "UTF-8");
	
			// Create a EventFactory
	
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
	
			// Create and write Start Tag
			StartDocument startDocument = eventFactory.createStartDocument("UTF-8");
	
			eventWriter.add(startDocument);
	
			// Create open tag
			eventWriter.add(end);
	
			StartElement rssStart = eventFactory.createStartElement("", "", "rss");
			eventWriter.add(rssStart);
			eventWriter.add(eventFactory.createAttribute("version", "2.0"));
			eventWriter.add(end);
	
			eventWriter.add(eventFactory.createStartElement("", "", "channel"));
			eventWriter.add(end);
	
			// Write the different nodes
			createNode(eventWriter, "title", "Posts from " + user.getJabberId());
			createNode(eventWriter, "description", "These posts were shared by jabber user");
	
			int cnt = 0;
			for (Post p : filter) {
				if (cnt++ > 200) {
					break;
				}
				eventWriter.add(eventFactory.createStartElement("", "", "item"));
				eventWriter.add(end);
				createNode(eventWriter, "title", "Posted " + p.getDate());
				createNode(eventWriter, "description", p.getMessage());
				createNode(eventWriter, "author", user.getJabberId());
				createNode(eventWriter, "guid", "" + p.getId());
				eventWriter.add(end);
				eventWriter.add(eventFactory.createEndElement("", "", "item"));
				eventWriter.add(end);
	
			}
	
			eventWriter.add(end);
			eventWriter.add(eventFactory.createEndElement("", "", "channel"));
			eventWriter.add(end);
			eventWriter.add(eventFactory.createEndElement("", "", "rss"));
	
			eventWriter.add(end);
	
			eventWriter.add(eventFactory.createEndDocument());
	
			eventWriter.close();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}		
	}
	
	private void createNode(XMLEventWriter eventWriter, String name,
			String value) throws XMLStreamException {
				XMLEventFactory eventFactory = XMLEventFactory.newInstance();
				XMLEvent end = eventFactory.createDTD("\n");
				XMLEvent tab = eventFactory.createDTD("\t");
				// Create Start node
				StartElement sElement = eventFactory.createStartElement("", "", name);
				eventWriter.add(tab);
				eventWriter.add(sElement);
				// Create Content
				Characters characters = eventFactory.createCharacters(value);
				eventWriter.add(characters);
				// Create End node
				EndElement eElement = eventFactory.createEndElement("", "", name);
				eventWriter.add(eElement);
				eventWriter.add(end);
			}

}
