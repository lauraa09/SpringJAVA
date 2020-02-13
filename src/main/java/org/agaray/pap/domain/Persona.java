package org.agaray.pap.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.type.TrueFalseType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
public class Persona {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;

	@Column(unique = true)
	private String loginname;

	private String password;

	@ManyToOne
	private Pais nace;

	@ManyToMany
	private Collection<Aficion> gustos;

	@ManyToMany
	private Collection<Aficion> odios;

//======================

	public Persona(String nombre) {
		this.nombre = nombre;
		this.gustos = new ArrayList<Aficion>();
		this.odios = new ArrayList<Aficion>();
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = (new BCryptPasswordEncoder()).encode(password);
	}

	public Persona() {
		this.gustos = new ArrayList<Aficion>();
		this.odios = new ArrayList<Aficion>();
	}

	public Persona(String nombrePersona, String loginnamePersona, String passwordPersona) {
		this.nombre = nombrePersona;
		this.loginname = loginnamePersona;
		this.password = (new BCryptPasswordEncoder()).encode(passwordPersona);
		this.gustos = new ArrayList<Aficion>();
		this.odios = new ArrayList<Aficion>();
	}

	public Long getId() {
		return id;
	}

	public Collection<Aficion> getGustos() {
		return gustos;
	}

	public void setGustos(Collection<Aficion> gustos) {
		this.gustos = gustos;
	}

	public Collection<Aficion> getOdios() {
		return odios;
	}

	public void setOdios(Collection<Aficion> odios) {
		this.odios = odios;
	}

	public Pais getNace() {
		return nace;
	}

	public void setNace(Pais nace) {
		this.nace = nace;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
