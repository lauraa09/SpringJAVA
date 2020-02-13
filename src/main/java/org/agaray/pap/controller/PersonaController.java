package org.agaray.pap.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Aficion;
import org.agaray.pap.domain.Pais;
import org.agaray.pap.domain.Persona;
import org.agaray.pap.helper.H;
import org.agaray.pap.repository.AficionRepository;
import org.agaray.pap.repository.PaisRepository;
import org.agaray.pap.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/persona")
public class PersonaController {

	@Autowired
	private PersonaRepository repoPersona;

	@Autowired
	private PaisRepository repoPais;

	@Autowired
	private AficionRepository repoAficion;

	
	@PostMapping("d")
	public String borrarPost(@RequestParam("id") Long idPersona, HttpSession s) {
		String nombrePersona = "----";
		try {
			Persona persona=repoPersona.getOne(idPersona);
			nombrePersona= persona.getNombre();
			repoPersona.delete(persona);
			H.info(s, "Persona " + nombrePersona + " borrada correctamente", "info", "/persona/r");
		} catch (Exception e) {
			H.info(s, "Error al borrar la persona " + nombrePersona , "danger", "/persona/c");
		}
		return "redirect:/info";
	}
	

	@GetMapping("c")
	public String crearGet(ModelMap m) {
		m.put("paises", repoPais.findAll());
		m.put("aficiones", repoAficion.findAll());
		m.put("view", "/persona/personaC");
		return "/_t/frame";
	}

	@PostMapping("c")
	public String crearPost(
			@RequestParam("nombre") String nombrePersona,
			@RequestParam("loginname") String loginnamePersona,
			@RequestParam("password") String passwordPersona,
			@RequestParam("idPais") Long idPais, 
			@RequestParam(value="idAficionGusta[]", required = false) List<Long> idGustos,
			@RequestParam(value="idAficionOdio[]", required = false) List<Long> idOdios,
			HttpSession s) {
		try {
			idGustos = (idGustos==null?new ArrayList<Long>():idGustos);
			idOdios= (idOdios==null?new ArrayList<Long>():idOdios);
			
			Persona persona = new Persona(nombrePersona,loginnamePersona,passwordPersona);
			Pais paisNacimiento = repoPais.getOne(idPais);
			
			paisNacimiento.getNacidos().add(persona);
			persona.setNace(paisNacimiento);
			
			for (Long id:idGustos) {
				Aficion aficion = repoAficion.getOne(id);
				aficion.getGustosas().add(persona);
				persona.getGustos().add(aficion);
			}

			for (Long id:idOdios) {
				Aficion aficion = repoAficion.getOne(id);
				aficion.getOdiosas().add(persona);
				persona.getOdios().add(aficion);
			}
			
			repoPersona.save(persona);

			H.info(s, "Persona "+nombrePersona+" creada correctamente", "info", "/persona/r");
		} catch (Exception e) {
			H.info(s, "Error al crear "+nombrePersona, "danger", "/persona/r");
		}
		return "redirect:/info";
	}
	

	@GetMapping("r")
	public String read(
			ModelMap m,
			@RequestParam(value = "f", required = false) String f
			) {
		f = (f==null)?"":f;
		m.put("f",f);
		List<Persona> personas = repoPersona.findDistinctByNombreIgnoreCaseContainingOrLoginnameIgnoreCaseContainingOrNaceNombreIgnoreCaseContainingOrGustosNombreIgnoreCaseContainingOrOdiosNombreIgnoreCaseContainingOrderByNaceNombreDesc(f, f, f, f, f);
		m.put("personas", personas);
		
		m.put("view","/persona/personaR");
		return "/_t/frame";
	}
	
	@GetMapping("u")
	public String updateGet(
			ModelMap m, 
			@RequestParam("id")Long id
			) {
		m.put("persona", repoPersona.getOne(id));
		m.put("paises", repoPais.findAll());
		m.put("aficiones", repoAficion.findAll());
		m.put("view", "/persona/personaU");
		return "/_t/frame";
	}

	@PostMapping("u")
	public String updatePost(
			@RequestParam("id") Long idPersona, 
			@RequestParam("nombre") String nombrePersona, 
			@RequestParam("idPais") Long idPais, 
			@RequestParam(value="idAficionGusta[]", required = false) List<Long> idGustos,
			@RequestParam(value="idAficionOdio[]", required = false) List<Long> idOdios,
			HttpSession s) {
		
		String vista="redirect:/persona/r";

		try {
			
			Persona persona = repoPersona.getOne(idPersona);
			
			
			// ====================
			// ATRIBUTOS "NORMALES"
			persona.setNombre(nombrePersona);
			// ====================
			
			// ====================
			// PAIS NACIMIENTO
			Pais paisNacimiento = repoPais.getOne(idPais);
			Pais paisNacimientoAnt = persona.getNace();
			// ====================
			
			paisNacimientoAnt.getNacidos().remove(persona);
			persona.setNace(null);
			
			paisNacimiento.getNacidos().add(persona);
			persona.setNace(paisNacimiento);
			
			// ====================
			// GUSTOS y ODIOS
			
			idGustos = (idGustos==null?new ArrayList<Long>():idGustos);
			idOdios= (idOdios==null?new ArrayList<Long>():idOdios);

			
			// Creo nueva colección de gustos nuevos y la sustituyo por la antigua
			List<Aficion> gustosNuevos = new ArrayList<Aficion>();
			for (Long id:idGustos) {
				Aficion aficion = repoAficion.getOne(id);
				aficion.getGustosas().add(persona);
				gustosNuevos.add(aficion);
			}
			persona.setGustos(gustosNuevos);
			
			// Creo nueva colección de odios nuevos y la sustituyo por la antigua
			List<Aficion> odiosNuevos = new ArrayList<Aficion>();
			for (Long id:idOdios) {
				Aficion aficion = repoAficion.getOne(id);
				aficion.getOdiosas().add(persona);
				odiosNuevos.add(aficion);
			}
			persona.setOdios(odiosNuevos);
			
			repoPersona.save(persona);

			H.info(s, "Persona "+nombrePersona+" actualizada correctamente", "info", "/persona/r");
		} catch (Exception e) {
			H.info(s, "Error al actualizar "+nombrePersona, "danger", "/persona/r");
			vista="redirect:/info";
		}
		return vista;
	}


}
