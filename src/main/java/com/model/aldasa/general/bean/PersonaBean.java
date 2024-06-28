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

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Persona;
import com.model.aldasa.service.PersonaService;
import com.model.aldasa.util.BaseBean;


@ManagedBean
@ViewScoped
public class PersonaBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{personService}")
	private PersonaService personaService;

	private LazyDataModel<Persona> lstPersonsLazy;

	private Persona personaSelected;

	private boolean estado = true;
	private String tituloDialog;

	
	@PostConstruct
	public void init() {
		iniciarLazy();
	}

	public void iniciarLazy() {
		lstPersonsLazy = new LazyDataModel<Persona>() {
			private List<Persona> datasource;

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}

			@Override
			public Persona getRowData(String rowKey) {
				int intRowKey = Integer.parseInt(rowKey);
				for (Persona person : datasource) {
					if (person.getId() == intRowKey) {
						return person;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Persona person) {
				return String.valueOf(person.getId());
			}

			@Override
			public List<Persona> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				// Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al
				// final y reemplazo los espacios por %, para hacer el LIKE
				// Si debageas aqui te vas a dar cuenta como lo captura
				String dni = "%" + (filterBy.get("dni") != null ? filterBy.get("dni").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String names = "%" + (filterBy.get("apellidos") != null ? filterBy.get("apellidos").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

				Sort sort = Sort.by("apellidos").ascending();
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
				Page<Persona> pagePerson = personaService.findAllByDniLikeAndApellidosLikeAndEstado(dni, names, estado,pageable);
				setRowCount((int) pagePerson.getTotalElements());
				return datasource = pagePerson.getContent();
			}
		};
	}

//	public void listarPersonas() {
//		lstPersons = personService.findByStatus(estado);
//	}

	public void newPerson() {
		tituloDialog = "NUEVA PERSONA";
		personaSelected = new Persona();
		personaSelected.setEstado(true); 
	}

	public void updatePerson() {
		tituloDialog = "MODIFICAR PERSONA";
	}

	public void savePersona() {
		if (personaSelected.getDni().equals("")) {
			addErrorMessage("Ingresar DNI.");
			return;
		}else {
			if(tituloDialog.equals("NUEVA PERSONA")) {
				Persona busca = personaService.findByDni(personaSelected.getDni());
				if(busca!=null) {
					addErrorMessage("Ya existe una persona con ese DNI");
					return;
				}
			}else {
				Persona busca = personaService.findByDniException(personaSelected.getDni(), personaSelected.getId());
				if(busca!=null) {
					addErrorMessage("Ya existe una persona con ese DNI");
					return;
				}
				
			}
		}
		
		if(personaSelected.getApellidos().equals("")) {
			addErrorMessage("Ingrese apellidos");
			return;
		}
		
		if(personaSelected.getNombres().equals("")) {
			addErrorMessage("Ingrese nombres.");
			return;
		}
		
		if(personaSelected.getTelefono().equals("")) {
			addErrorMessage("Ingrese telefono o celular.");
			return;
		}
		
		if(personaSelected.getFechaNacimiento()==null) {
			addErrorMessage("Ingrese fecha de nacimiento.");
			return;
		}
		
		if(personaSelected.getDireccion().equals("")) {
			addErrorMessage("Ingrese dirección.");
			return;
		}

		Persona guarda = personaService.save(personaSelected);
		if(guarda!= null){
			if(tituloDialog.equals("NUEVA PERSONA")) {
				personaSelected = new Persona();
				personaSelected.setEstado(true); 
			}
			addInfoMessage("Se guardó correctamente");
		}
	}

	public PersonaService getPersonaService() {
		return personaService;
	}

	public void setPersonaService(PersonaService personaService) {
		this.personaService = personaService;
	}

	public Persona getPersonaSelected() {
		return personaSelected;
	}

	public void setPersonaSelected(Persona personaSelected) {
		this.personaSelected = personaSelected;
	}

	public boolean getEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getTituloDialog() {
		return tituloDialog;
	}

	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}

	public LazyDataModel<Persona> getLstPersonsLazy() {
		return lstPersonsLazy;
	}

	public void setLstPersonsLazy(LazyDataModel<Persona> lstPersonsLazy) {
		this.lstPersonsLazy = lstPersonsLazy;
	}
	

}
