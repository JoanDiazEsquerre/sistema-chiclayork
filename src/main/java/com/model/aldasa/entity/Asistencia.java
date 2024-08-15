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
@Table(name = "asistencia")
public class Asistencia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idusuario")
	private Usuario usuario;
	
	private String tipo;
	
	private Date hora;
	
	@ManyToOne
	@JoinColumn(name="idusuariocrea")
	private Usuario usuarioCrea;
	
	@Column(name="fechacrea")
	private Date fechaCrea;

	@ManyToOne
	@JoinColumn(name="idusuariomodifica")
	private Usuario usuarioModifica;
	
	@Column(name="fechamodifica")
	private Date fechaModifica;
	
	private boolean estado;

	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Date getHora() {
		return hora;
	}
	public void setHora(Date hora) {
		this.hora = hora;
	}
	public Usuario getUsuarioCrea() {
		return usuarioCrea;
	}
	public void setUsuarioCrea(Usuario usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}
	public Usuario getUsuarioModifica() {
		return usuarioModifica;
	}
	public void setUsuarioModifica(Usuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	public Date getFechaCrea() {
		return fechaCrea;
	}
	public void setFechaCrea(Date fechaCrea) {
		this.fechaCrea = fechaCrea;
	}
	public Date getFechaModifica() {
		return fechaModifica;
	}
	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof Asistencia) && (id != null)
            ? id.equals(((Asistencia) other).id)
            : (other == this);
    }
	
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
	
}
