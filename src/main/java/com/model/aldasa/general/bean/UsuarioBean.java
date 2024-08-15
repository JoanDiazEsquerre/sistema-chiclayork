package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.tomcat.jni.User;
import org.eclipse.jdt.internal.compiler.env.IModule.IService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Perfil;
import com.model.aldasa.entity.Persona;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.entity.UsuarioSucursal;
import com.model.aldasa.service.PersonaService;
import com.model.aldasa.service.EmpresaService;
import com.model.aldasa.service.PerfilService;
import com.model.aldasa.service.SucursalService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.service.UsuarioSucursalService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoProspeccion;

@ManagedBean
@ViewScoped
public class UsuarioBean extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService; 
	
	@ManagedProperty(value = "#{personaService}")
	private PersonaService personaService; 
	
	@ManagedProperty(value = "#{perfilService}")
	private PerfilService perfilService;  
	
	@ManagedProperty(value = "#{sucursalService}")
	private SucursalService sucursalService;
	
	@ManagedProperty(value = "#{usuarioSucursalService}")
	private UsuarioSucursalService usuarioSucursalService;
	
	@ManagedProperty(value = "#{empresaService}")
	private EmpresaService empresaService;
	
	private LazyDataModel<Usuario> lstUsuarioLazy;
	
	private List<Usuario> lstUsuario;
	private List<Persona> lstPersona;
	private List<Perfil> lstPerfil;
    private List<Sucursal> lstSucursal;
    private List<Empresa> lstEmpresa ;
    private List<UsuarioSucursal> lstUsuarioSucursal ;

	
	private Usuario userSelected;
    private Sucursal sucursal;
    private Empresa empresa ;
    private UsuarioSucursal usuarioSucursalSelected;
    private UsuarioSucursal suc;

	private boolean estado=true;
	private boolean validaUsuario;
	
	private String tituloDialog="";
	
	@PostConstruct
	public void init() {
		iniciarLazy();
    	lstEmpresa = empresaService.findByEstado(true);
    	empresa = lstEmpresa.get(0);
    	listarSucursalPorEmpresa();
    }

	public void listarSucursalPorEmpresa() {
		lstSucursal = sucursalService.findByEmpresaAndEstado(empresa, true);
		if(!lstSucursal.isEmpty()) {
			sucursal=lstSucursal.get(0);
		}
		
				
	}
	
	public void iniciarLazy() {

		lstUsuarioLazy = new LazyDataModel<Usuario>() {
			private List<Usuario> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Usuario getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Usuario usuario : datasource) {
                    if (usuario.getId() == intRowKey) {
                        return usuario;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Usuario usuario) {
                return String.valueOf(usuario.getId());
            }

			@Override
			public List<Usuario> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                //Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
                //Si debageas aqui te vas a dar cuenta como lo captura
                
                String username="%"+ (filterBy.get("username")!=null?filterBy.get("username").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
                String password="%"+ (filterBy.get("password")!=null?filterBy.get("password").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
                String personSurnames="%"+ (filterBy.get("persona.apellidos")!=null?filterBy.get("person.apellidos").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
                String profileName="%"+ (filterBy.get("perfil.nombre")!=null?filterBy.get("profile.name").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
                
                Sort sort=Sort.by("username").ascending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }
//                
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                //Aqui llamo al servicio que a  su vez llama al repositorio que contiene la sentencia LIKE,
                //Aqui tu tienes que completar la query, yo solo lo he hecho para dni y nombre a modo de ejemplo
                //Tu deberias preparar el metodo para cada filtro que tengas en la tabla
                Page<Usuario> pageUsuario=null;
                
                
                pageUsuario= usuarioService.findByPerfilNombreLikeAndPersonaApellidosLikeAndPasswordLikeAndUsernameLikeAndEstado(profileName, personSurnames, password, username, estado, pageable);
                
                setRowCount((int) pageUsuario.getTotalElements());
                return datasource = pageUsuario.getContent();
            }
		};
	}
	
	public void restablecerValores() {
		lstEmpresa = empresaService.findByEstado(true);
    	empresa = lstEmpresa.get(0);
    	listarSucursalPorEmpresa();
    	
    	lstUsuarioSucursal = usuarioSucursalService.findByUsuario(userSelected);
    	
    	
	}
	
	public void listarPersonas() {
		lstPersona=personaService.findByEstado(true);
		System.out.println("tamaño:"+lstPersona.size()); 
	}
	
	public void listarPerfiles() {
		lstPerfil=perfilService.findByEstado(true);
	}

	public void newUser() {
		tituloDialog = "NUEVO USUARIO";

		listarPersonas();
		listarPerfiles();
		
		userSelected = new Usuario();
		userSelected.setEstado(true); 
		userSelected.setControlarAsistencia(false); 
	}
	
	public void updateUser() {
		tituloDialog = "MODIFICAR USUARIO";
		listarPersonas();
		listarPerfiles();
	}
	
	public void asignarSucursal() {
		
		if(sucursal==null) {
			addErrorMessage("Selecionar una sucursal.");
			return;
		}
		
		UsuarioSucursal usuSucur = usuarioSucursalService.findByUsuarioAndSucursal(userSelected, sucursal);
		
		if(usuSucur!=null) {
			addErrorMessage("El usuario tiene asignada la sucursal.");
			return;
		}else {
			UsuarioSucursal asigSucur = new UsuarioSucursal();
			asigSucur.setSucursal(sucursal);
			asigSucur.setUsuario(userSelected);
			usuarioSucursalService.save(asigSucur);
	    	lstUsuarioSucursal = usuarioSucursalService.findByUsuario(userSelected);
			addInfoMessage("Sucursal asignado correctamente.");

		}
		
	}
	
	public void desasignarSucursal() {
		usuarioSucursalService.delete(suc);
    	lstUsuarioSucursal = usuarioSucursalService.findByUsuario(userSelected);
		addInfoMessage("Desasignado correctamente.");


	}
	
	public void saveUpdateUser() {
		
		if(userSelected.getPersona()==null) {
			addErrorMessage("Falta asignar una persona.");
			return ;
		}else {
			if(tituloDialog.equals("NUEVO USUARIO")) {
				Usuario buscarPorPersona =usuarioService.findByPersona(userSelected.getPersona());
				if(buscarPorPersona!=null) {
					addErrorMessage("La persona esta asignada en otro Usuario.");
					return;
				}
			}else {
				Usuario buscaUsername = usuarioService.findByPersonaException(userSelected.getPersona().getId(), userSelected.getId());
				if(buscaUsername!=null ) {
					addErrorMessage("La persona esta asignada en otro Usuario.");
					return;
				}
			}
			
			
		}
		
		if(userSelected.getUsername() == null) {
			addErrorMessage("Falta ingresar Nombre de usuario.");
			return ;
		}else {
			if(tituloDialog.equals("NUEVO USUARIO")) {
				Usuario buscaUsername = usuarioService.findByUsername(userSelected.getUsername());
				if(buscaUsername!=null ) {
					addErrorMessage("Ya existe el nombre de usuario.");
					return;
				}
			}else {
				Usuario buscaUsername = usuarioService.findByUsernameException(userSelected.getUsername(), userSelected.getId());
				if(buscaUsername!=null ) {
					addErrorMessage("Ya existe el nombre de usuario.");
					return ;
				}
			}
			
		}
		
		if(userSelected.getPassword().equals("") || userSelected.getPassword()==null) {
			addErrorMessage("Falta ingresar contraseña.");
			return ;
		}
		
		if(userSelected.getPerfil()==null) {
			addErrorMessage("Seleccionar perfil.");
			return ;
		}
		
		if(tituloDialog.equals("NUEVO USUARIO")) {
			
			
			Usuario usu = usuarioService.save(userSelected);
			if(usu==null) {
				addErrorMessage("No se puede guardar.");
				return ;
			}
		}else {
			Usuario usu = usuarioService.save(userSelected); 
			if(usu==null) {
				addErrorMessage("No se puede guardar.");
				return ;
			}
		}
			
		if (tituloDialog.equals("NUEVO USUARIO")) {
			newUser();
		}
				
		addInfoMessage("Se guardó correctamente."); 
		
		
			
		
		
	}
	
	public List<Persona> completePerson(String query) {
        List<Persona> lista = new ArrayList<>();
        for (Persona c : getLstPersona()) {
            if (c.getApellidos().toUpperCase().contains(query.toUpperCase()) || c.getNombres().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	public Converter getConversorPerson() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Persona c = null;
                    for (Persona si : lstPersona) {
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
                    return ((Persona) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorProfile() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Perfil c = null;
                    for (Perfil si : lstPerfil) {
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
                    return ((Perfil) value).getId() + "";
                }
            }
        };
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
	
	public Converter getConversorEmpresa() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Empresa c = null;
                    for (Empresa si : lstEmpresa) {
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
                    return ((Empresa) value).getId() + "";
                }
            }
        };
    }

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public PersonaService getPersonaService() {
		return personaService;
	}

	public void setPersonaService(PersonaService personaService) {
		this.personaService = personaService;
	}

	public PerfilService getPerfilService() {
		return perfilService;
	}

	public void setPerfilService(PerfilService perfilService) {
		this.perfilService = perfilService;
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

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public LazyDataModel<Usuario> getLstUsuarioLazy() {
		return lstUsuarioLazy;
	}

	public void setLstUsuarioLazy(LazyDataModel<Usuario> lstUsuarioLazy) {
		this.lstUsuarioLazy = lstUsuarioLazy;
	}

	public List<Usuario> getLstUsuario() {
		return lstUsuario;
	}

	public void setLstUsuario(List<Usuario> lstUsuario) {
		this.lstUsuario = lstUsuario;
	}

	public List<Persona> getLstPersona() {
		return lstPersona;
	}

	public void setLstPersona(List<Persona> lstPersona) {
		this.lstPersona = lstPersona;
	}

	public List<Perfil> getLstPerfil() {
		return lstPerfil;
	}

	public void setLstPerfil(List<Perfil> lstPerfil) {
		this.lstPerfil = lstPerfil;
	}

	public List<Sucursal> getLstSucursal() {
		return lstSucursal;
	}

	public void setLstSucursal(List<Sucursal> lstSucursal) {
		this.lstSucursal = lstSucursal;
	}

	public List<Empresa> getLstEmpresa() {
		return lstEmpresa;
	}

	public void setLstEmpresa(List<Empresa> lstEmpresa) {
		this.lstEmpresa = lstEmpresa;
	}

	public List<UsuarioSucursal> getLstUsuarioSucursal() {
		return lstUsuarioSucursal;
	}

	public void setLstUsuarioSucursal(List<UsuarioSucursal> lstUsuarioSucursal) {
		this.lstUsuarioSucursal = lstUsuarioSucursal;
	}

	public Usuario getUserSelected() {
		return userSelected;
	}

	public void setUserSelected(Usuario userSelected) {
		this.userSelected = userSelected;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public UsuarioSucursal getUsuarioSucursalSelected() {
		return usuarioSucursalSelected;
	}

	public void setUsuarioSucursalSelected(UsuarioSucursal usuarioSucursalSelected) {
		this.usuarioSucursalSelected = usuarioSucursalSelected;
	}

	public UsuarioSucursal getSuc() {
		return suc;
	}

	public void setSuc(UsuarioSucursal suc) {
		this.suc = suc;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public boolean isValidaUsuario() {
		return validaUsuario;
	}

	public void setValidaUsuario(boolean validaUsuario) {
		this.validaUsuario = validaUsuario;
	}

	public String getTituloDialog() {
		return tituloDialog;
	}

	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	
	
	
}
