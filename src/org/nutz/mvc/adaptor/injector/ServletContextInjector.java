package org.nutz.mvc.adaptor.injector;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.adaptor.ParamInjector;

public class ServletContextInjector implements ParamInjector {

	public Object get(HttpServletRequest request, HttpServletResponse response,
			Object refer) {
		return request.getSession().getServletContext();
	}

}