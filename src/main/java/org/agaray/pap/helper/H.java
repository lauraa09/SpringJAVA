package org.agaray.pap.helper;

import javax.servlet.http.HttpSession;

public class H {
	/**
	 * 
	 * @param s
	 * @param mensaje
	 * @param severity info, warning o danger
	 * @param link
	 */
	public static void info(HttpSession s, String mensaje, String severity, String link) {
		s.setAttribute("mensaje", mensaje);
		s.setAttribute("severity", severity);
		s.setAttribute("link", link);
	}

	public static void info(HttpSession s, String mensaje, String severity) {
		s.setAttribute("mensaje", mensaje);
		s.setAttribute("severity", severity);
		s.setAttribute("link", "/");
	}
	
	public static void info(HttpSession s, String mensaje) {
		s.setAttribute("mensaje", mensaje);
		s.setAttribute("severity", "info");
		s.setAttribute("link", "/");
	}
}
