package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Identificador;

public interface IdentificadorService {
	
	Optional<Identificador> findById(Integer id);
	Identificador save(Identificador entity);
	void delete(Identificador entity);
	
	List<Identificador> findByEstado(boolean estado);

}
