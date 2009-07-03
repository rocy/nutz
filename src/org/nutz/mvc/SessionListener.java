package org.nutz.mvc;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.nutz.mvc.access.Session;

public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		Session.me(se.getSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		if (se.getSession().getAttribute(Session.class.getName()) != null)
			Session.me(se.getSession()).detach();
	}

}