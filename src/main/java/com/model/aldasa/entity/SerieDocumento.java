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
@Table(name = "seriedocumento")
public class SerieDocumento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idtipodocumento")
	private TipoDocumento tipoDocumento;

	private String serie;
	private String numero;
	
	@ManyToOne
	@JoinColumn(name="idsucursal")
	private Sucursal sucursal;
	
	private String anio;
	
	@Column(name = "tamanioserie")
	private Integer tamanioSerie;
	
	@Column(name = "tamanionumero")
	private Integer tamanioNumero;
	
	@Column(name = "codigointerno")
	private String codigoInterno;
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public Integer getTamanioSerie() {
		return tamanioSerie;
	}
	public void setTamanioSerie(Integer tamanioSerie) {
		this.tamanioSerie = tamanioSerie;
	}
	public Integer getTamanioNumero() {
		return tamanioNumero;
	}
	public void setTamanioNumero(Integer tamanioNumero) {
		this.tamanioNumero = tamanioNumero;
	}
	public String getCodigoInterno() {
		return codigoInterno;
	}
	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}


}
