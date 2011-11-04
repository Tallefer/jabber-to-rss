package com.vridosh.jabber2rss.util;

public class JabberUtil {
	public static String normalizeJid(String jid) {
		String jidId = jid;      
        int i = jidId.indexOf("/");
        if (i > 0) {
        	jidId = jidId.substring(0, i);
        }
		return jidId;
	}

}
