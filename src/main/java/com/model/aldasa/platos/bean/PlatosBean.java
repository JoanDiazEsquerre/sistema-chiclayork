package com.model.aldasa.platos.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.context.PrimeRequestContext;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.Plato;
import com.model.aldasa.entity.Producto;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.PlatoService;
import com.model.aldasa.service.ProductoService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.TipoPlatoType;

@ManagedBean
@ViewScoped
public class PlatosBean extends BaseBean{
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{platoService}")
	private PlatoService platoService;
	
	@ManagedProperty(value = "#{productoService}")
	private ProductoService productoService;
	
	
	private Plato platoMenu,platoCarta,platoEntrada, platoBebida, platoSelectedMant, platoPlatoBandera, platoGuarniciones, platoPostres, platoOtros;
	private Producto productoSelected;
	
	private Integer idMod;
	
	private List<Plato> lstPlatoMenu,lstPlatoCarta, lstPlatoEntrada, lstPlatoBebida, lstPlatoBandera, lstPlatoGuarniciones, lstPlatoPostres, lstPlatoOtros;

	private List<Producto> lstProducto ;
	
	private LazyDataModel<Plato> lazyPlatosMant;
	private LazyDataModel<Plato> lazyPlatoEntradaEnCarta;
	private LazyDataModel<Plato> lazyPlatoMenuEnCarta;
	private LazyDataModel<Plato> lazyPlatoCartaEnCarta;
	private LazyDataModel<Plato> lazyPlatoBebidaEnCarta;
	private LazyDataModel<Plato> lazyPlatoBanderaEnCarta;
	private LazyDataModel<Plato> lazyPlatoGuarnicionesEnCarta;
	private LazyDataModel<Plato> lazyPlatoPostresEnCarta;
	private LazyDataModel<Plato> lazyPlatoOtrosEnCarta;

	
	private String tipoPlato;

	
	private Plato platoNew;
	
	
	@PostConstruct
	public void init() {
		listarPlatos();
		iniciarLazyPlatosEntradaEnCarta();
		iniciarLazyPlatosMenuEnCarta();
		iniciarLazyPlatosCartaEnCarta();
		iniciarLazyPlatosBebidaEnCarta();
		iniciarLazyPlatosBanderaEnCarta();
		iniciarLazyPlatosGuarnicionesEnCarta();
		iniciarLazyPlatosPostresEnCarta();
		iniciarLazyPlatosOtrosEnCarta();
		lstProducto = productoService.findByEstado(true);
	}
	
	public void quitarEnCarta() {
		List<Plato> lstPlatoAuto = platoService.findByEstadoAndEnCarta(true, "SI");
		for(Plato p : lstPlatoAuto) {
			p.setEnCarta("NO");
			platoService.save(p);
		}
		
		listarPlatos();
		addInfoMessage("Se quitaron todos los items correctamente.");
	}
	
	public void registroAutomatico() {
		List<Plato> lstPlatoAuto = platoService.findByEstadoAndAutomatico(true, "SI");
		for(Plato p : lstPlatoAuto) {
			p.setEnCarta("SI");
			platoService.save(p);
		}
		
		listarPlatos();
		addInfoMessage("Se agregaron los registros automaticos correctamente.");
	}
	
	public void accionEliminar(Plato plato) {
		plato.setEstado(false);
		platoService.save(plato);
		listarPlatos();
		addInfoMessage("Se eliminó correctamente.");
	}
	
	public void accionModificar(Plato plato) {
		idMod = plato.getId();
		platoNew = new Plato();
		platoNew.setNombre(plato.getNombre());
		platoNew.setPrecio(plato.getPrecio());
		platoNew.setEstado(plato.getEstado());
		platoNew.setAutomatico(plato.getAutomatico());
		platoNew.setEnCarta(plato.getEnCarta());
		platoNew.setProducto(plato.getProducto());
		
	}
	
	public void agregarPlato(Plato plato) {
		if(plato.getEnCarta().equals("SI")) {
			addErrorMessage("El plato ya se encuentra agregado.");
			return;
		}
		plato.setEnCarta("SI");
		platoService.save(plato);
		listarPlatos();
		addInfoMessage("Se agrego correctamente.");
		
	}
	
	public void eliminarPlatoMenu(Plato plato) {
		plato.setEnCarta("NO");
		platoService.save(plato);
		listarPlatos();
		addInfoMessage("Se eliminó correctamente el item.");
		
	}
	
