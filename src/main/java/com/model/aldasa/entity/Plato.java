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
@Table(name = "plato")
public class Plato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre, automatico;
	private BigDecimal precio;
	private boolean estado;
	
	@Column(name = "encarta")
	private String enCarta;
	
	@ManyToOne
	@JoinColumn(name="idproducto")
	private Producto producto;	
	
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
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getAutomatico() {
		return automatico;
	}
	public void setAutomatico(String automatico) {
		this.automatico = automatico;
	}
	public String getEnCarta() {
		return enCarta;
	}
	public void setEnCarta(String enCarta) {
		this.enCarta = enCarta;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof Plato) && (id != null)
            ? id.equals(((Plato) other).id)
            : (other == this);
    }

   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }

}
