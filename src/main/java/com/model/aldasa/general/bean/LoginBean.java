package com.model.aldasa.general.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.entity.UsuarioSucursal;
import com.model.aldasa.service.SucursalService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.service.UsuarioSucursalService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;
    
    @Autowired
   	private SucursalService sucursalService;
    
    @Autowired
   	private UsuarioSucursalService usuarioSucursalService;
       
       @Autowired
   	private UsuarioService usuarioService; 
       

    private String username;
    private String password;
    
    private Sucursal sucursal;
    
    private List<Sucursal> lstSucursal;
    
    @PostConstruct
  	public void init() {
      	lstSucursal =sucursalService.findByEstado(true);
  	}

    public String doLogin2() {
        return "success";
    }

    public String doLogin() {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);  
            Authentication auth = authenticationManager.authenticate(token);
            String valor= "error";
            if(auth.isAuthenticated()) {
            	Usuario usu = usuarioService.findByUsernameAndPassword(username, password); 
            	UsuarioSucursal usuSuc = usuarioSucursalService.findByUsuarioAndSucursal(usu, sucursal);
            	if(usuSuc!=null) {
            		SecurityContextHolder.getContext().setAuthentication(auth);
            		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sucursalLog", sucursal); 

                    //navegacionBean.onPageLoad();
                    response.sendRedirect(request.getContextPath()+"/secured/view/home.xhtml");
                    valor = "success";
            	}else {
            		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sucursalLog", null); 
            		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tienes acceso a la sucursal", "No tienes acceso a la sucursal"));
            	}
            }
            
            return valor; // Otra página de la aplicación
        } catch (AuthenticationException | IOException e) {
        	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sucursalLog", null); 
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nombre de usuario o contraseña incorrectos", "Nombre de usuario o contraseña incorrectos"));
            return "error"; // Mismo LoginBean
        }
    }
    
    public Converter getConversorSucursal() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Sucursal c = null;
                    for (Sucursal si : lstSucursal) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Sucursal) value).getId() + "";
                }
            }
        };
    }
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public List<Sucursal> getLstSucursal() {
		return lstSucursal;
	}
	public void setLstSucursal(List<Sucursal> lstSucursal) {
		this.lstSucursal = lstSucursal;
	}
	public SucursalService getSucursalService() {
		return sucursalService;
	}
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	public UsuarioSucursalService getUsuarioSucursalService() {
		return usuarioSucursalService;
	}
	public void setUsuarioSucursalService(UsuarioSucursalService usuarioSucursalService) {
		this.usuarioSucursalService = usuarioSucursalService;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
    
}