package org.agaray.pap.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Pais;
import org.agaray.pap.helper.H;
import org.agaray.pap.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/pais")
public class PaisController {

	@Autowired
	private PaisRepository repoPais;

	@GetMapping("r")
	public String read(ModelMap m) {
		List<Pais> paises = repoPais.findAll();
		m.put("paises", paises);

		m.put("view", "/pais/paisR");
		return "/_t/frame";
	}

	@GetMapping("c")
	public String crearGet(ModelMap m, HttpSession s) {
		m.put("view", "/pais/paisC");
		return "/_t/frame";
	}

	@PostMapping("c")
	public String crearPost(@RequestParam("nombre") String nombrePais, HttpSession s) {
		try {
			repoPais.save(new Pais(nombrePais));
			H.info(s, "País " + nombrePais + " creado correctamente", "info", "/pais/c");
		} catch (Exception e) {
			H.info(s, "País " + nombrePais + " duplicado", "danger", "/pais/c");
		}
		return "redirect:/info";
	}

	@PostMapping("d")
	public String borrarPost(@RequestParam("id") Long idPais, HttpSession s) {
		String nombrePais = "----";
		try {
			Pais pais = repoPais.getOne(idPais);
			nombrePais = pais.getNombre();
			repoPais.delete(pais);
			H.info(s, "País " + nombrePais + " borrado correctamente", "info", "/pais/r");
		} catch (Exception e) {
			H.info(s, "Error al borrar el país " + nombrePais, "danger", "/pais/c");
		}
		return "redirect:/info";
	}

	@GetMapping("u")
	public String updateGet(@RequestParam("id") Long idPais, ModelMap m) {
		m.put("pais", repoPais.getOne(idPais));
		m.put("view", "/pais/paisU");
		return "/_t/frame";
	}

	@PostMapping("u")
	public String updatePost(@RequestParam("nombre") String nombrePais, @RequestParam("id") Long idPais,
			HttpSession s) {
		try {
			Pais p = repoPais.getOne(idPais);
			p.setNombre(nombrePais);
			repoPais.save(p);
			H.info(s, "País " + nombrePais + " actualizado correctamente", "info", "/pais/r");
		} catch (Exception e) {
			H.info(s, "País " + nombrePais + " duplicado", "danger", "/pais/r");
		}
		return "redirect:/info";
	}

}
