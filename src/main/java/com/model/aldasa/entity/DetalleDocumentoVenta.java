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
@Table(name = "detalledocumentoventa")
public class DetalleDocumentoVenta{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="iddocumentoventa")
	private DocumentoVenta documentoVenta;
	
	@ManyToOne
	@JoinColumn(name="idproducto")
	private Producto producto;
	
	private String descripcion;
	
	@Column(name="cantidad")
	private BigDecimal cantidad;
	
	@Column(name="valorunitario")
	private BigDecimal valorUnitario;
	
	@Column(name="preciounitario")
	private BigDecimal precioUnitario;
	
	private BigDecimal total;
	
	private boolean estado;
	
	@Column(name="importeventasinigv")
	private BigDecimal importeVentaSinIgv;
	
	@Column(name="preciosinigv")
	private BigDecimal precioSinIgv;
	
	@ManyToOne
	@JoinColumn(name="iddetallecredito")
	private DetalleCredito detalleCredito;
	


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public DocumentoVenta getDocumentoVenta() {
		return documentoVenta;
	}
	public void setDocumentoVenta(DocumentoVenta documentoVenta) {
		this.documentoVenta = documentoVenta;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public BigDecimal getImporteVentaSinIgv() {
		return importeVentaSinIgv;
	}
	public void setImporteVentaSinIgv(BigDecimal importeVentaSinIgv) {
		this.importeVentaSinIgv = importeVentaSinIgv;
	}
	public BigDecimal getPrecioSinIgv() {
		return precioSinIgv;
	}
	public void setPrecioSinIgv(BigDecimal precioSinIgv) {
		this.precioSinIgv = precioSinIgv;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	public DetalleCredito getDetalleCredito() {
		return detalleCredito;
	}
	public void setDetalleCredito(DetalleCredito detalleCredito) {
		this.detalleCredito = detalleCredito;
	}
	
	
	
}
