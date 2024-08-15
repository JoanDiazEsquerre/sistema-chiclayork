package com.model.aldasa.general.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.security.core.context.SecurityContextHolder;

import com.model.aldasa.atencion.bean.AtencionBean;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.Perfiles;


@ManagedBean()
@SessionScoped
public class NavegacionBean implements Serializable  { 
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	
	private String ruta;
	private String username;                              
	private Usuario usuarioLogin = new Usuario();
	private Sucursal sucursalLogin;
	
	
	private boolean menuPlatos, menuMantenimientos, menuMesas, menuReportes, menuCompras, menucaja, menuVentas, menuAsistencia;
	private boolean subMenuPlatos, subMenuPersonas, subMenuUsuarios, subMenuMesas, subMenuReporteAtencion, subMenuCompras, subMenuComprasVentas, subMenuCaja,
	subMenuDocumentoVentas, subMenuReporteDocumentoVenta, subMenuCredito, subMenuAsistencia, subMenuReporteAsistencia;
	
	private int[] permisoPlatos= {Perfiles.ADMINISTRADOR.getId(),Perfiles.MESERO.getId(), Perfiles.CHEF.getId(), Perfiles.CAJERO.getId() };
	private int[] permisoPersonas= {Perfiles.ADMINISTRADOR.getId()};
	private int[] permisoUsuarios= {Perfiles.ADMINISTRADOR.getId()};
	private int[] permisoMesas= {Perfiles.ADMINISTRADOR.getId(),Perfiles.MESERO.getId(), Perfiles.CHEF.getId(), Perfiles.CAJERO.getId()}; 
	private int[] permisoReporteAtencion= {Perfiles.ADMINISTRADOR.getId()};
	private int[] permisoCompras= {Perfiles.ADMINISTRADOR.getId()};
	private int[] permisoComprasVentas= {Perfiles.ADMINISTRADOR.getId()};
	private int[] permisoCaja= {Perfiles.ADMINISTRADOR.getId(), Perfiles.CAJERO.getId()};
	private int[] permisoDocumentoVentas= {Perfiles.ADMINISTRADOR.getId(), Perfiles.CAJERO.getId()};
	private int[] permisoReporteDocumentoVenta= {Perfiles.ADMINISTRADOR.getId()};
	private int[] permisoCredito= {Perfiles.ADMINISTRADOR.getId(), Perfiles.CAJERO.getId()};
	private int[] permisoAsistencia= {Perfiles.ADMINISTRADOR.getId(), Perfiles.ASISTENCIA.getId()};
	private int[] permisoReporteAsistencia = {Perfiles.ADMINISTRADOR.getId()};


	@PostConstruct
	public void init() {
		sucursalLogin = (Sucursal)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sucursalLog"); 
		if(sucursalLogin==null) {
			cerrarSesion();
			return;
			
		}
		ruta = "modulos/general/mantenimientos/inicio.xhtml";
		username = SecurityContextHolder.getContext().getAuthentication().getName();
		usuarioLogin = usuarioService.findByUsername(username);
		permisoPantallas();
	}
	

	
	public void permisoPantallas() {
		menuPlatos=false; 
		menuMantenimientos=false;
		menuReportes = false;
		menuCompras = false;
		menucaja = false;
		//*******************************************************************************
		subMenuMesas = validaPermiso(permisoMesas);
		
		if(subMenuMesas) {
			menuMesas=true;
		}
		
		//*******************************************************************************
		subMenuPlatos = validaPermiso(permisoPlatos);
		
		if(subMenuPlatos) {
			menuPlatos=true;
		}
		//*******************************************************************************
		subMenuPersonas = validaPermiso(permisoPersonas);
		subMenuUsuarios = validaPermiso(permisoUsuarios);
		
		
		if(subMenuPersonas || subMenuUsuarios) {
			menuMantenimientos=true;
		}
		
		//*******************************************************************************
		
		subMenuReporteAtencion = validaPermiso(permisoReporteAtencion);
		subMenuComprasVentas = validaPermiso(permisoComprasVentas);
		subMenuReporteDocumentoVenta = validaPermiso(permisoReporteDocumentoVenta);
		subMenuReporteAsistencia = validaPermiso(permisoReporteAsistencia);
		
		if(subMenuReporteAtencion || subMenuComprasVentas || subMenuReporteDocumentoVenta || subMenuReporteAsistencia) {
			menuReportes=true;
		}
				
		//*******************************************************************************
		
		subMenuCompras = validaPermiso(permisoCompras);
		
		if(subMenuCompras) {
			menuCompras=true;
		}
				
		//*******************************************************************************
		
		subMenuCaja = validaPermiso(permisoCaja);
		
		if(subMenuCaja) {
			menucaja=true;
		}
				
		//*******************************************************************************
				
		subMenuDocumentoVentas = validaPermiso(permisoDocumentoVentas);
		subMenuCredito= validaPermiso(permisoCredito);
		
		if(subMenuDocumentoVentas || subMenuCredito) {
			menuVentas=true;
		}
				
		//*******************************************************************************
		
		subMenuAsistencia = validaPermiso(permisoAsistencia);
		
		if(subMenuAsistencia) {
			menuAsistencia=true;
		}
				
		//*******************************************************************************
				
	}
	
