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
@Table(name = "detallecredito")
public class DetalleCredito {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idcredito")
	private Credito credito;
	
	@ManyToOne
	@JoinColumn(name="idplato")
	private Plato plato;
	
	@Column(name = "preciounitario")
	private BigDecimal precioUnitario;
	
	private BigDecimal cantidad;
	
	private BigDecimal total;

	private String observacion;

	private boolean estado;
	
	@Column(name = "estadopago")
	private boolean estadoPago;
	

	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Credito getCredito() {
		return credito;
	}
	public void setCredito(Credito credito) {
		this.credito = credito;
	}
	public Plato getPlato() {
		return plato;
	}
	public void setPlato(Plato plato) {
		this.plato = plato;
	}
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
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
	public boolean isEstadoPago() {
		return estadoPago;
	}
	public void setEstadoPago(boolean estadoPago) {
		this.estadoPago = estadoPago;
	}

	
	
	
	

	
	
}
