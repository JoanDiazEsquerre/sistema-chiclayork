package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.atencion.bean.AtencionBean;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class InicioBean implements Serializable {
	
	@ManagedProperty(value = "#{atencionBean}")
	private AtencionBean atencionBean;
	
	private static final long serialVersionUID = 1L;
	
	private String texto1, texto2, texto3, texto4;
	
	@PostConstruct
	public void init() {
		texto1 = "Cada uno según el don que ha recibido,";
		texto2 = "adminístrelo a los otros,";
		texto3 = "como buenos dispensadores de";
		texto4 = "las diferentes gracias de Dios. (Pedro 4:10)";
		
	}
	
	public void detenerTimer() {
        if (atencionBean.getTimer() != null) {
        	atencionBean.getTimer().cancel();
        	atencionBean.setTimer(null); // Liberar la referencia al Timer
        }
    }
	
	
	public String getTexto1() {
		return texto1;
	}
	public void setTexto1(String texto1) {
		this.texto1 = texto1;
	}
	public String getTexto2() {
		return texto2;
	}
	public void setTexto2(String texto2) {
		this.texto2 = texto2;
	}
	public String getTexto3() {
		return texto3;
	}
	public void setTexto3(String texto3) {
		this.texto3 = texto3;
	}
	public String getTexto4() {
		return texto4;
	}
	public void setTexto4(String texto4) {
		this.texto4 = texto4;
	}

	public AtencionBean getAtencionBean() {
		return atencionBean;
	}

	public void setAtencionBean(AtencionBean atencionBean) {
		this.atencionBean = atencionBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
