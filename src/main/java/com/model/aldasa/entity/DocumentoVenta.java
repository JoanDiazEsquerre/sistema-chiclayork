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
@Table(name = "documentoventa")
public class DocumentoVenta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idcliente")
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name="iddocumentoventaref")
	private DocumentoVenta documentoVentaRef;
	
	@ManyToOne
	@JoinColumn(name="idatencionmesa")
	private AtencionMesa atencionMesa;
	
	@ManyToOne
	@JoinColumn(name="idsucursal")
	private Sucursal sucursal;
	
	@ManyToOne
	@JoinColumn(name="idtipodocumento")
	private TipoDocumento tipoDocumento;
	
	private String serie;
	
	private String numero;

	private String ruc;

	@Column(name="razonsocial")
	private String razonSocial;
	
	@Column(name="nombrecomercial")
	private String nombreComercial;
	
	private String direccion;
	
	@Column(name="fechaemision")
	private Date fechaEmision;

	@Column(name="fechavencimiento")
	private Date fechaVencimiento;
	
	@Column(name="tipomoneda")
	private String tipoMoneda;
	
	private String observacion;
	
	@Column(name="tipopago")
	private String tipoPago;
	
	@Column(name="subtotal")
	private BigDecimal subTotal;
	
	private BigDecimal igv;
	
	private BigDecimal total;
	
	@Column(name="fecharegistro")
	private Date fechaRegistro;
	
	@ManyToOne
	@JoinColumn(name="idusuarioregistro")
	private Usuario usuarioRegistro;
	
	private boolean estado;

	private BigDecimal anticipos;
	
	@Column(name="opgravada")
	private BigDecimal opGravada;
	
	@Column(name="opexonerada")
	private BigDecimal opExonerada;
	
	@Column(name="opinafecta")
	private BigDecimal opInafecta;
	
	@Column(name="opgratuita")
	private BigDecimal opGratuita;
	
	private BigDecimal descuentos;
	
	private BigDecimal isc;
	
	@Column(name="otroscargos")
	private BigDecimal otrosCargos;
	
	@Column(name="otrostributos")
	private BigDecimal otrosTributos;

	@Column(name="tasacambio")
	private BigDecimal tasaCambio;

	@ManyToOne
	@JoinColumn(name="idmotivonota")
	private MotivoNota motivoNota;
	
	@ManyToOne
	@JoinColumn(name="idtipooperacion")
	private TipoOperacion tipoOperacion;
	
	@ManyToOne
	@JoinColumn(name="ididentificador")
	private Identificador identificador;
	
	@Column(name="notacredito")
	private Boolean notacredito;
	
	@Column(name="notadebito")
	private Boolean notaDebito;
	
	@Column(name="numeronotacredito")
	private String numeroNotaCredito;
	
	@Column(name="numeronotadebito")
	private String numeroNotaDebito;
	
	@Column(name="enviosunat")
	private boolean envioSunat;
	
	@Column(name="envioaceptadaporsunat")
	private String envioAceptadaPorSunat;
	
	@Column(name="enviosunatdescription")
	private String envioSunatDescription;
	
	@Column(name="enviosunatnote")
	private String envioSunatNote;
	
	@Column(name="enviosunatsoaperror")
	private String envioSunatSoapError;
	
	@Column(name="envioenlacedelpdf")
	private String envioEnlaceDelPdf;
	
	@Column(name="envioenlacedelxml")
	private String envioEnlaceDelXml;
	
	@Column(name="envioenlacedelcdr")
	private String envioEnlaceDelCdr;
	
	@Column(name="enviocadenacodigoqr")
	private String envioCadenaCodigoQr;
	
	@Column(name="enviocodigohash")
	private String envioCodigoHash;
	
	@Column(name="anulacionsunatticketnumero")
	private String anulacionSunatTicketNumero;
	
	@Column(name="anulacionaceptadaporsunat")
	private String anulacionAceptadaPorSunat;
	
	@Column(name="anulacionsunatdescription")
	private String anulacionSunatDescription;
	
	@Column(name="anulacionsunatnote")
	private String anulacionSunatNote;
	
	@Column(name="anulacionsunatresponsecode")
	private String anulacionSunatResponsecode;
	
	@Column(name="anulacionsunatsoaperror")
	private String anulacionSunatSoapError;
	
	@Column(name="enviosunatresponsecode")
	private String envioSunatResponseCode;
	
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public DocumentoVenta getDocumentoVentaRef() {
		return documentoVentaRef;
	}
	public void setDocumentoVentaRef(DocumentoVenta documentoVentaRef) {
		this.documentoVentaRef = documentoVentaRef;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
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
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getIgv() {
		return igv;
	}
	public void setIgv(BigDecimal igv) {
		this.igv = igv;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Usuario getUsuarioRegistro() {
		return usuarioRegistro;
	}
	public void setUsuarioRegistro(Usuario usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public BigDecimal getAnticipos() {
		return anticipos;
	}
	public void setAnticipos(BigDecimal anticipos) {
		this.anticipos = anticipos;
	}
	public BigDecimal getOpGravada() {
		return opGravada;
	}
	public void setOpGravada(BigDecimal opGravada) {
		this.opGravada = opGravada;
	}
	public BigDecimal getOpExonerada() {
		return opExonerada;
	}
	public void setOpExonerada(BigDecimal opExonerada) {
		this.opExonerada = opExonerada;
	}
	public BigDecimal getOpInafecta() {
		return opInafecta;
	}
	public void setOpInafecta(BigDecimal opInafecta) {
		this.opInafecta = opInafecta;
	}
	public BigDecimal getOpGratuita() {
		return opGratuita;
	}
	public void setOpGratuita(BigDecimal opGratuita) {
		this.opGratuita = opGratuita;
	}
	public BigDecimal getDescuentos() {
		return descuentos;
	}
	public void setDescuentos(BigDecimal descuentos) {
		this.descuentos = descuentos;
	}
	public BigDecimal getIsc() {
		return isc;
	}
	public void setIsc(BigDecimal isc) {
		this.isc = isc;
	}
	public BigDecimal getOtrosCargos() {
		return otrosCargos;
	}
	public void setOtrosCargos(BigDecimal otrosCargos) {
		this.otrosCargos = otrosCargos;
	}
	public BigDecimal getOtrosTributos() {
		return otrosTributos;
	}
	public void setOtrosTributos(BigDecimal otrosTributos) {
		this.otrosTributos = otrosTributos;
	}
	public MotivoNota getMotivoNota() {
		return motivoNota;
	}
	public void setMotivoNota(MotivoNota motivoNota) {
		this.motivoNota = motivoNota;
	}
	public TipoOperacion getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(TipoOperacion tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public Identificador getIdentificador() {
		return identificador;
	}
	public void setIdentificador(Identificador identificador) {
		this.identificador = identificador;
	}
	public boolean isNotacredito() {
		return notacredito;
	}
	public void setNotacredito(boolean notacredito) {
		this.notacredito = notacredito;
	}
	public boolean isNotaDebito() {
		return notaDebito;
	}
	public void setNotaDebito(boolean notaDebito) {
		this.notaDebito = notaDebito;
	}
	public String getNumeroNotaCredito() {
		return numeroNotaCredito;
	}
	public void setNumeroNotaCredito(String numeroNotaCredito) {
		this.numeroNotaCredito = numeroNotaCredito;
	}
	public String getNumeroNotaDebito() {
		return numeroNotaDebito;
	}
	public void setNumeroNotaDebito(String numeroNotaDebito) {
		this.numeroNotaDebito = numeroNotaDebito;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public BigDecimal getTasaCambio() {
		return tasaCambio;
	}
	public void setTasaCambio(BigDecimal tasaCambio) {
		this.tasaCambio = tasaCambio;
	}
	public Boolean getNotacredito() {
		return notacredito;
	}
	public void setNotacredito(Boolean notacredito) {
		this.notacredito = notacredito;
	}
	public Boolean getNotaDebito() {
		return notaDebito;
	}
	public void setNotaDebito(Boolean notaDebito) {
		this.notaDebito = notaDebito;
	}
	public boolean isEnvioSunat() {
		return envioSunat;
	}
	public void setEnvioSunat(boolean envioSunat) {
		this.envioSunat = envioSunat;
	}
	public String getEnvioAceptadaPorSunat() {
		return envioAceptadaPorSunat;
	}
	public void setEnvioAceptadaPorSunat(String envioAceptadaPorSunat) {
		this.envioAceptadaPorSunat = envioAceptadaPorSunat;
	}
	public String getEnvioSunatDescription() {
		return envioSunatDescription;
	}
	public void setEnvioSunatDescription(String envioSunatDescription) {
		this.envioSunatDescription = envioSunatDescription;
	}
	public String getEnvioSunatNote() {
		return envioSunatNote;
	}
	public void setEnvioSunatNote(String envioSunatNote) {
		this.envioSunatNote = envioSunatNote;
	}
	public String getEnvioSunatSoapError() {
		return envioSunatSoapError;
	}
	public void setEnvioSunatSoapError(String envioSunatSoapError) {
		this.envioSunatSoapError = envioSunatSoapError;
	}
	public String getEnvioEnlaceDelPdf() {
		return envioEnlaceDelPdf;
	}
	public void setEnvioEnlaceDelPdf(String envioEnlaceDelPdf) {
		this.envioEnlaceDelPdf = envioEnlaceDelPdf;
	}
	public String getEnvioEnlaceDelXml() {
		return envioEnlaceDelXml;
	}
	public void setEnvioEnlaceDelXml(String envioEnlaceDelXml) {
		this.envioEnlaceDelXml = envioEnlaceDelXml;
	}
	public String getEnvioEnlaceDelCdr() {
		return envioEnlaceDelCdr;
	}
	public void setEnvioEnlaceDelCdr(String envioEnlaceDelCdr) {
		this.envioEnlaceDelCdr = envioEnlaceDelCdr;
	}
	public String getEnvioCadenaCodigoQr() {
		return envioCadenaCodigoQr;
	}
	public void setEnvioCadenaCodigoQr(String envioCadenaCodigoQr) {
		this.envioCadenaCodigoQr = envioCadenaCodigoQr;
	}
	public String getEnvioCodigoHash() {
		return envioCodigoHash;
	}
	public void setEnvioCodigoHash(String envioCodigoHash) {
		this.envioCodigoHash = envioCodigoHash;
	}
	public String getAnulacionSunatTicketNumero() {
		return anulacionSunatTicketNumero;
	}
	public void setAnulacionSunatTicketNumero(String anulacionSunatTicketNumero) {
		this.anulacionSunatTicketNumero = anulacionSunatTicketNumero;
	}
	public String getAnulacionAceptadaPorSunat() {
		return anulacionAceptadaPorSunat;
	}
	public void setAnulacionAceptadaPorSunat(String anulacionAceptadaPorSunat) {
		this.anulacionAceptadaPorSunat = anulacionAceptadaPorSunat;
	}
	public String getAnulacionSunatDescription() {
		return anulacionSunatDescription;
	}
	public void setAnulacionSunatDescription(String anulacionSunatDescription) {
		this.anulacionSunatDescription = anulacionSunatDescription;
	}
	public String getAnulacionSunatNote() {
		return anulacionSunatNote;
	}
	public void setAnulacionSunatNote(String anulacionSunatNote) {
		this.anulacionSunatNote = anulacionSunatNote;
	}
	public String getAnulacionSunatResponsecode() {
		return anulacionSunatResponsecode;
	}
	public void setAnulacionSunatResponsecode(String anulacionSunatResponsecode) {
		this.anulacionSunatResponsecode = anulacionSunatResponsecode;
	}
	public String getAnulacionSunatSoapError() {
		return anulacionSunatSoapError;
	}
	public void setAnulacionSunatSoapError(String anulacionSunatSoapError) {
		this.anulacionSunatSoapError = anulacionSunatSoapError;
	}
	public String getEnvioSunatResponseCode() {
		return envioSunatResponseCode;
	}
	public void setEnvioSunatResponseCode(String envioSunatResponseCode) {
		this.envioSunatResponseCode = envioSunatResponseCode;
	}
	public AtencionMesa getAtencionMesa() {
		return atencionMesa;
	}
	public void setAtencionMesa(AtencionMesa atencionMesa) {
		this.atencionMesa = atencionMesa;
	}
	
}
