package com.model.aldasa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Credito;
import com.model.aldasa.entity.Persona;

public interface CreditoService {

	Optional<Credito> findById(Integer id);
	Credito save(Credito entity);
	void delete(Credito entity);
	
	Page<Credito> findAllByEstadoAndPersona(String estado, Persona persona, Pageable pageable);
	Page<Credito> findAllByEstado(String estado, Pageable pageable);
	
	Page<Credito> findAllByEstadoAndPersonaNombresLikeAndPersonaApellidosLike(String estado, String nombres, String apellidos, Pageable pageable);
	
	List<Credito> findAllByEstadoAndPersona(String estado, Persona persona);
	
	BigDecimal sumaTotal(String estado, int idPersona);
	BigDecimal sumaTotal(String estado);
}
