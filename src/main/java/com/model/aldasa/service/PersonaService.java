package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Persona;

public interface PersonaService {
	
	Optional<Persona> findById(Integer id);
	Persona save(Persona entity);
	void delete(Persona entity);
	List<Persona> findByEstado(Boolean status);
	List<Persona> findByEstadoAndCredito(Boolean status, boolean credito);
	Persona findByDni(String dni);
	Persona findByDniException(String dni, int idUser);
	Page<Persona> findAllByDniLikeAndApellidosLikeAndEstado(String dni, String names, Boolean status, Pageable pageable);
	

}