	public boolean validaPermiso(int[] permiso) {
		boolean valida = false;
		for(int per:permiso) {
			if(per == usuarioLogin.getPerfil().getId()) {
				valida= true;
			}
		}
		
		return valida;
	}
	
//	public void detenerTimer() {
//        if (atencionBean.getTimer() != null) {
//        	atencionBean.getTimer().cancel();
//        	atencionBean.setTimer(null); // Liberar la referencia al Timer
//        }
//    }
	
	
	public void getProcesoPlatosPage() {
		ruta = "modulos/platos/mantenimientos/platos.xhtml";
	}
	public void getProcesoPersonaPage() {
		ruta = "modulos/general/mantenimientos/personas.xhtml";
	}
	public void getProcesoCreditoPage() {
		ruta = "modulos/ventas/procesos/credito.xhtml";
	}
	public void getProcesoUsuarioPage() {
		ruta = "modulos/general/mantenimientos/usuarios.xhtml";
	}
	public void getProcesoMesasPage() {
		ruta = "modulos/atencion/procesos/atencion.xhtml";
	}
	public void getProcesoReporteAtencionPage() {
		ruta = "modulos/atencion/reportes/reporteAtencion.xhtml";
	}
	public void getProcesoComprasPage() {
		ruta = "modulos/compras/procesos/compras.xhtml";
	}
	public void getReporteComprasVentasPage() {
		ruta = "modulos/compras/reportes/comprasVentas.xhtml";
	}
	
