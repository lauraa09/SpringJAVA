package org.agaray.pap.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Aficion;
import org.agaray.pap.helper.H;
import org.agaray.pap.repository.AficionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/aficion")
public class AficionController {

	@Autowired
	private AficionRepository repoAficion;

	@PostMapping("d")
	public String borrarPost(@RequestParam("id") Long idAficion, HttpSession s) {
		String nombreAficion = "----";
		try {
			Aficion aficion=repoAficion.getOne(idAficion);
			nombreAficion= aficion.getNombre();
			repoAficion.delete(aficion);
			H.info(s, "Afición " + nombreAficion + " borrada correctamente", "info", "/aficion/r");
		} catch (Exception e) {
			H.info(s, "Error al borrar la afición " + nombreAficion , "danger", "/aficion/c");
		}
		return "redirect:/info";
	}

	@GetMapping("r")
	public String read(ModelMap m) {
		List<Aficion> aficiones = repoAficion.findAll();
		m.put("aficiones", aficiones);
	
		m.put("view","/aficion/aficionR");
		return "/_t/frame";
	}

	@GetMapping("c")
	public String crearGet(ModelMap m) {
		m.put("view","/aficion/aficionC");
		return "/_t/frame";
	}

	@PostMapping("c")
	public String crearPost(@RequestParam("nombre") String nombreAficion, HttpSession s) {
		try {
			repoAficion.save(new Aficion(nombreAficion));
			H.info(s, "Afición " + nombreAficion + " creada correctamente", "info", "/aficion/c");
		} catch (Exception e) {
			H.info(s, "Afición " + nombreAficion + " duplicada", "danger", "/aficion/c");
		}
		return "redirect:/info";
	}
}
