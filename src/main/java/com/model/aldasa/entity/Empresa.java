package com.model.aldasa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "empresa")
public class Empresa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre;
	
	private boolean estado;

	@Column(name="rutafe")
	private String rutaFe;
	
	@Column(name="tokenfe")
	private String tokenFe;
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getRutaFe() {
		return rutaFe;
	}
	public void setRutaFe(String rutaFe) {
		this.rutaFe = rutaFe;
	}
	public String getTokenFe() {
		return tokenFe;
	}
	public void setTokenFe(String tokenFe) {
		this.tokenFe = tokenFe;
	}
	
	

}
