package com.model.aldasa.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Credito;
import com.model.aldasa.entity.Persona;

public interface CreditoRepository extends JpaRepository<Credito, Integer>{

	Page<Credito> findAllByEstadoAndPersona(String estado, Persona persona, Pageable pageable);
	Page<Credito> findAllByEstado(String estado, Pageable pageable);
	
	Page<Credito> findAllByEstadoAndPersonaNombresLikeAndPersonaApellidosLike(String estado, String nombres, String apellidos, Pageable pageable);
	
	List<Credito> findAllByEstadoAndPersona(String estado, Persona persona);
	
	@Query(nativeQuery = true,value = "SELECT sum(d.total) FROM credito d " + 
			" WHERE d.estado=:estado AND d.idPersona=:idPersona")
	BigDecimal sumaTotal(String estado, int idPersona);
	
	@Query(nativeQuery = true,value = "SELECT sum(d.total) FROM credito d " + 
			" WHERE d.estado=:estado")
	BigDecimal sumaTotal(String estado);
}
