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
@Table(name = "credito")
public class Credito {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name="idpersona")
	private Persona persona;
	
	private BigDecimal total;
	
	private String estado;
	
	@ManyToOne
	@JoinColumn(name="idusercrea")
	private Usuario userCrea;
	
	@Column(name="fechacrea")
	private Date fechaCrea;
	
	@Column(name="montopagado")
	private BigDecimal montoPagado;
	
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Usuario getUserCrea() {
		return userCrea;
	}
	public void setUserCrea(Usuario userCrea) {
		this.userCrea = userCrea;
	}
	public Date getFechaCrea() {
		return fechaCrea;
	}
	public void setFechaCrea(Date fechaCrea) {
		this.fechaCrea = fechaCrea;
	}
	public BigDecimal getMontoPagado() {
		return montoPagado;
	}
	public void setMontoPagado(BigDecimal montoPagado) {
		this.montoPagado = montoPagado;
	}
	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof Credito) && (id != null)
            ? id.equals(((Credito) other).id)
            : (other == this);
    }

   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
	
}
