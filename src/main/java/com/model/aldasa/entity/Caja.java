package com.model.aldasa.entity;

import java.math.BigDecimal;
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
@Table(name = "caja")
public class Caja {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Date fecha;
	
	@Column(name = "montoinicioefectivo")
	private BigDecimal montoInicioEfectivo;
	
	@Column(name = "montofinalefectivo")
	private BigDecimal montoFinalEfectivo;
	
	@Column(name = "montoiniciopos")
	private BigDecimal montoInicioPos;
	
	@Column(name = "montofinalpos")
	private BigDecimal montoFinalPos;
	
	@ManyToOne
	@JoinColumn(name="idusuario")
	private Usuario usuario;
	
	private String estado;

	@ManyToOne
	@JoinColumn(name="idsucursal")
	private Sucursal sucursal;
	
	
	public Integer getId() {
		return id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public BigDecimal getMontoInicioEfectivo() {
		return montoInicioEfectivo;
	}
	public void setMontoInicioEfectivo(BigDecimal montoInicioEfectivo) {
		this.montoInicioEfectivo = montoInicioEfectivo;
	}
	public BigDecimal getMontoFinalEfectivo() {
		return montoFinalEfectivo;
	}
	public void setMontoFinalEfectivo(BigDecimal montoFinalEfectivo) {
		this.montoFinalEfectivo = montoFinalEfectivo;
	}
	public BigDecimal getMontoInicioPos() {
		return montoInicioPos;
	}
	public void setMontoInicioPos(BigDecimal montoInicioPos) {
		this.montoInicioPos = montoInicioPos;
	}
	public BigDecimal getMontoFinalPos() {
		return montoFinalPos;
	}
	public void setMontoFinalPos(BigDecimal montoFinalPos) {
		this.montoFinalPos = montoFinalPos;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}


}
