package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Persona;

public interface PersonaRepository extends PagingAndSortingRepository<Persona, Integer>{

	List<Persona> findByEstado(Boolean status);
	List<Persona> findByEstadoAndCredito(Boolean status, boolean credito);
	Page<Persona> findAllByDniLikeAndApellidosLikeAndEstado(String dni, String names, Boolean status, Pageable pageable);
	Persona findByDni(String dni);
	
	@Query(nativeQuery = true,value = " SELECT * FROM persona  WHERE dni =:dni and id<>:idUser")
    Persona findByDniException(String dni, int idUser);
	

}
