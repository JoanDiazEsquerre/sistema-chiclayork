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
@Table(name = "atencionmesa")
public class AtencionMesa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idpiso")
	private Piso piso;
	
	@ManyToOne
	@JoinColumn(name="idsucursal")
	private Sucursal sucursal;
	
	@ManyToOne
	@JoinColumn(name="idusuario")
	private Usuario usuario;
	
	@Column(name = "nummesa")
	private int numMesa;
	
	@Column(name = "estadochef")
	private String estadoChef;
	
	private String estado;
	
	@Column(name = "fechacobro")
	private Date fechaCobro;

	@Column(name = "fecharegistro")
	private Date fechaRegistro;
	
	private boolean credito;
	
	@Column(name = "dniruc")
	private String dniRuc;
	
	@Column(name = "nombrecliente")
	private String nombreCliente;
	
	@Column(name = "tipopago")
	private String tipoPago;
	
	@Column(name = "tipopago2")
	private String tipoPago2;
	
	private BigDecimal monto;
	
	@Column(name = "monto2")
	private BigDecimal monto2;
	
	@ManyToOne
	@JoinColumn(name="idtipodocumento")
	private TipoDocumento tipoDocumento;
	
	@ManyToOne
	@JoinColumn(name="idusuariocobro")
	private Usuario usuarioCobro;
	
	@ManyToOne
	@JoinColumn(name="iddocumentoventa")
	private DocumentoVenta documentoVenta;
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Piso getPiso() {
		return piso;
	}
	public void setPiso(Piso piso) {
		this.piso = piso;
	}
	public int getNumMesa() {
		return numMesa;
	}
	public void setNumMesa(int numMesa) {
		this.numMesa = numMesa;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public Date getFechaCobro() {
		return fechaCobro;
	}
	public void setFechaCobro(Date fechaCobro) {
		this.fechaCobro = fechaCobro;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String getEstadoChef() {
		return estadoChef;
	}
	public void setEstadoChef(String estadoChef) {
		this.estadoChef = estadoChef;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public boolean isCredito() {
		return credito;
	}
	public void setCredito(boolean credito) {
		this.credito = credito;
	}
	public String getDniRuc() {
		return dniRuc;
	}
	public void setDniRuc(String dniRuc) {
		this.dniRuc = dniRuc;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public Usuario getUsuarioCobro() {
		return usuarioCobro;
	}
	public void setUsuarioCobro(Usuario usuarioCobro) {
		this.usuarioCobro = usuarioCobro;
	}
	public DocumentoVenta getDocumentoVenta() {
		return documentoVenta;
	}
	public void setDocumentoVenta(DocumentoVenta documentoVenta) {
		this.documentoVenta = documentoVenta;
	}
	public String getTipoPago2() {
		return tipoPago2;
	}
	public void setTipoPago2(String tipoPago2) {
		this.tipoPago2 = tipoPago2;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public BigDecimal getMonto2() {
		return monto2;
	}
	public void setMonto2(BigDecimal monto2) {
		this.monto2 = monto2;
	}

	

}
