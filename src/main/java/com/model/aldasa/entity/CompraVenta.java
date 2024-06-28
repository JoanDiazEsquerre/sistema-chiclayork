package com.model.aldasa.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CompraVenta {

	private Date fecha;
	private BigDecimal montoCompra, montoVenta, montoVentaCredito, montoVentaContado, calculo;
	
	
	
	
	
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public BigDecimal getMontoCompra() {
		return montoCompra;
	}
	public void setMontoCompra(BigDecimal montoCompra) {
		this.montoCompra = montoCompra;
	}
	public BigDecimal getMontoVenta() {
		return montoVenta;
	}
	public void setMontoVenta(BigDecimal montoVenta) {
		this.montoVenta = montoVenta;
	}
	public BigDecimal getCalculo() {
		return calculo;
	}
	public void setCalculo(BigDecimal calculo) {
		this.calculo = calculo;
	}
	public BigDecimal getMontoVentaCredito() {
		return montoVentaCredito;
	}
	public void setMontoVentaCredito(BigDecimal montoVentaCredito) {
		this.montoVentaCredito = montoVentaCredito;
	}
	public BigDecimal getMontoVentaContado() {
		return montoVentaContado;
	}
	public void setMontoVentaContado(BigDecimal montoVentaContado) {
		this.montoVentaContado = montoVentaContado;
	}
	
}
