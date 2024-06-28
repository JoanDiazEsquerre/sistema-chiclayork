package com.model.aldasa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cliente")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idperson")
	private Persona person;
	
	@Column(name="razonsocial")
	private String razonSocial;
	
	@Column(name="nombrecomercial")
	private String nombreComercial;
	
	@Column(name="dniruc")
	private String dniRuc;
	
	private String direccion;
	
	@Column(name="personanatural")
	private boolean personaNatural;
	
	private boolean estado;
	
	@Column(name="fecharegistro")
	private Date fechaRegistro;

	@ManyToOne
	@JoinColumn(name="idusuarioregistro")
	private Usuario idUsuarioRegistro;

	@Column(name="email1fe")
	private String email1Fe;
	
	@Column(name="email2fe")
	private String email2Fe;
	
	@Column(name="email3fe")
	private String email3Fe;

	
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Persona getPerson() {
		return person;
	}
	public void setPerson(Persona person) {
		this.person = person;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getNombreComercial() {
		return nombreComercial;
	}
	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}
	
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Usuario getIdUsuarioRegistro() {
		return idUsuarioRegistro;
	}
	public void setIdUsuarioRegistro(Usuario idUsuarioRegistro) {
		this.idUsuarioRegistro = idUsuarioRegistro;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public boolean isPersonaNatural() {
		return personaNatural;
	}
	public void setPersonaNatural(boolean personaNatural) {
		this.personaNatural = personaNatural;
	}
	public String getEmail1Fe() {
		return email1Fe;
	}
	public void setEmail1Fe(String email1Fe) {
		this.email1Fe = email1Fe;
	}
	public String getEmail2Fe() {
		return email2Fe;
	}
	public void setEmail2Fe(String email2Fe) {
		this.email2Fe = email2Fe;
	}
	public String getEmail3Fe() {
		return email3Fe;
	}
	public void setEmail3Fe(String email3Fe) {
		this.email3Fe = email3Fe;
	}
	public String getDniRuc() {
		return dniRuc;
	}
	public void setDniRuc(String dniRuc) {
		this.dniRuc = dniRuc;
	}
	
}
