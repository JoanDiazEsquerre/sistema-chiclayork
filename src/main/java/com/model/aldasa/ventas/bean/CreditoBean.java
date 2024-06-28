package com.model.aldasa.ventas.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.AtencionMesa;
import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Credito;
import com.model.aldasa.entity.DetalleAtencionMesa;
import com.model.aldasa.entity.DetalleCredito;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.Persona;
import com.model.aldasa.entity.Plato;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.ClienteService;
import com.model.aldasa.service.CreditoService;
import com.model.aldasa.service.DetalleCreditoService;
import com.model.aldasa.service.PersonaService;
import com.model.aldasa.service.PlatoService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.TipoPlatoType;


@ManagedBean
@ViewScoped
public class CreditoBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{personaService}")
	private PersonaService personaService;
	
	@ManagedProperty(value = "#{creditoService}")
	private CreditoService creditoService;
	
	@ManagedProperty(value = "#{platoService}")
	private PlatoService platoService;
	
	@ManagedProperty(value = "#{detalleCreditoService}")
	private DetalleCreditoService detalleCreditoService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	private LazyDataModel<Credito> lstCreditoLazy;
	
	private List<Persona> lstPerson = new ArrayList<>();
	private List<Persona> lstPersonCredito = new ArrayList<>();
	List<Credito> lstCobroCreditoMasivo = new ArrayList<>();
	private List<Plato> lstEntradaEnCarta,lstMenuEnCarta, lstCartaEnCarta, lstBebidaEnCarta, lstPlatoBanderaEnCarta, lstGuarnicionesEnCarta, lstPostresEnCarta, lstOtrosEnCarta;
	private List<DetalleCredito> lstDetalleCreditoSelected = new ArrayList<>();
	
	private Persona person ;
	private Persona personPorCobrar ;
	private Persona personSelected;
	private Credito creditoSelected;
	private Plato entradaSelected, cartaSelected , menuSelected, bebidaSelected, platoBanderaSelected, guarnicionesSelected, postresSelected, otrosSelected;
	
	private String estadoCobrar= "Pendiente";
	private Date fecha;
	private String tituloDialog;
	private BigDecimal sumaTotalCreditos;
	private String nombres, apellidos, dni;
	
	@PostConstruct
	public void init() {
		iniciarLazy();
		lstPerson = personaService.findByEstado(true);
		lstPersonCredito = personaService.findByEstadoAndCredito(true, true);
		fecha = new Date();
		listarPlatosEnCarta();
	}
	
	public void limpiarDatos() {
		person = null;
		nombres = "";
		apellidos = "";
		dni = "";
		
		lstPersonCredito = personaService.findByEstadoAndCredito(true, true);
		lstPerson = personaService.findByEstado(true);
	}
	
	public void setearDatosPersona() {
		if(person==null) {
			nombres="";
			apellidos="";
			dni="";
		}else {
			nombres=person.getNombres();
			apellidos=person.getApellidos();
			dni=person.getDni();
		}
		
	}
	
	public void validaCobroMasivo() {
		if(personPorCobrar==null) {
			addErrorMessage("Seleccionar Persona");
			return;
		}
		
		lstCobroCreditoMasivo = creditoService.findAllByEstadoAndPersona("Pendiente", personPorCobrar);
		
		if(lstCobroCreditoMasivo.isEmpty()) {
			addErrorMessage("No hay nada que cobrar para " + personPorCobrar.getNombres() + " " + personPorCobrar.getApellidos());
			return;
		}
		
		PrimeFaces.current().executeScript("PF('cobraCreditoMasivo').show()");
		
	}
	
	public void cobraCreditoMasivo() {
		
		if(!lstCobroCreditoMasivo.isEmpty()) {
			for(Credito d:lstCobroCreditoMasivo) {
				d.setEstado("Cobrado");
				creditoService.save(d);
			}
			addInfoMessage("Créditos cobrados correctamente.");
		}else {
			addErrorMessage("No hay nada que cobrar para " + personPorCobrar.getNombres() + " " + personPorCobrar.getApellidos());
		} 
		
	}
	
	public void cobraCredito() {
		creditoSelected.setEstado("Cobrado");
		creditoService.save(creditoSelected);
		addInfoMessage("Crédito Cobrado correctamente.");
	}
	
	public void eliminaCredito() {
		creditoSelected.setEstado("Anulado");
		creditoService.save(creditoSelected);
		addInfoMessage("Crédito Anulado correctamente.");
	}
	
	public void saveDetalleCredito() {
		if(creditoSelected.getFecha()==null) {
			addErrorMessage("Seleccionar fecha."); 
			return;
		}
		
		if(creditoSelected.getPersona()==null) {
			addErrorMessage("Seleccionar persona."); 
			return;
		}
		
		if(lstDetalleCreditoSelected.isEmpty()) {
			addErrorMessage("La lista de pedido esta vacío."); 
			return;
		}
		if(creditoSelected.getId()==null ) {
			creditoSelected.setUserCrea(navegacionBean.getUsuarioLogin());
			creditoSelected.setFechaCrea(new Date());
			creditoSelected.setMontoPagado(BigDecimal.ZERO);
		}
	
		creditoService.save(creditoSelected);
		
		
		List<DetalleCredito> lstDetallesActuales = detalleCreditoService.findByCreditoAndEstado(creditoSelected, true);
		if(!lstDetallesActuales.isEmpty()) {
			for(DetalleCredito d:lstDetallesActuales) {
				boolean encuentra = false;
				
				for(DetalleCredito d2 : lstDetalleCreditoSelected) {
					if(d2.getId()!=null) {
						if(d.getId().equals(d2.getId())) {
							encuentra = true;
						}
					}
				}
				
				if(encuentra==false) {
					d.setEstado(false);
					detalleCreditoService.save(d);
				}
				
			}
		}
		
		
		for(DetalleCredito detalle : lstDetalleCreditoSelected) {
			detalleCreditoService.save(detalle);
		}
		addInfoMessage("Se guardó correctamente el pedido.");
	}
	
	public void newCredito() {
		tituloDialog = "NUEVO CRÉDITO";
		creditoSelected = new Credito();
		creditoSelected.setFecha(new Date());
		creditoSelected.setPersona(null);
		creditoSelected.setTotal(BigDecimal.ZERO);
		creditoSelected.setEstado("Pendiente");
		lstDetalleCreditoSelected = new ArrayList<>();
	}
	
	public void updateCredito() {
		tituloDialog = "MODIFICAR CRÉDITO";
		lstDetalleCreditoSelected = detalleCreditoService.findByCreditoAndEstado(creditoSelected, true);
		if(!lstDetalleCreditoSelected.isEmpty()) {
			calcularTotal();
		}
	}
	
	public void aumentarDetalle(DetalleCredito detalle) {
		detalle.setCantidad(detalle.getCantidad().add(BigDecimal.ONE));
		detalle.setTotal(detalle.getPrecioUnitario().multiply(detalle.getCantidad()));
		calcularTotal();
	}
	
	public void disminuyeDetalle(DetalleCredito detalle) {
		if(detalle.getCantidad().compareTo(BigDecimal.ONE) >0) {
			detalle.setCantidad(detalle.getCantidad().subtract(BigDecimal.ONE));
			detalle.setTotal(detalle.getPrecioUnitario().multiply(detalle.getCantidad()));
			calcularTotal();
		}
		
		
	}
	
	public void eliminarDetalleCredito(int index) {
		lstDetalleCreditoSelected.remove(index);
		calcularTotal();
		addInfoMessage("Detalle eliminado");
		
	}
	
	public void savePerson() {
		
		if(person != null) {
			if(person.isCredito()) {
				addErrorMessage("La persona ya se encuentra en la relación.");
				return;
			}
			person.setCredito(true);
			personaService.save(person);
			
		}else {
			
			if(nombres.equals("")) {
				addErrorMessage("Ingrese nombres.");
				return;
			}
			if(apellidos.equals("")) {
				addErrorMessage("Ingrese apellidos.");
				return;
			}
			if(dni.equals("")) {
				addErrorMessage("Ingrese DNI.");
				return;
			}
			for(Persona p : lstPerson) {
				if(p.getDni().equals(dni)) {
					addErrorMessage("La persona ya existe.");
					return;
				}
			}
			Persona persona = new Persona();
			persona.setNombres(nombres);
			persona.setApellidos(apellidos);
			persona.setDni(dni);
			persona.setTelefono("");
			persona.setDireccion("");
			persona.setFechaNacimiento(new Date());
			persona.setEstado(true);
			persona.setCredito(true);
			
			personaService.save(persona);
		}
		
		limpiarDatos();
		addInfoMessage("Se guardó la persona correctamente.");
	
	}
	
	public void deletePerson() {
		
		personSelected.setCredito(false);
		personaService.save(personSelected);
		
		lstPersonCredito = personaService.findByEstadoAndCredito(true, true);
		lstPerson = personaService.findByEstado(true);
		
		addInfoMessage("Se elimió la persona correctamente.");
	}
	
	public void listarPlatosEnCarta() {
		lstEntradaEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.ENTRADA.getNombre(), true, "SI");
		lstMenuEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.MENU.getNombre(), true, "SI");
		lstCartaEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.CARTA.getNombre(), true, "SI");
		lstBebidaEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.BEBIDA.getNombre(), true, "SI"); 
		lstPlatoBanderaEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.PLATO_BANDERA.getNombre(), true, "SI"); 
		lstGuarnicionesEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.GUARNICIONES.getNombre(), true, "SI"); 
		lstPostresEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.POSTRES.getNombre(), true, "SI"); 
		lstOtrosEnCarta = platoService.findByProductoDescripcionAndEstadoAndEnCarta(TipoPlatoType.OTROS.getNombre(), true, "SI"); 
	
	}
	
	public void agregarDetalle(Plato plato) {
		if(plato != null) {
			DetalleCredito detalle = new DetalleCredito();
			detalle.setCredito(creditoSelected);
			detalle.setPlato(plato); 
			detalle.setCantidad(BigDecimal.ONE); 
			detalle.setPrecioUnitario(plato.getPrecio());
			detalle.setTotal(plato.getPrecio());
			detalle.setObservacion("");
			detalle.setEstado(true);
			lstDetalleCreditoSelected.add(detalle);
			calcularTotal();
		}
		
		entradaSelected=null;
		menuSelected=null;
		cartaSelected=null;
		bebidaSelected=null;
		platoBanderaSelected=null;
		guarnicionesSelected=null;
		postresSelected=null;
		otrosSelected=null;
		
	}
	
	public void calcularTotal() {
		BigDecimal totalDetalle = BigDecimal.ZERO;
		if(!lstDetalleCreditoSelected.isEmpty()) {
			for(DetalleCredito detalle : lstDetalleCreditoSelected) {
				totalDetalle = totalDetalle.add(detalle.getTotal());
			}
		}
		
		creditoSelected.setTotal(totalDetalle);
	}
	
	public void iniciarLazy() {
		lstCreditoLazy = new LazyDataModel<Credito>() {
			private List<Credito> datasource;

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}

			@Override
			public Credito getRowData(String rowKey) {
				int intRowKey = Integer.parseInt(rowKey);
				for (Credito credito : datasource) {
					if (credito.getId() == intRowKey) {
						return credito;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Credito credito) {
				return String.valueOf(credito.getId());
			}

			@Override
			public List<Credito> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
			
				Sort sort = Sort.by("id").descending();
				if (sortBy != null) {
					for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
						System.out.println(entry.getKey() + "/" + entry.getValue());
						if (entry.getValue().getOrder().isAscending()) {
							sort = Sort.by(entry.getKey()).descending();
						} else {
							sort = Sort.by(entry.getKey()).ascending();

						}

					}
				}
				
				Pageable pageable = PageRequest.of(first / pageSize, pageSize, sort);
				
				Page<Credito> pageCredito =null;
				
				if(personPorCobrar!=null) {
					pageCredito = creditoService.findAllByEstadoAndPersona(estadoCobrar,personPorCobrar, pageable);
					sumaTotalCreditos = creditoService.sumaTotal(estadoCobrar, personPorCobrar.getId());
				}else {
					pageCredito = creditoService.findAllByEstado(estadoCobrar, pageable);
					sumaTotalCreditos = creditoService.sumaTotal(estadoCobrar);
				}
				
				if(sumaTotalCreditos==null) {
					sumaTotalCreditos = BigDecimal.ZERO;
                }
				
				setRowCount((int) pageCredito.getTotalElements());
				return datasource = pageCredito.getContent();
			}
		};
	}

	
	public List<Persona> completePerson(String query) {
        List<Persona> lista = new ArrayList<>();
        for (Persona c : lstPerson) {
            if (c.getApellidos().toUpperCase().contains(query.toUpperCase()) || c.getNombres().toUpperCase().contains(query.toUpperCase()) || c.getDni().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	public List<Persona> completePersonCredito(String query) {
        List<Persona> lista = new ArrayList<>();
        for (Persona c : lstPersonCredito) {
            if (c.getApellidos().toUpperCase().contains(query.toUpperCase()) || c.getNombres().toUpperCase().contains(query.toUpperCase()) || c.getDni().toUpperCase().contains(query.toUpperCase())) {
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
                    for (Persona si : lstPerson) {
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
	
	public Converter getConversorPersonCredito() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Persona c = null;
                    for (Persona si : lstPersonCredito) {
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
	
	public Converter getConversorEntradaEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstEntradaEnCarta) {
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
	
	public Converter getConversorMenuEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstMenuEnCarta) {
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
	
	public Converter getConversorCartaEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstCartaEnCarta) {
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
	
	public Converter getConversorBebidaEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstBebidaEnCarta) {
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
	
	public Converter getConversorPlatoBanderaEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPlatoBanderaEnCarta) {
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
	
	public Converter getConversorGuarnicionesEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstGuarnicionesEnCarta) {
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
	
	public Converter getConversorPostresEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstPostresEnCarta) {
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
	
	public Converter getConversorOtrosEnCarta() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Plato c = null;
                    for (Plato si : lstOtrosEnCarta) {
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
	
	
	
	public PersonaService getPersonaService() {
		return personaService;
	}
	public void setPersonaService(PersonaService personaService) {
		this.personaService = personaService;
	}
	public List<Persona> getLstPerson() {
		return lstPerson;
	}
	public void setLstPerson(List<Persona> lstPerson) {
		this.lstPerson = lstPerson;
	}
	public Persona getPerson() {
		return person;
	}
	public void setPerson(Persona person) {
		this.person = person;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getEstadoCobrar() {
		return estadoCobrar;
	}
	public void setEstadoCobrar(String estadoCobrar) {
		this.estadoCobrar = estadoCobrar;
	}
	public Persona getPersonSelected() {
		return personSelected;
	}
	public void setPersonSelected(Persona personSelected) {
		this.personSelected = personSelected;
	}
	public Credito getCreditoSelected() {
		return creditoSelected;
	}
	public void setCreditoSelected(Credito creditoSelected) {
		this.creditoSelected = creditoSelected;
	}
	public LazyDataModel<Credito> getLstCreditoLazy() {
		return lstCreditoLazy;
	}
	public void setLstCreditoLazy(LazyDataModel<Credito> lstCreditoLazy) {
		this.lstCreditoLazy = lstCreditoLazy;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public List<Persona> getLstPersonCredito() {
		return lstPersonCredito;
	}
	public void setLstPersonCredito(List<Persona> lstPersonCredito) {
		this.lstPersonCredito = lstPersonCredito;
	}
	public CreditoService getCreditoService() {
		return creditoService;
	}
	public void setCreditoService(CreditoService creditoService) {
		this.creditoService = creditoService;
	}
	public PlatoService getPlatoService() {
		return platoService;
	}
	public void setPlatoService(PlatoService platoService) {
		this.platoService = platoService;
	}
	public List<Plato> getLstEntradaEnCarta() {
		return lstEntradaEnCarta;
	}
	public void setLstEntradaEnCarta(List<Plato> lstEntradaEnCarta) {
		this.lstEntradaEnCarta = lstEntradaEnCarta;
	}
	public Plato getEntradaSelected() {
		return entradaSelected;
	}
	public void setEntradaSelected(Plato entradaSelected) {
		this.entradaSelected = entradaSelected;
	}
	public List<DetalleCredito> getLstDetalleCreditoSelected() {
		return lstDetalleCreditoSelected;
	}
	public void setLstDetalleCreditoSelected(List<DetalleCredito> lstDetalleCreditoSelected) {
		this.lstDetalleCreditoSelected = lstDetalleCreditoSelected;
	}
	public List<Plato> getLstMenuEnCarta() {
		return lstMenuEnCarta;
	}
	public void setLstMenuEnCarta(List<Plato> lstMenuEnCarta) {
		this.lstMenuEnCarta = lstMenuEnCarta;
	}
	public List<Plato> getLstCartaEnCarta() {
		return lstCartaEnCarta;
	}
	public void setLstCartaEnCarta(List<Plato> lstCartaEnCarta) {
		this.lstCartaEnCarta = lstCartaEnCarta;
	}
	public List<Plato> getLstBebidaEnCarta() {
		return lstBebidaEnCarta;
	}
	public void setLstBebidaEnCarta(List<Plato> lstBebidaEnCarta) {
		this.lstBebidaEnCarta = lstBebidaEnCarta;
	}
	public List<Plato> getLstPlatoBanderaEnCarta() {
		return lstPlatoBanderaEnCarta;
	}
	public void setLstPlatoBanderaEnCarta(List<Plato> lstPlatoBanderaEnCarta) {
		this.lstPlatoBanderaEnCarta = lstPlatoBanderaEnCarta;
	}
	public List<Plato> getLstGuarnicionesEnCarta() {
		return lstGuarnicionesEnCarta;
	}
	public void setLstGuarnicionesEnCarta(List<Plato> lstGuarnicionesEnCarta) {
		this.lstGuarnicionesEnCarta = lstGuarnicionesEnCarta;
	}
	public List<Plato> getLstPostresEnCarta() {
		return lstPostresEnCarta;
	}
	public void setLstPostresEnCarta(List<Plato> lstPostresEnCarta) {
		this.lstPostresEnCarta = lstPostresEnCarta;
	}
	public List<Plato> getLstOtrosEnCarta() {
		return lstOtrosEnCarta;
	}
	public void setLstOtrosEnCarta(List<Plato> lstOtrosEnCarta) {
		this.lstOtrosEnCarta = lstOtrosEnCarta;
	}
	public Plato getCartaSelected() {
		return cartaSelected;
	}
	public void setCartaSelected(Plato cartaSelected) {
		this.cartaSelected = cartaSelected;
	}
	public Plato getMenuSelected() {
		return menuSelected;
	}
	public void setMenuSelected(Plato menuSelected) {
		this.menuSelected = menuSelected;
	}
	public Plato getBebidaSelected() {
		return bebidaSelected;
	}
	public void setBebidaSelected(Plato bebidaSelected) {
		this.bebidaSelected = bebidaSelected;
	}
	public Plato getPlatoBanderaSelected() {
		return platoBanderaSelected;
	}
	public void setPlatoBanderaSelected(Plato platoBanderaSelected) {
		this.platoBanderaSelected = platoBanderaSelected;
	}
	public Plato getGuarnicionesSelected() {
		return guarnicionesSelected;
	}
	public void setGuarnicionesSelected(Plato guarnicionesSelected) {
		this.guarnicionesSelected = guarnicionesSelected;
	}
	public Plato getPostresSelected() {
		return postresSelected;
	}
	public void setPostresSelected(Plato postresSelected) {
		this.postresSelected = postresSelected;
	}
	public Plato getOtrosSelected() {
		return otrosSelected;
	}
	public void setOtrosSelected(Plato otrosSelected) {
		this.otrosSelected = otrosSelected;
	}
	public DetalleCreditoService getDetalleCreditoService() {
		return detalleCreditoService;
	}
	public void setDetalleCreditoService(DetalleCreditoService detalleCreditoService) {
		this.detalleCreditoService = detalleCreditoService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public Persona getPersonPorCobrar() {
		return personPorCobrar;
	}
	public void setPersonPorCobrar(Persona personPorCobrar) {
		this.personPorCobrar = personPorCobrar;
	}
	public List<Credito> getLstCobroCreditoMasivo() {
		return lstCobroCreditoMasivo;
	}
	public void setLstCobroCreditoMasivo(List<Credito> lstCobroCreditoMasivo) {
		this.lstCobroCreditoMasivo = lstCobroCreditoMasivo;
	}
	public BigDecimal getSumaTotalCreditos() {
		return sumaTotalCreditos;
	}
	public void setSumaTotalCreditos(BigDecimal sumaTotalCreditos) {
		this.sumaTotalCreditos = sumaTotalCreditos;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	


}
