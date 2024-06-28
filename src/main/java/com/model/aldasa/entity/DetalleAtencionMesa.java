package com.model.aldasa.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "detalleatencionmesa")
public class DetalleAtencionMesa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idatencionmesa")
	private AtencionMesa atencionMesa;
	
	@ManyToOne
	@JoinColumn(name="idplato")
	private Plato plato;
	
	@ManyToOne
	@JoinColumn(name="idplatoentrada")
	private Plato platoEntrada;
	
	private BigDecimal cantidad, total;
	
	@Column(name = "preciounitario")
	private BigDecimal precioUnitario;
	
	private String observacion;
	private boolean estado;

	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public AtencionMesa getAtencionMesa() {
		return atencionMesa;
	}
	public void setAtencionMesa(AtencionMesa atencionMesa) {
		this.atencionMesa = atencionMesa;
	}
	public Plato getPlato() {
		return plato;
	}
	public void setPlato(Plato plato) {
		this.plato = plato;
	}
	public Plato getPlatoEntrada() {
		return platoEntrada;
	}
	public void setPlatoEntrada(Plato platoEntrada) {
		this.platoEntrada = platoEntrada;
	}
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}