	public void listarPlatosEnCarta() {
//		lstPlatoEntradaEnCarta = platoService.findByTipoAndEstadoAndEnCarta(tipoPlato, true, "NO");
	}
	
	public void inicarDatosPlato() {
		idMod=null;
		
		platoNew = new Plato();
		platoNew.setEstado(true);
		platoNew.setAutomatico("NO");
		platoNew.setEnCarta("NO");
//		platoNew.setTipo("Entrada");
//		platoNew.setProducto(lstProducto.get(0));
		
		iniciarLazyPlatosMant();
	}
	
	
	public void listarPlatos() {
		lstPlatoEntrada = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.ENTRADA.getNombre(), true);
		lstPlatoMenu = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.MENU.getNombre(), true);
		lstPlatoCarta = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.CARTA.getNombre(), true);
		lstPlatoBebida = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.BEBIDA.getNombre(), true); 
		lstPlatoBandera = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.PLATO_BANDERA.getNombre(), true); 
		lstPlatoGuarniciones = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.GUARNICIONES.getNombre(), true); 
		lstPlatoPostres = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.POSTRES.getNombre(), true); 
		lstPlatoOtros = platoService.findByProductoDescripcionAndEstado(TipoPlatoType.OTROS.getNombre(), true); 
	}
	

	public void savePlato() {
		if(platoNew.getNombre().equals("")) {
			addErrorMessage("Ingresar nombre del plato");
			return;
		}
		
		if(platoNew.getPrecio() == null) {
			addErrorMessage("Ingresar precio del plato");
			return;
		}
		
		
		platoNew.setProducto(productoSelected);
		
		if(idMod == null) {
			Plato encuentraPlato = platoService.findByNombreAndEstadoAndProducto(platoNew.getNombre(), true, platoNew.getProducto());
			if(encuentraPlato != null) {
				addErrorMessage("El plato ya se encuentra registrado"); 
			}else {
				platoService.save(platoNew);
				listarPlatos();
				inicarDatosPlato();
				addInfoMessage("Se registro el plato correctamente."); 
				
			}
		}else {
			Plato encuentraPlato = platoService.findByNombreException(platoNew.getNombre(), true, platoNew.getProducto().getId(),idMod);
			if(encuentraPlato!=null) {
				addErrorMessage("El plato ya se encuentra registrado"); 
			}else {
				platoNew.setId(idMod);
				platoService.save(platoNew);
				listarPlatos();
				inicarDatosPlato();
				addInfoMessage("Se modificó el plato correctamente."); 
			}
		}
		
		
	}

	public Converter getConversorPlatoEntrada() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoEntrada) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorProducto() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Producto c = null;
                    for (Producto si : lstProducto) {
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
                    return ((Producto) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPlatoMenu() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoMenu) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPlatoCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoCarta) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPlatoBebida() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoBebida) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPlatoBandera() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoBandera) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorGuarniciones() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoGuarniciones) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPostres() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoPostres) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorOtros() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoOtros) {
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
                    return ((Plato) value).getId() + "";
                }
            }
        };
    }
	
	public void iniciarLazyPlatosMant() {

		lazyPlatosMant = new LazyDataModel<Plato>() {
			private List<Plato> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Plato getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Plato plato : datasource) {
                    if (plato.getId() == intRowKey) {
                        return plato;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Plato plato) {
                return String.valueOf(plato.getId());
            }

			@Override
			public List<Plato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("id").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }        
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
                if(productoSelected==null) {
                	productoSelected = lstProducto.get(0);
                }
                
                Page<Plato> pagePlato= platoService.findByEstadoAndProducto(true, productoSelected, pageable);
                
                
                setRowCount((int) pagePlato.getTotalElements());
                return datasource = pagePlato.getContent();
            }
		};
	}
	
	public void iniciarLazyPlatosEntradaEnCarta() {

		lazyPlatoEntradaEnCarta = new LazyDataModel<Plato>() {
			private List<Plato> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Plato getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Plato plato : datasource) {
                    if (plato.getId() == intRowKey) {
                        return plato;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Plato plato) {
                return String.valueOf(plato.getId());
            }

			@Override
			public List<Plato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("id").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }        
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
                Page<Plato> pagePlato= platoService.findByProductoDescripcionAndEstadoAndEnCarta("Entrada", true, "SI", pageable);
                
                
                setRowCount((int) pagePlato.getTotalElements());
                return datasource = pagePlato.getContent();
            }
		};
	}
	
	public void iniciarLazyPlatosMenuEnCarta() {

		lazyPlatoMenuEnCarta = new LazyDataModel<Plato>() {
			private List<Plato> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Plato getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Plato plato : datasource) {
                    if (plato.getId() == intRowKey) {
                        return plato;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Plato plato) {
                return String.valueOf(plato.getId());
            }

			@Override
			public List<Plato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("id").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }        
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
                Page<Plato> pagePlato= platoService.findByProductoDescripcionAndEstadoAndEnCarta("Menu", true, "SI", pageable);
                
                
                setRowCount((int) pagePlato.getTotalElements());
                return datasource = pagePlato.getContent();
            }
		};
	}
	
	public void iniciarLazyPlatosCartaEnCarta() {

		lazyPlatoCartaEnCarta = new LazyDataModel<Plato>() {
			private List<Plato> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Plato getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Plato plato : datasource) {
                    if (plato.getId() == intRowKey) {
                        return plato;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Plato plato) {
                return String.valueOf(plato.getId());
            }

			@Override
			public List<Plato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("id").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }        
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
                Page<Plato> pagePlato= platoService.findByProductoDescripcionAndEstadoAndEnCarta("Carta", true, "SI", pageable);
                
                
                setRowCount((int) pagePlato.getTotalElements());
                return datasource = pagePlato.getContent();
            }
		};
	}
	
	public void iniciarLazyPlatosBebidaEnCarta() {

		lazyPlatoBebidaEnCarta = new LazyDataModel<Plato>() {
			private List<Plato> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Plato getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Plato plato : datasource) {
                    if (plato.getId() == intRowKey) {
                        return plato;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Plato plato) {
                return String.valueOf(plato.getId());
            }

			@Override
			public List<Plato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("id").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }        
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
                Page<Plato> pagePlato= platoService.findByProductoDescripcionAndEstadoAndEnCarta("Bebida", true, "SI", pageable);
                
                
                setRowCount((int) pagePlato.getTotalElements());
                return datasource = pagePlato.getContent();
            }
		};
	}
	
	public void iniciarLazyPlatosBanderaEnCarta() {

		lazyPlatoBanderaEnCarta = new LazyDataModel<Plato>() {
			private List<Plato> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Plato getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Plato plato : datasource) {
                    if (plato.getId() == intRowKey) {
                        return plato;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Plato plato) {
                return String.valueOf(plato.getId());
            }

			@Override
			public List<Plato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("id").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }        
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
                Page<Plato> pagePlato= platoService.findByProductoDescripcionAndEstadoAndEnCarta("Plato Bandera", true, "SI", pageable);
                
                
                setRowCount((int) pagePlato.getTotalElements());
                return datasource = pagePlato.getContent();
            }
		};
	}
	
	public void iniciarLazyPlatosGuarnicionesEnCarta() {

		lazyPlatoGuarnicionesEnCarta = new LazyDataModel<Plato>() {
			private List<Plato> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Plato getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Plato plato : datasource) {
                    if (plato.getId() == intRowKey) {
                        return plato;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Plato plato) {
                return String.valueOf(plato.getId());
            }

			@Override
			public List<Plato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("id").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }        
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
                Page<Plato> pagePlato= platoService.findByProductoDescripcionAndEstadoAndEnCarta("Guarniciones", true, "SI", pageable);
                
                
                setRowCount((int) pagePlato.getTotalElements());
                return datasource = pagePlato.getContent();
            }
		};
	}
	
	public void iniciarLazyPlatosPostresEnCarta() {

		lazyPlatoPostresEnCarta = new LazyDataModel<Plato>() {
			private List<Plato> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Plato getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Plato plato : datasource) {
                    if (plato.getId() == intRowKey) {
                        return plato;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Plato plato) {
                return String.valueOf(plato.getId());
            }

			@Override
			public List<Plato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("id").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }        
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
                Page<Plato> pagePlato= platoService.findByProductoDescripcionAndEstadoAndEnCarta("Postres", true, "SI", pageable);
                
                
                setRowCount((int) pagePlato.getTotalElements());
                return datasource = pagePlato.getContent();
            }
		};
	}
	
	public void iniciarLazyPlatosOtrosEnCarta() {

		lazyPlatoOtrosEnCarta = new LazyDataModel<Plato>() {
			private List<Plato> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Plato getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Plato plato : datasource) {
                    if (plato.getId() == intRowKey) {
                        return plato;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Plato plato) {
                return String.valueOf(plato.getId());
            }

			@Override
			public List<Plato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("id").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }        
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
                Page<Plato> pagePlato= platoService.findByProductoDescripcionAndEstadoAndEnCarta("Otros", true, "SI", pageable);
                
                
                setRowCount((int) pagePlato.getTotalElements());
                return datasource = pagePlato.getContent();
            }
		};
	}

	
	
	
	
	
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public Plato getPlatoMenu() {
		return platoMenu;
	}
	public void setPlatoMenu(Plato platoMenu) {
		this.platoMenu = platoMenu;
	}
	public Plato getPlatoCarta() {
		return platoCarta;
	}
	public void setPlatoCarta(Plato platoCarta) {
		this.platoCarta = platoCarta;
	}
	public Plato getPlatoEntrada() {
		return platoEntrada;
	}
	public void setPlatoEntrada(Plato platoEntrada) {
		this.platoEntrada = platoEntrada;
	}
	public List<Plato> getLstPlatoMenu() {
		return lstPlatoMenu;
	}
	public void setLstPlatoMenu(List<Plato> lstPlatoMenu) {
		this.lstPlatoMenu = lstPlatoMenu;
	}
	public List<Plato> getLstPlatoCarta() {
		return lstPlatoCarta;
	}
	public void setLstPlatoCarta(List<Plato> lstPlatoCarta) {
		this.lstPlatoCarta = lstPlatoCarta;
	}
	public List<Plato> getLstPlatoEntrada() {
		return lstPlatoEntrada;
	}
	public void setLstPlatoEntrada(List<Plato> lstPlatoEntrada) {
		this.lstPlatoEntrada = lstPlatoEntrada;
	}
	public String getTipoPlato() {
		return tipoPlato;
	}
	public void setTipoPlato(String tipoPlato) {
		this.tipoPlato = tipoPlato;
	}
	public PlatoService getPlatoService() {
		return platoService;
	}
	public void setPlatoService(PlatoService platoService) {
		this.platoService = platoService;
	}
	public Integer getIdMod() {
		return idMod;
	}
	public void setIdMod(Integer idMod) {
		this.idMod = idMod;
	}
	public Plato getPlatoBebida() {
		return platoBebida;
	}
	public void setPlatoBebida(Plato platoBebida) {
		this.platoBebida = platoBebida;
	}
	public List<Plato> getLstPlatoBebida() {
		return lstPlatoBebida;
	}
	public void setLstPlatoBebida(List<Plato> lstPlatoBebida) {
		this.lstPlatoBebida = lstPlatoBebida;
	}
	public Plato getPlatoNew() {
		return platoNew;
	}
	public void setPlatoNew(Plato platoNew) {
		this.platoNew = platoNew;
	}
	public LazyDataModel<Plato> getLazyPlatosMant() {
		return lazyPlatosMant;
	}
	public void setLazyPlatosMant(LazyDataModel<Plato> lazyPlatosMant) {
		this.lazyPlatosMant = lazyPlatosMant;
	}
	public Plato getPlatoSelectedMant() {
		return platoSelectedMant;
	}
	public void setPlatoSelectedMant(Plato platoSelectedMant) {
		this.platoSelectedMant = platoSelectedMant;
	}
	public LazyDataModel<Plato> getLazyPlatoEntradaEnCarta() {
		return lazyPlatoEntradaEnCarta;
	}
	public void setLazyPlatoEntradaEnCarta(LazyDataModel<Plato> lazyPlatoEntradaEnCarta) {
		this.lazyPlatoEntradaEnCarta = lazyPlatoEntradaEnCarta;
	}
	public LazyDataModel<Plato> getLazyPlatoMenuEnCarta() {
		return lazyPlatoMenuEnCarta;
	}
	public void setLazyPlatoMenuEnCarta(LazyDataModel<Plato> lazyPlatoMenuEnCarta) {
		this.lazyPlatoMenuEnCarta = lazyPlatoMenuEnCarta;
	}
	public LazyDataModel<Plato> getLazyPlatoCartaEnCarta() {
		return lazyPlatoCartaEnCarta;
	}
	public void setLazyPlatoCartaEnCarta(LazyDataModel<Plato> lazyPlatoCartaEnCarta) {
		this.lazyPlatoCartaEnCarta = lazyPlatoCartaEnCarta;
	}
	public LazyDataModel<Plato> getLazyPlatoBebidaEnCarta() {
		return lazyPlatoBebidaEnCarta;
	}
	public void setLazyPlatoBebidaEnCarta(LazyDataModel<Plato> lazyPlatoBebidaEnCarta) {
		this.lazyPlatoBebidaEnCarta = lazyPlatoBebidaEnCarta;
	}
	public ProductoService getProductoService() {
		return productoService;
	}
	public void setProductoService(ProductoService productoService) {
		this.productoService = productoService;
	}
	public List<Producto> getLstProducto() {
		return lstProducto;
	}
	public void setLstProducto(List<Producto> lstProducto) {
		this.lstProducto = lstProducto;
	}
	public Producto getProductoSelected() {
		return productoSelected;
	}
	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
	}
	public Plato getPlatoPlatoBandera() {
		return platoPlatoBandera;
	}
	public void setPlatoPlatoBandera(Plato platoPlatoBandera) {
		this.platoPlatoBandera = platoPlatoBandera;
	}
	public List<Plato> getLstPlatoBandera() {
		return lstPlatoBandera;
	}
	public void setLstPlatoBandera(List<Plato> lstPlatoBandera) {
		this.lstPlatoBandera = lstPlatoBandera;
	}
	public LazyDataModel<Plato> getLazyPlatoBanderaEnCarta() {
		return lazyPlatoBanderaEnCarta;
	}
	public void setLazyPlatoBanderaEnCarta(LazyDataModel<Plato> lazyPlatoBanderaEnCarta) {
		this.lazyPlatoBanderaEnCarta = lazyPlatoBanderaEnCarta;
	}
	public Plato getPlatoGuarniciones() {
		return platoGuarniciones;
	}
	public void setPlatoGuarniciones(Plato platoGuarniciones) {
		this.platoGuarniciones = platoGuarniciones;
	}
	public List<Plato> getLstPlatoGuarniciones() {
		return lstPlatoGuarniciones;
	}
	public void setLstPlatoGuarniciones(List<Plato> lstPlatoGuarniciones) {
		this.lstPlatoGuarniciones = lstPlatoGuarniciones;
	}
	public LazyDataModel<Plato> getLazyPlatoGuarnicionesEnCarta() {
		return lazyPlatoGuarnicionesEnCarta;
	}
	public void setLazyPlatoGuarnicionesEnCarta(LazyDataModel<Plato> lazyPlatoGuarnicionesEnCarta) {
		this.lazyPlatoGuarnicionesEnCarta = lazyPlatoGuarnicionesEnCarta;
	}
	public Plato getPlatoPostres() {
		return platoPostres;
	}
	public void setPlatoPostres(Plato platoPostres) {
		this.platoPostres = platoPostres;
	}
	public List<Plato> getLstPlatoPostres() {
		return lstPlatoPostres;
	}
	public void setLstPlatoPostres(List<Plato> lstPlatoPostres) {
		this.lstPlatoPostres = lstPlatoPostres;
	}
	public LazyDataModel<Plato> getLazyPlatoPostresEnCarta() {
		return lazyPlatoPostresEnCarta;
	}
	public void setLazyPlatoPostresEnCarta(LazyDataModel<Plato> lazyPlatoPostresEnCarta) {
		this.lazyPlatoPostresEnCarta = lazyPlatoPostresEnCarta;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Plato getPlatoOtros() {
		return platoOtros;
	}
	public void setPlatoOtros(Plato platoOtros) {
		this.platoOtros = platoOtros;
	}
	public List<Plato> getLstPlatoOtros() {
		return lstPlatoOtros;
	}
	public void setLstPlatoOtros(List<Plato> lstPlatoOtros) {
		this.lstPlatoOtros = lstPlatoOtros;
	}

	public LazyDataModel<Plato> getLazyPlatoOtrosEnCarta() {
		return lazyPlatoOtrosEnCarta;
	}

	public void setLazyPlatoOtrosEnCarta(LazyDataModel<Plato> lazyPlatoOtrosEnCarta) {
		this.lazyPlatoOtrosEnCarta = lazyPlatoOtrosEnCarta;
	}
	
}
