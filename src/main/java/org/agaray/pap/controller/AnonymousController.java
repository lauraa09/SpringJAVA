package org.agaray.pap.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Persona;
import org.agaray.pap.helper.H;
import org.agaray.pap.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnonymousController {

	@Autowired
	PersonaRepository repoPersona;

	@GetMapping("/info")
	public String info(HttpSession s, ModelMap m) {
		String mensaje = s.getAttribute("mensaje") != null ? (String) s.getAttribute("mensaje")
				: "Pulsa para volver a home";
		String severity = s.getAttribute("severity") != null ? (String) s.getAttribute("severity") : "info";
		String link = s.getAttribute("link") != null ? (String) s.getAttribute("link") : "/";

		s.removeAttribute("mensaje");
		s.removeAttribute("severity");
		s.removeAttribute("link");

		m.put("mensaje", mensaje);
		m.put("severity", severity);
		m.put("link", link);

		m.put("view", "/_t/info");
		return "/_t/frame";
	}

	@GetMapping("/")
	public String home(ModelMap m) {
		m.put("view", "/anonymous/home");
		return "/_t/frame";
	}

	@GetMapping("/registro")
	public String registro(ModelMap m) {
		return "redirect:/persona/c";
	}

	@GetMapping("/login")
	public String login(ModelMap m) {
		m.put("view", "/anonymous/login");
		return "/_t/frame";
	}

	@PostMapping("/login")
	public String login(@RequestParam("loginname") String loginname, @RequestParam("password") String password,
			ModelMap m, HttpSession s) {
		String view = "/";
		try {
			Persona persona = repoPersona.getByLoginname(loginname);
			if (!(new BCryptPasswordEncoder()).matches(password, persona.getPassword())) {
				throw new Exception();
			}
			s.setAttribute("persona", persona);
		} catch (Exception e) {
			H.info(s, "Usuario o contraseña incorrecta", "danger", "/login");
			view = "/info";
		}

		return "redirect:" + view;
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession s) {
		s.invalidate();
		return "redirect:/";
	}
}