	public void getProcesoCajaPage() {
		ruta = "modulos/caja/procesos/caja.xhtml";
	}
	public void getProcesosDocumentoVentasPage() {
		ruta = "modulos/ventas/procesos/documentoVenta.xhtml";
	}
	public void getReportesDocumentoVentaPage() {
		ruta = "modulos/ventas/reportes/reporteDocumentoVenta.xhtml";
	}
	public void getAsistenciaAsistenciaPage() {
		ruta = "modulos/asistencia/procesos/asistencia.xhtml";
	}
	public void getAsistenciaReporteAsistenciaPage() {
		ruta = "modulos/asistencia/procesos/reporteAsistencia.xhtml";
	}
	

	
	public void cerrarSesion() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}
	


	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}
	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}
	public Sucursal getSucursalLogin() {
		return sucursalLogin;
	}
	public void setSucursalLogin(Sucursal sucursalLogin) {
		this.sucursalLogin = sucursalLogin;
	}
	public boolean isMenuPlatos() {
		return menuPlatos;
	}
	public void setMenuPlatos(boolean menuPlatos) {
		this.menuPlatos = menuPlatos;
	}
	public boolean isSubMenuPlatos() {
		return subMenuPlatos;
	}
	public void setSubMenuPlatos(boolean subMenuPlatos) {
		this.subMenuPlatos = subMenuPlatos;
	}
	public int[] getPermisoPlatos() {
		return permisoPlatos;
	}
	public void setPermisoPlatos(int[] permisoPlatos) {
		this.permisoPlatos = permisoPlatos;
	}
	public boolean isMenuMantenimientos() {
		return menuMantenimientos;
	}
	public void setMenuMantenimientos(boolean menuMantenimientos) {
		this.menuMantenimientos = menuMantenimientos;
	}
	public boolean isSubMenuPersonas() {
		return subMenuPersonas;
	}
	public void setSubMenuPersonas(boolean subMenuPersonas) {
		this.subMenuPersonas = subMenuPersonas;
	}
	public int[] getPermisoPersonas() {
		return permisoPersonas;
	}
	public void setPermisoPersonas(int[] permisoPersonas) {
		this.permisoPersonas = permisoPersonas;
	}
	public boolean isSubMenuUsuarios() {
		return subMenuUsuarios;
	}
	public void setSubMenuUsuarios(boolean subMenuUsuarios) {
		this.subMenuUsuarios = subMenuUsuarios;
	}
	public int[] getPermisoUsuarios() {
		return permisoUsuarios;
	}
	public void setPermisoUsuarios(int[] permisoUsuarios) {
		this.permisoUsuarios = permisoUsuarios;
	}
	public boolean isMenuMesas() {
		return menuMesas;
	}
	public void setMenuMesas(boolean menuMesas) {
		this.menuMesas = menuMesas;
	}
	public boolean isSubMenuMesas() {
		return subMenuMesas;
	}
	public void setSubMenuMesas(boolean subMenuMesas) {
		this.subMenuMesas = subMenuMesas;
	}
	public int[] getPermisoMesas() {
		return permisoMesas;
	}
	public void setPermisoMesas(int[] permisoMesas) {
		this.permisoMesas = permisoMesas;
	}
	public boolean isMenuReportes() {
		return menuReportes;
	}
	public void setMenuReportes(boolean menuReportes) {
		this.menuReportes = menuReportes;
	}
	public boolean isSubMenuReporteAtencion() {
		return subMenuReporteAtencion;
	}
	public void setSubMenuReporteAtencion(boolean subMenuReporteAtencion) {
		this.subMenuReporteAtencion = subMenuReporteAtencion;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public boolean isMenuCompras() {
		return menuCompras;
	}
	public void setMenuCompras(boolean menuCompras) {
		this.menuCompras = menuCompras;
	}
	public int[] getPermisoReporteAtencion() {
		return permisoReporteAtencion;
	}
	public void setPermisoReporteAtencion(int[] permisoReporteAtencion) {
		this.permisoReporteAtencion = permisoReporteAtencion;
	}
	public boolean isSubMenuCompras() {
		return subMenuCompras;
	}
	public void setSubMenuCompras(boolean subMenuCompras) {
		this.subMenuCompras = subMenuCompras;
	}
	public int[] getPermisoCompras() {
		return permisoCompras;
	}
	public void setPermisoCompras(int[] permisoCompras) {
		this.permisoCompras = permisoCompras;
	}
	public boolean isSubMenuComprasVentas() {
		return subMenuComprasVentas;
	}
	public void setSubMenuComprasVentas(boolean subMenuComprasVentas) {
		this.subMenuComprasVentas = subMenuComprasVentas;
	}
	public int[] getPermisoComprasVentas() {
		return permisoComprasVentas;
	}
	public void setPermisoComprasVentas(int[] permisoComprasVentas) {
		this.permisoComprasVentas = permisoComprasVentas;
	}
	public boolean isMenucaja() {
		return menucaja;
	}
	public void setMenucaja(boolean menucaja) {
		this.menucaja = menucaja;
	}
	public boolean isSubMenuCaja() {
		return subMenuCaja;
	}
	public void setSubMenuCaja(boolean subMenuCaja) {
		this.subMenuCaja = subMenuCaja;
	}
	public boolean isMenuVentas() {
		return menuVentas;
	}
	public void setMenuVentas(boolean menuVentas) {
		this.menuVentas = menuVentas;
	}
	public boolean isSubMenuDocumentoVentas() {
		return subMenuDocumentoVentas;
	}
	public void setSubMenuDocumentoVentas(boolean subMenuDocumentoVentas) {
		this.subMenuDocumentoVentas = subMenuDocumentoVentas;
	}
	public int[] getPermisoCaja() {
		return permisoCaja;
	}
	public void setPermisoCaja(int[] permisoCaja) {
		this.permisoCaja = permisoCaja;
	}
	public int[] getPermisoDocumentoVentas() {
		return permisoDocumentoVentas;
	}
	public void setPermisoDocumentoVentas(int[] permisoDocumentoVentas) {
		this.permisoDocumentoVentas = permisoDocumentoVentas;
	}
	public boolean isSubMenuReporteDocumentoVenta() {
		return subMenuReporteDocumentoVenta;
	}
	public void setSubMenuReporteDocumentoVenta(boolean subMenuReporteDocumentoVenta) {
		this.subMenuReporteDocumentoVenta = subMenuReporteDocumentoVenta;
	}
	public int[] getPermisoReporteDocumentoVenta() {
		return permisoReporteDocumentoVenta;
	}
	public void setPermisoReporteDocumentoVenta(int[] permisoReporteDocumentoVenta) {
		this.permisoReporteDocumentoVenta = permisoReporteDocumentoVenta;
	}
	public boolean isSubMenuCredito() {
		return subMenuCredito;
	}
	public void setSubMenuCredito(boolean subMenuCredito) {
		this.subMenuCredito = subMenuCredito;
	}
	public int[] getPermisoCredito() {
		return permisoCredito;
	}
	public void setPermisoCredito(int[] permisoCredito) {
		this.permisoCredito = permisoCredito;
	}
	public boolean isMenuAsistencia() {
		return menuAsistencia;
	}
	public void setMenuAsistencia(boolean menuAsistencia) {
		this.menuAsistencia = menuAsistencia;
	}
	public boolean isSubMenuAsistencia() {
		return subMenuAsistencia;
	}
	public void setSubMenuAsistencia(boolean subMenuAsistencia) {
		this.subMenuAsistencia = subMenuAsistencia;
	}
	public boolean isSubMenuReporteAsistencia() {
		return subMenuReporteAsistencia;
	}
	public void setSubMenuReporteAsistencia(boolean subMenuReporteAsistencia) {
		this.subMenuReporteAsistencia = subMenuReporteAsistencia;
	}
	public int[] getPermisoAsistencia() {
		return permisoAsistencia;
	}
	public void setPermisoAsistencia(int[] permisoAsistencia) {
		this.permisoAsistencia = permisoAsistencia;
	}
	
}
